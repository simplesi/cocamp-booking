package uk.org.woodcraft.bookings.datamodel;

public enum Accesslevel {

	GLOBAL_ADMIN(true),
	ORG_ADMIN(true),
	UNIT_ADMIN(true),
	UNASSIGNED(false),
	AWAITING_EMAIL_CONFIRM(false),
	NO_LOGIN(false);
	
	private final boolean permitLogin;
	
	Accesslevel(boolean permitLogin)
	{
		this.permitLogin = permitLogin;
	}

	public boolean canLogin() {
		return permitLogin;
	}
}
