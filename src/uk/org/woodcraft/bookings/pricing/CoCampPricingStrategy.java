package uk.org.woodcraft.bookings.pricing;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.DateUtils;

public class CoCampPricingStrategy implements PricingStrategy {

	
	public double priceOf(Booking booking) {
		
		if (booking.getEventKey() == null) return 0;
		if (booking.getArrivalDate() == null) return 0;
		if (booking.getDepartureDate() == null) return 0;
		
		Event event = CannedQueries.eventByKey(booking.getEventKey());
		if (event == null) return 0;
		
		double price = priceForDuration(booking, event);
		
		if(booking.getDob() != null && 
				DateUtils.ageOnDay(booking.getDob(), event.getPublicEventStart()) <= 5)
		{
			// Those aged 5 and under are free
			price = 0;
		}
		
		
		if (booking.getBookingCreationDate().after(event.getBookingDeadline()))
		{
			// Late booking fee is 25
			price += 25;
		}
			
		
		if (booking.getCancellationDate() != null)
		{
			if (booking.getCancellationDate().before(event.getBookingDeadline()))
				// £75 for bookings cancelled after booking deadline, capped at fee
				return Math.min(25d, price);
			else
			{	
				// £75 for bookings cancelled after booking deadline, capped at fee
				return Math.min(75d, price);
			}
		}
		
		return price;
		
	}

	private double priceForDuration(Booking booking, Event event)
	{
		Date firstPricableDate = booking.getArrivalDate();
		if(firstPricableDate.before(event.getPublicEventStart()))
			firstPricableDate = event.getPublicEventStart();
		
		Date lastPricableDate = booking.getDepartureDate();
		if(lastPricableDate.after(event.getPublicEventEnd()))
			lastPricableDate = event.getPublicEventEnd();
		
		int chargableDays = DateUtils.daysBetween(lastPricableDate, firstPricableDate);
		
		long price = 35 + (15 * chargableDays);
		
		// Price is capped at 150
		if (price > 150) price = 150;
		
		// Just in case something goes horribly wrong...
		if (price < 0 ) 
			throw new IllegalStateException("Price of booking was < 0! Price was : " + price);
		
		return price;
	}
	
}
