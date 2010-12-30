package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.org.woodcraft.bookings.accounts.LineItem;
import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.AgeGroup;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.SystemClock;

import com.opensymphony.xwork2.ActionSupport;

public class AccountsAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 5349573159349600392L;

	private Map<String, Object> sessionData;
	List<LineItem> costs = new ArrayList<LineItem>();
	List<Transaction> payments = new ArrayList<Transaction>();
	
	private Clock clock = new SystemClock();
	
	public String accountsForUnit()
	{
		Unit unit = (Unit)sessionData.get(SessionConstants.CURRENT_UNIT);
		SecurityModel.checkAllowed(Operation.WRITE, unit);
		
		Collection<Booking> bookings = CannedQueries.bookingsForUnit(unit, (Event)sessionData.get(SessionConstants.CURRENT_EVENT));
		prepareForData(bookings, null);
		return SUCCESS;
	}

	public String accountsForOrg()
	{
		Organisation org = (Organisation)sessionData.get(SessionConstants.CURRENT_ORG);
		SecurityModel.checkAllowed(Operation.WRITE, org);
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org, (Event)sessionData.get(SessionConstants.CURRENT_EVENT));
		prepareForData(bookings, null);
		return SUCCESS;
	}
	
	public void prepareForData(Collection<Booking> bookings, Collection<Transaction> transactions)
	{
		Map<BookingsBucket, Collection<Booking>> bucketedBookings = bucketBookings(bookings);	
		for(Map.Entry<BookingsBucket, Collection<Booking>> entry : bucketedBookings.entrySet())
		{
			costs.add(bucketedBookingsToLineItem(entry.getKey(), entry.getValue()));
		}
		
		
		// Build transaction list, both bills and payments
		
		
	}
	
	private LineItem bucketedBookingsToLineItem(BookingsBucket bucket, Collection<Booking> contents)
	{
		return new LineItem(null, TransactionType.Fees, String.format("%s - %d days", bucket.ageGroup.toString(), bucket.days), contents.size(), bucket.fee);
	}
	
	
	
	private Map<BookingsBucket, Collection<Booking>> bucketBookings( Collection<Booking> bookings)
	{
		Map<BookingsBucket, Collection<Booking>> buckets = new HashMap<AccountsAction.BookingsBucket, Collection<Booking>>();
		for(Booking booking: bookings)
		{
			BookingsBucket bucketKey = new BookingsBucket(booking.getAgeGroup(), 
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
			total += item.getQuantity();
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
		return ((Event)sessionData.get(SessionConstants.CURRENT_EVENT)).getEarlyBookingDeadline().after(clock.getTime());
	}
	
	public Date getEarlyBookingsDate()
	{
		return ((Event)sessionData.get(SessionConstants.CURRENT_EVENT)).getEarlyBookingDeadline();
	}
	
	public double getEarlyBookingsAmount()
	{
		return getTotalCost() / 2;
	}
	
	class BookingsBucket
	{
		final AgeGroup ageGroup;
		final int days;
		final double fee;
		 
		public BookingsBucket(AgeGroup ageGroup, int days, double fee) {
			this.ageGroup = ageGroup;
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
			
			return ageGroup.equals(other.ageGroup) 
				&& days == other.days;
		}
	}


	@Override
	public void setSession(Map<String, Object> sessionData) {
		this.sessionData = sessionData;
	}
	
}
