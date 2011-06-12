package uk.org.woodcraft.bookings.datamodel;

public enum Accesslevel {

	GLOBAL_ADMIN("Global Admin", true, true, true, true),
	ORG_ADMIN("Organisation bookings secretary", true, false, true, false),
	UNIT_ADMIN("Unit booking secretary", true, false, false, false),
//	UNASSIGNED("Unassigned user"),
//	AWAITING_EMAIL_CONFIRM("New user pending approval"),
	NO_LOGIN("System user"),
	API("API Access");
	
	private final String displayName;
	private final boolean canLogin;
	private final boolean canChangeOrg;
	private final boolean canChangeUnit;
	private final boolean canManageUsers;
	
	private Accesslevel(String displayName) {
		this(displayName, false, false, false, false);
	}
	
	Accesslevel(String displayName, boolean permitLogin, boolean canChangeOrg, boolean canChangeUnit, boolean canManageUsers)
	{
		this.displayName = displayName;
		this.canLogin = permitLogin;
		this.canChangeOrg = canChangeOrg;
		this.canChangeUnit = canChangeUnit;
		this.canManageUsers = canManageUsers;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}

	public boolean getCanLogin() {
		return canLogin;
	}
	
	public boolean getCanChangeOrg() {
		return canChangeOrg;
	}
	
	public boolean getCanChangeUnit() {
		return canChangeUnit;
	}
	
	public boolean getCanManageUsers() {
		return canManageUsers;
	}
	
	public boolean getIsSuperUser() {
		return GLOBAL_ADMIN.equals(this);
	}
	
	public boolean getCanManageOrg() {
		return canChangeUnit;
	}
	
	
}
