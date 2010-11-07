package uk.org.woodcraft.bookings.datamodel;


public interface DeleteRestricted {
	
	/*
	 * If the delete conditions are met, "", otherwise error message
	 */
	public String getDeleteConditionError();
	public boolean deleteRequiresConfirmation();
}
