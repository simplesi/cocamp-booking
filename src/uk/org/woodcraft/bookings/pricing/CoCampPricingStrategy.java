package uk.org.woodcraft.bookings.pricing;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.DateUtils;

public class CoCampPricingStrategy implements PricingStrategy {

	
	public long priceOf(Booking booking) {
		
		if (booking.getEventKey() == null) return 0;
		if (booking.getArrivalDate() == null) return 0;
		if (booking.getDepartureDate() == null) return 0;
		
		Event event = CannedQueries.eventByKey(booking.getEventKey());
		
		long price = priceForDuration(booking, event);
		
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
		
		
		// TODO: Need to handle cancellation
		
		return price;
		
	}

	private long priceForDuration(Booking booking, Event event)
	{
		Date firstPricableDate = booking.getArrivalDate();
		if(firstPricableDate.before(event.getPublicEventStart()))
			firstPricableDate = event.getPublicEventStart();
		
		Date lastPricableDate = booking.getDepartureDate();
		if(lastPricableDate.after(event.getPublicEventEnd()))
			lastPricableDate = event.getPublicEventEnd();
		
		int chargableDays = (int) (lastPricableDate.getTime() - firstPricableDate.getTime()) / (24 * 60 * 60 * 1000);
		
		long price = 35 + (15 * chargableDays);
		
		// Price is capped at 150
		if (price > 150) price = 150;
		
		// Just in case something goes horribly wrong...
		if (price < 0 ) 
			throw new IllegalStateException("Price of booking was < 0! Price was : " + price);
		
		return price;
	}
	
}
