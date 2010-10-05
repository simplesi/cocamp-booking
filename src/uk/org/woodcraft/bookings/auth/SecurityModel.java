package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class SecurityModel {

	public static void checkGlobalOperationAllowed(Operation operation)
	{
		checkAllowed(operation, null, null, null);
	}
	
	public static void checkAllowed(Operation operation, Organisation org)
	{
		checkAllowed(operation, org, null, null);
	}
	
	public static void checkAllowed(Operation operation, Unit unit)
	{
		checkAllowed(operation,  null, unit, null);
	}
	
	public static void checkAllowed(Operation operation, User user)
	{
		checkAllowed(operation,  null, null, user);
	}
	
	public static void checkAllowed(Operation operation, Organisation checkOrg, Unit checkUnit, User checkUser)
	{
		boolean permitted = false;
		
		// If this is a new unit / org, anyone can touch it
		if(checkOrg != null && checkOrg.isNew()) permitted = true;
		if(checkUnit != null && checkUnit.isNew()) permitted = true;
		if(checkUser != null && checkUser.isNew()) permitted = true;
		
		User user = SessionUtils.currentUser(false);
		
		if (user != null) {
			switch (user.getAccessLevel()) {
			case GLOBAL_ADMIN:
				permitted = true;
				break;
				
			case ORG_ADMIN:
				if(user.getOrganisation().equals(checkOrg)) permitted = true;
				if(checkUnit != null && checkUnit.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
				if(checkUser != null && checkUser.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
	
			case UNIT_ADMIN:
				if(user.getUnit().equals(checkUnit)) permitted = true;
				if(checkUser != null && checkUser.getUnitKey().equals(user.getUnitKey())) permitted = true;
				
			default:
				if (user.equals(checkUser)) permitted = true;
			}
		}
		

		
		if (!permitted)	throw new SecurityException("You do not have the right security access to do that action");
	}
}
