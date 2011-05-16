package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.woodcraft.bookings.accounts.LineItem;
import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.AgeGroup;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.SystemClock;

public class AccountsAction extends SessionBasedAction{

	private static final long serialVersionUID = 5349573159349600392L;

	private String viewLevel = "unit";
	private List<LineItem> costs = new ArrayList<LineItem>();
	private List<Transaction> payments = new ArrayList<Transaction>();
	
	private Clock clock = new SystemClock();
	
	public String accountsForUnit()
	{
		Unit unit = getCurrentUnit();
		Event event = getCurrentEvent();
		SecurityModel.checkAllowed(Operation.WRITE, unit);
		
		Collection<Booking> bookings = CannedQueries.bookingsForUnit(unit, event);
		Collection<Transaction> transactions = CannedQueries.transactionsForUnit(unit, event);
		prepareForData(bookings, transactions);
		return SUCCESS;
	}

	public String accountsForOrg()
	{
		Organisation org = getCurrentOrganisation();
		Event event = getCurrentEvent();
		
		SecurityModel.checkAllowed(Operation.WRITE, org);
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org, getCurrentEvent());
		Collection<Transaction> transactions = CannedQueries.transactionsForOrg(org, event);
		
		prepareForData(bookings, transactions);
		viewLevel = "organisation";
		return SUCCESS;
	}
	
	public String accountsForAll()
	{
		Event event = getCurrentEvent();
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		Collection<Booking> bookings = CannedQueries.bookingsForEvent(event);
		Collection<Transaction> transactions = CannedQueries.transactionsForEvent(event);
		prepareForData(bookings, transactions);
		viewLevel = "event";
		return SUCCESS;
	}
	
	public void prepareForData(Collection<Booking> bookings, Collection<Transaction> transactions)
	{
		Map<BookingsBucket, Collection<Booking>> bucketedBookings = bucketBookings(bookings);	
		
		List<BookingsBucket> buckets = new ArrayList<BookingsBucket>(bucketedBookings.keySet());
		Collections.sort(buckets);
		
		for(BookingsBucket bucket : buckets)
		{
			costs.add(bucketedBookingsToLineItem(bucket, bucketedBookings.get(bucket)));
		}
		
		for(Transaction transaction : transactions)
		{
			if (transaction.getType().getIsCost())
			{
				LineItem item = new LineItem(transaction.getTimestamp(), transaction.getType(), transaction.getName(), null, transaction.getAmount(), 0);				
				costs.add(item);
			} else {
				payments.add(transaction);
			}
		}
		
	}
	
	private LineItem bucketedBookingsToLineItem(BookingsBucket bucket, Collection<Booking> contents)
	{
		String cancellation = "";
		
		int bookingQuantity = contents.size();
		if (bucket.isCancelled)
		{
			cancellation = "(cancelled) ";
			bookingQuantity = 0;
		}
		return new LineItem(null, TransactionType.Fees, 
				String.format("%s %s- %d days", bucket.ageGroup.toString(), cancellation, bucket.days), contents.size(), bucket.fee, bookingQuantity);
	}
	
	
	
	private Map<BookingsBucket, Collection<Booking>> bucketBookings( Collection<Booking> bookings)
	{
		Map<BookingsBucket, Collection<Booking>> buckets = new HashMap<AccountsAction.BookingsBucket, Collection<Booking>>();
		for(Booking booking: bookings)
		{
			BookingsBucket bucketKey = new BookingsBucket(booking.getAgeGroup(), booking.getIsCancelled(),
												DateUtils.daysBetween(booking.getArrivalDate(), booking.getDepartureDate()), 
												booking.getFee());
			if (!buckets.containsKey(bucketKey))
				buckets.put(bucketKey, new ArrayList<Booking>());
			
			Collection<Booking> bucket = buckets.get(bucketKey);
			bucket.add(booking);			
		}
		
		return buckets;
	}
	
	public List<LineItem> getCosts()
	{
		return costs;
	}
	
	public double getTotalCost()
	{
		double total = 0;
		for(LineItem item : costs)
			total += item.getLinePrice();
		
		return total;
	}
	
	public int getTotalBookingsCount()
	{
		int total = 0;
		for(LineItem item : costs)
		{
			if(item.getType() == TransactionType.Fees)
			total += item.getBookingQuantity();
		}
		return total;
	}
	
	public List<Transaction> getPayments()
	{
		return payments;
	}
	
	public double getPaymentTotal()
	{
		double total = 0;
		for(Transaction item : payments)
			total += item.getAmount();
		
		return total;
	}
	
	public double getBalance()
	{
		return getTotalCost() - getPaymentTotal();
	}
	
	public boolean getIsBeforeEarlyBookingDeadline()
	{
		return getCurrentEvent().getEarlyBookingDeadline().after(clock.getTime());
	}
	
	public Date getEarlyBookingsDate()
	{
		return getCurrentEvent().getEarlyBookingDeadline();
	}
	
	public double getEarlyBookingsAmount()
	{
		return getTotalCost() / 2;
	}
	
	public String getViewLevel()
	{
		return viewLevel;
	}
	
	class BookingsBucket implements Comparable<BookingsBucket>
	{
		final AgeGroup ageGroup;
		final boolean isCancelled;
		final int days;
		final double fee;
		 
		public BookingsBucket(AgeGroup ageGroup, boolean isCancelled, int days, double fee) {
			this.ageGroup = ageGroup;
			this.isCancelled = isCancelled;
			this.days = days;
			this.fee = fee;
		}
		 
		@Override
		public int hashCode() {
			return ageGroup.hashCode();
		}
		 
		@Override
		public boolean equals(Object obj) {
			if (! (obj instanceof BookingsBucket)) return false;
			BookingsBucket other = (BookingsBucket) obj;
			
			return ageGroup.equals(other.ageGroup) && isCancelled == other.isCancelled
				&& days == other.days && fee == other.fee;
		}

		// Cancellation, Agegroup, days (desc)
		@Override
		public int compareTo(BookingsBucket o) {
			if (isCancelled != o.isCancelled)
			{
				if (isCancelled) 
					return 1;
				else
					return -1;
			}
			if (ageGroup != o.ageGroup) return ageGroup.compareTo(o.ageGroup);
			if (days == o.days) return 0;
			if (days > o.days) return -1;
			if (fee < o.fee) return -1; // want people who have booked late to come after those that have booked early
			return 1;
		}
	}
	
}
