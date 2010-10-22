package uk.org.woodcraft.bookings.pricing;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class CoCampPricingStrategy implements PricingStrategy {

	public long priceOf(Booking booking) {
		
		if (booking.getEventKey() == null) return 0;
		if (booking.getArrivalDate() == null) return 0;
		if (booking.getDepartureDate() == null) return 0;
		
		Event eventInfo = CannedQueries.eventByKey(booking.getEventKey());
		
		Date firstPricableDate = booking.getArrivalDate();
		if(firstPricableDate.before(eventInfo.getPublicEventStart()))
			firstPricableDate = eventInfo.getPublicEventStart();
		
		Date lastPricableDate = booking.getDepartureDate();
		if(lastPricableDate.after(eventInfo.getPublicEventEnd()))
			lastPricableDate = eventInfo.getPublicEventEnd();
		
		//int chargableDays = Date
		
		// Need to handle cancellation
		
		return 0;
		
	}

}
