package uk.org.woodcraft.bookings.pricing;

import uk.org.woodcraft.bookings.datamodel.Event;

public class PricingFactory {

	// TODO: This whole class should disappear.
	public static PricingStrategy getPricingStrategy(Event event)
	{
		RegisteredPricingStrategy registered = event.getRegisteredPricingStrategy();
		if (registered == null) throw new IllegalStateException(String.format("All events must have a pricing strategy, %s is missing one", event.getName()));
		
		return registered.getStrategy();
	}
}
