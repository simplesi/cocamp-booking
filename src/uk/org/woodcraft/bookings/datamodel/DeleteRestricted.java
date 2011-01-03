package uk.org.woodcraft.bookings.datamodel;

import uk.org.woodcraft.bookings.utils.Clock;


public interface DeleteRestricted {
	
	/*
	 * If the delete conditions are met, "", otherwise error message
	 */
	public String getDeleteConditionError(Clock clock);
	public boolean deleteRequiresConfirmation();
}
