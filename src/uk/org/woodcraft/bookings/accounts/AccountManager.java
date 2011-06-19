package uk.org.woodcraft.bookings.accounts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.DateUtils;

public class AccountManager {

	public AccountManager(Event event) {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		
		Collection<Booking> bookings = CannedQueries.bookingsForEvent(event);
		Collection<Transaction> transactions = CannedQueries.transactionsForEvent(event);
		prepareForData(bookings, transactions);
	}
	
	public AccountManager(Event event, Organisation org)
	{
		SecurityModel.checkAllowed(Operation.WRITE, org);
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org, event);
		Collection<Transaction> transactions = CannedQueries.transactionsForOrg(org, event);
		
		prepareForData(bookings, transactions);
	}
	
	public AccountManager(Event event, Unit unit)
	{
		SecurityModel.checkAllowed(Operation.WRITE, unit);
		
		Collection<Booking> bookings = CannedQueries.bookingsForUnit(unit, event);
		Collection<Transaction> transactions = CannedQueries.transactionsForUnit(unit, event);
		prepareForData(bookings, transactions);
	}
	
	private List<LineItem> costs = new ArrayList<LineItem>();
	private List<Transaction> payments = new ArrayList<Transaction>();
	
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
		Map<BookingsBucket, Collection<Booking>> buckets = new HashMap<BookingsBucket, Collection<Booking>>();
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
	
	
	
}
