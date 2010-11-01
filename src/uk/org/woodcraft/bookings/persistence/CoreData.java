package uk.org.woodcraft.bookings.persistence;

import javax.jdo.PersistenceManager;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.User;

/***
 * Data that will be available in production (and tests)
 */
public class CoreData {

	public static final String SYSTEM_USER_EMAIL = "system@bookings";
	public static final User SYSTEM_USER = new User(SYSTEM_USER_EMAIL, "System User", "", Accesslevel.NO_LOGIN);
	
	public static void createCoreData()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Annoyingly datanucleus won't let us use the copy up above...
		pm.makePersistent(new User(SYSTEM_USER_EMAIL, "System User", "", Accesslevel.NO_LOGIN));
		
		pm.close();
	}
	
}
