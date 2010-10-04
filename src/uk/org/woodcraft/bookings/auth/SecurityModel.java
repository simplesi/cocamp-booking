package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class SecurityModel {

	public static void checkGlobalOperationAllowed(Operation operation)
	{
		checkAllowed(operation, null, null);
	}
	
	public static void checkAllowed(Operation operation, Organisation org)
	{
		checkAllowed(operation, org, null);
	}
	
	public static void checkAllowed(Operation operation, Unit unit)
	{
		checkAllowed(operation,  null, unit);
	}
	
	public static void checkAllowed(Operation operation, Organisation org, Unit unit)
	{
		User user = SessionUtils.currentUser(false);
		
		boolean permitted = false;
		if (user != null) {
			switch (user.getAccessLevel()) {
			case GLOBAL_ADMIN:
				permitted = true;
				break;
				
			case ORG_ADMIN:
				if(user.getOrganisation().equals(org)) permitted = true;
				if(unit != null && unit.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
	
			case UNIT_ADMIN:
				if(user.getUnit().equals(unit)) permitted = true;
				
			default:
			}
		}
		
		// If this is a new unit / org, anyone can touch it
		if(org != null && org.isNew()) permitted = true;
		if(unit != null && unit.isNew()) permitted = true;
		
		if (!permitted)	throw new SecurityException("You do not have the right security access to do that action");
	}
}
