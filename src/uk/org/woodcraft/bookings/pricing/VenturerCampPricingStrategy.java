package uk.org.woodcraft.bookings.pricing;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.DateUtils;

public class VenturerCampPricingStrategy implements PricingStrategy {

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
	
		if (booking.getBookingCreationDate().after(event.getPublicEventStart()))
		{
			// At-event booking fee is 30, applies for any booking created after it started
			price += 30;
			
		} else if (booking.getBookingCreationDate().after(event.getBookingDeadline()))
		{
			// Late booking fee is 10, applies for any booking created after this time
			price += 10;
		}
	
		
		if (booking.getBookingUnlockDate() != null && booking.getBookingUnlockDate().after(event.getBookingAmendmentDeadline()))
		{
			// Amendment fee is 5, for any booking changed after the deadline
			price += 5;
		}
		
			
		
		if (booking.getCancellationDate() != null)
		{
			if (booking.getCancellationDate().before(event.getBookingDeadline()))
				// £0 for bookings cancelled before booking deadline, capped at fee
				return 0d;
			else
			{	
				// £50 for bookings cancelled after booking deadline, capped at fee
				return Math.min(50d, price);
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
		
		long price = 10 + (13 * chargableDays);
		
		// Price is capped at 112
		if (price > 112) price = 112;
		
		// Just in case something goes horribly wrong...
		if (price < 0 ) 
			throw new IllegalStateException("Price of booking was < 0! Price was : " + price);
		
		return price;
	}

}
