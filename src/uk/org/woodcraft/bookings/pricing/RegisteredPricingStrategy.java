package uk.org.woodcraft.bookings.pricing;

public enum RegisteredPricingStrategy {
	
	COCAMP("CoCamp", new CoCampPricingStrategy()),
	VENTURER_CAMP("Venturer Camp", new VenturerCampPricingStrategy());
	
	private String displayName;
	private PricingStrategy strategy;
	
	private RegisteredPricingStrategy(String displayName, PricingStrategy strategy)
	{
		this.displayName = displayName;
		this.strategy = strategy;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public PricingStrategy getStrategy()
	{
		return strategy;
	}
}
