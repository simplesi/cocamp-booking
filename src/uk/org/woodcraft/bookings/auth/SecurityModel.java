package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Configuration;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class SecurityModel {
	
	public static void checkGlobalOperationAllowed(Operation operation)
	{
		checkAllowed(operation, null, null, null, null);
	}
	
	public static void checkAllowed(Operation operation, Object object)
	{
		if (object instanceof Organisation)
			checkAllowed(operation, (Organisation)object, null, null, null);
		else if (object instanceof Unit)
			checkAllowed(operation, null, (Unit)object, null, null);
		else if (object instanceof User)
			checkAllowed(operation, null, null, (User)object, null);
		else if (object instanceof Booking)
			checkAllowed(operation, null, null, null, (Booking)object);
	}
	
	public static void checkAllowed(Operation operation, Organisation org)
	{
		checkAllowed(operation, org, null, null, null);
	}
	
	public static void checkAllowed(Operation operation, Unit unit)
	{
		checkAllowed(operation,  null, unit, null, null);
	}
	
	public static void checkAllowed(Operation operation, User user)
	{
		checkAllowed(operation,  null, null, user, null);
	}
	
	// TODO: Move this to a secured iterface on the ModelObject instead
	public static void checkAllowed(Operation operation, Organisation checkOrg, Unit checkUnit, User checkUser, Booking checkBooking)
	{
		boolean permitted = false;
		
		// If this is a new unit / org, anyone can touch it, since they can be created in signup
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
				if(checkOrg != null && checkOrg.equals(user.getOrganisation())) permitted = true;
				if(checkUnit != null && checkUnit.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
				if(checkUser != null && checkUser.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
				if(checkBooking != null) 
				{
					Unit unit= CannedQueries.unitByKey(checkBooking.getUnitKey());
					if (unit.getOrganisationKey().equals(user.getOrganisationKey())) permitted = true;
				}
			case UNIT_ADMIN:
				if(user.getUnit().equals(checkUnit)) permitted = true;
				if(checkUser != null && checkUser.getUnitKey().equals(user.getUnitKey())) permitted = true;
				if(checkBooking != null && checkBooking.getUnitKey().equals(user.getUnitKey())) permitted = true;
			default:
				if (user.equals(checkUser)) permitted = true;
			}
		}
		

		
		if (!permitted)	throw new SecurityException("You do not have the right security access to do that action");
	}
	
	public static void checkIsAdminUser(SessionBasedAction action)
	{
		checkIsAdmin(action.getCurrentUser());
	}
	
	public static void checkIsAdminUser()
	{
		checkIsAdmin(SessionUtils.currentUser(false));
	}
	
	private static void checkIsAdmin(User user)
	{
		if (user == null)
			throw new SecurityException("You must be logged in to perform this action.");
		
		if (!user.getAccessLevel().equals(Accesslevel.GLOBAL_ADMIN))
			throw new SecurityException("You must be an administrator to perform this action");
	}
	
	public static void checkIsDevMode()
	{
		if(! Configuration.get().getBooleanProperty("isDev"))
		{
			throw new SecurityException("This action can only be performed on a development intance");
		}
	}
}
