package uk.org.woodcraft.bookings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Predicates;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;

import com.google.appengine.api.datastore.Key;

public class ApplyCoCampDiscountsAction extends SessionBasedAction {

	private static final long serialVersionUID = 4164806583384446180L;

	public static final String SESSION_DISCOUNT_LIST = "DISCOUNT_LIST";
	private static final double DISCOUNT_AMOUNT = 10.0d;
	
	private static final Predicate SELECT_DISCOUNT = new Predicates.TransactionTypePredicate(TransactionType.Discount);
	
	private List<DiscountLine> discountLines = new ArrayList<DiscountLine>();
	private double totalDiscount;
	
	public String generateDiscounts() {
		SecurityModel.checkIsAdminUser(this);
		Event event = getCurrentEvent();
		
		Map<Unit, List<Transaction>> transactionsByUnit = getRelevantTransactions(event);
		Map<Key, List<Booking>> bookingsByUnit = getRelevantBookings(event);
		
		List<Transaction> allDiscounts = new ArrayList<Transaction>();
		totalDiscount = 0;
		for(Unit unit : transactionsByUnit.keySet())
		{
			DiscountLine unitDiscount = new DiscountLine(getClock(), event, unit, bookingsByUnit.get(unit.getKeyCheckNotNull()), transactionsByUnit.get(unit), DISCOUNT_AMOUNT);
			getDiscountLines().add(unitDiscount);
			
			// Only persist the discount if it is non-zero
			if (unitDiscount.getDiscount().getAmount() != 0)
				allDiscounts.add(unitDiscount.getDiscount());
			totalDiscount += unitDiscount.getDiscount().getAmount();
		}
		
		// Cache it so that we can apply these discounts if approved
		setSessionObject(SESSION_DISCOUNT_LIST, (Serializable) allDiscounts);
		
		return SUCCESS;
	}
	
	
	public String confirmDiscounts() {
		SecurityModel.checkIsAdminUser(this);
		@SuppressWarnings("unchecked")
		List<Transaction> discountsToApply = (List<Transaction>)getSessionObject(SESSION_DISCOUNT_LIST);
		
		if (discountsToApply == null)
		{
			addActionError("Unable to find the set of discounts to add! Failed to set new discounts");
			return ERROR;
		}
		
		// This really needs to be atomic, but hey ho.
		
		// First purge existing discounts for this event
		@SuppressWarnings("unchecked")
		Collection<Transaction> discounts = CollectionUtils.select(CannedQueries.transactionsForEvent(getCurrentEvent()), SELECT_DISCOUNT);
		
		// Can't delete all in one transaction as seperate entity groups
		for(Transaction discount : discounts)
			CannedQueries.delete(discount);
		
		CannedQueries.save(discountsToApply);
		addActionMessage(String.format("Successfully added %d discounts", discountsToApply.size()));
		
		return SUCCESS;
	}
	
	public Map<Unit, List<Transaction>> getRelevantTransactions(Event event)
	{
		Map<Unit, List<Transaction>> relevantTransactions = new HashMap<Unit, List<Transaction>>();
		
		Date eventEarlyBookingDeadline = DateUtils.cleanupTime(event.getEarlyBookingDeadline());
		
		for(Transaction transaction : CannedQueries.transactionsForEvent(event))
		{
			if (!DateUtils.cleanupTime(transaction.getTimestamp()).after(eventEarlyBookingDeadline) || transaction.getType() == TransactionType.Discount)
			{
				// Ignore any additional bills
				if ( !transaction.getType().getIsCost())
				{
					Unit unit = transaction.getUnit();
					List<Transaction> transactionsForUnit = relevantTransactions.get(unit);
					
					if (transactionsForUnit == null )
					{
						transactionsForUnit = new ArrayList<Transaction>();
						relevantTransactions.put(unit, transactionsForUnit);
					}	
					
					transactionsForUnit.add(transaction);
				}
			}
		}
		return relevantTransactions;
	}
	
