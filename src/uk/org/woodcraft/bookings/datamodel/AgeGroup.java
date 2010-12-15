package uk.org.woodcraft.bookings.datamodel;

public enum AgeGroup {
	Unknown(0,0),
	Woodchip(0,5),
	Elfin(6,8),
	Pioneer(9,12),
	Venturer(13,15),
	DF(16,20),
	Leader(21,-1);

	
	int ageLower;
	int ageUpper;
	
	AgeGroup(int ageLower, int ageUpper)
	{
		this.ageLower = ageLower;
		this.ageUpper = ageUpper;
	}
	
	public String toString()
	{	
		if (ageUpper == ageLower)
			return "";
		
		if (ageUpper == -1)
		{
			return String.format("%s (%d+)", name(), ageLower); 
		}
		return String.format("%s (%d-%d)", name(), ageLower, ageUpper);
	}
	
	public static AgeGroup groupFor(int age)
	{
		for(AgeGroup group: values())
		{
			if (group.ageLower <= age 
					&& (group.ageUpper == -1 || group.ageUpper >= age ) ) 
				return group;
		}
		return null;
	}
}
