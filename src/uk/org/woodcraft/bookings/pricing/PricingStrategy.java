package uk.org.woodcraft.bookings.pricing;

import uk.org.woodcraft.bookings.datamodel.Booking;

public interface PricingStrategy {

	public double priceOf(Booking booking);
}
