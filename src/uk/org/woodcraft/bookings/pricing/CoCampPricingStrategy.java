package uk.org.woodcraft.bookings.pricing;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;

public class CoCampPricingStrategy implements PricingStrategy {

	private Event eventInfo;
	
	public long priceOf(Booking booking) {
		Date firstPricableDate = booking.getArrivalDate();
		if(firstPricableDate.before(eventInfo.getPublicEventStart()))
			firstPricableDate = eventInfo.getPublicEventStart();
		
		Date lastPricableDate = booking.getDepartureDate();
		if(lastPricableDate.after(eventInfo.getPublicEventEnd()))
			lastPricableDate = eventInfo.getPublicEventEnd();
		
		//int chargableDays = Date
		
		return 0;
		
	}

}
