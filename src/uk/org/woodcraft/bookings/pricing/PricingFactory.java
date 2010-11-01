package uk.org.woodcraft.bookings.pricing;

public class PricingFactory {

	private static final PricingStrategy PRICING_STRATEGY = new CoCampPricingStrategy();
	
	
	public static PricingStrategy getPricingStrategy()
	{
		return PRICING_STRATEGY;
	}
}
