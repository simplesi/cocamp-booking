package uk.org.woodcraft.bookings;

public class CannedReportLabel
{
	private String tag;
	private String displayName;
	private String description;
	
	public CannedReportLabel( String tag, String displayName, String description) {
		this.tag = tag;
		this.displayName = displayName;
		this.description = description;
	}

	public String getTag() {
		return tag;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDescription() {
		return description;
	}
	
	
}
