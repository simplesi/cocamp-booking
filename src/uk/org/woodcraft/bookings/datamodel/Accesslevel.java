package uk.org.woodcraft.bookings.datamodel;

public enum Accesslevel {

	GLOBAL_ADMIN(true, true, true),
	ORG_ADMIN(true, false, true),
	UNIT_ADMIN(true, false, false),
	UNASSIGNED(false),
	AWAITING_EMAIL_CONFIRM(false),
	NO_LOGIN(false);
	
	private final boolean canLogin;
	private final boolean canChangeOrg;
	private final boolean canChangeUnit;
	
	private Accesslevel(boolean permitLogin) {
		this(permitLogin, false, false);
	}
	
	Accesslevel(boolean permitLogin, boolean canChangeOrg, boolean canChangeUnit)
	{
		this.canLogin = permitLogin;
		this.canChangeOrg = canChangeOrg;
		this.canChangeUnit = canChangeUnit;
	}

	public boolean getCanLogin() {
		return canLogin;
	}
	
	public Boolean getCanChangeOrg() {
		return canChangeOrg;
	}
	
	public Boolean getCanChangeUnit() {
		return canChangeUnit;
	}
	
}