	public Map<Key,List<Booking>> getRelevantBookings(Event event)
	{
		Map<Key, List<Booking>> relevantBookings = new HashMap<Key, List<Booking>>();
		
		Date eventEarlyBookingDeadline = DateUtils.cleanupTime(event.getEarlyBookingDeadline());
		for(Booking booking : CannedQueries.bookingsForEvent(event))
		{
			if (!DateUtils.cleanupTime(booking.getBookingCreationDate()).after(eventEarlyBookingDeadline))
			{
				// Only full price bookings apply
				if (booking.getFee() == 150d)
				{
					Key unitKey = booking.getUnitKey();
					List<Booking> bookingsForUnit = relevantBookings.get(unitKey);
					if (bookingsForUnit == null)
					{
						bookingsForUnit = new ArrayList<Booking>();
						relevantBookings.put(unitKey, bookingsForUnit);
					}
					
					bookingsForUnit.add(booking);
				}
			}
		}
		return relevantBookings;		
	}


	public List<DiscountLine> getDiscountLines() {
		return discountLines;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public static class DiscountLine implements Serializable
	{
		private static final long serialVersionUID = -7084864059096169005L;
	
		private Unit unit;
		private Collection<Booking> paidBookings = new ArrayList<Booking>();
		private Collection<Booking> unpaidBookings = new ArrayList<Booking>();
		private Collection<Transaction> qualifyingPayments;

		private Transaction previousDiscount;
		private Transaction discount;
		
		public DiscountLine(Clock clock, Event event, Unit unit, List<Booking> qualifyingBookings, 
				 				List<Transaction> qualifyingTransactions, 
				 				double rebateAmountPerBooking) 
		{
			this.unit = unit; 
			
			@SuppressWarnings("unchecked")
			Collection<Transaction> qualifyingPayments = CollectionUtils.selectRejected(qualifyingTransactions, SELECT_DISCOUNT);
						
			this.previousDiscount = (Transaction) CollectionUtils.find(qualifyingTransactions, SELECT_DISCOUNT);
			calculateDiscount(clock.getTime(), event, qualifyingBookings, qualifyingPayments, rebateAmountPerBooking);
		}
		
		private void calculateDiscount( Date timestamp, Event event, Collection<Booking> qualifyingBookings, Collection<Transaction> qualifyingPayments, double rebateAmount)
		{
			this.qualifyingPayments = qualifyingPayments;
			double qualifyingPaymentTotal = getTotalQualifyingPayments();
			double balance = qualifyingPaymentTotal;
			
			// iterate the bookings until we have no money left
			for(Booking booking : qualifyingBookings)
			{
				if (balance > booking.getFee() / 2)
				{
					balance -= (booking.getFee() / 2);
					paidBookings.add(booking);
				} else {
					unpaidBookings.add(booking);
				}
			}
		
			int numberOfDiscountBookings = paidBookings.size();
			double amountToDiscount = numberOfDiscountBookings * rebateAmount;
			String discountName = String.format("Earlybird booking discount for %d people", numberOfDiscountBookings);
			String discountComment = String.format("%.2f received prior to deadline", qualifyingPaymentTotal);
			if (unpaidBookings.size() > 0)
				discountComment += String.format("; There were %d additional bookings with insufficient funds received to qualify for the discount", 
										unpaidBookings.size());
			
			discount = new Transaction(unit.getKeyCheckNotNull(), event.getKeyCheckNotNull(), 
					timestamp, TransactionType.Discount, discountName, discountComment, amountToDiscount);
			
		}
		
		public double getTotalQualifyingPrice()
		{
			double total = 0;
			for(Booking booking : paidBookings)
				total += booking.getFee()/2;
			return total;
		}
		
		public double getTotalQualifyingPayments()
		{
			double total = 0;
			for(Transaction transaction : qualifyingPayments)
				total += transaction.getAmount();
			return total;
		}

		public Unit getUnit() {
			return unit;
		}


		public Collection<Booking> getPaidBookings() {
			return paidBookings;
		}
		
		public Collection<Booking> getUnpaidBookings() {
			return unpaidBookings;
		}
		
		public Collection<Transaction> getQualifyingPayments() {
			return qualifyingPayments;
		}

		public Transaction getPreviousDiscount() {
			return previousDiscount;
		}
		
		public Transaction getDiscount() {
			return discount;
		}	
		
		@Override
		public String toString() {
			return String.format("%s - %.2f discount, %.2f paid, %d bookings covered, %d bookings outstanding, %s", 
						unit.getName(), discount.getAmount(), getTotalQualifyingPayments(), paidBookings.size(), unpaidBookings.size(), discount.getName());
		}
	}
}
