package uk.org.woodcraft.bookings.testdata;

import javax.jdo.PersistenceManager;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.PMF;
import uk.org.woodcraft.bookings.utils.DateUtils;

public class TestDataGenerator {

	public static void generateData()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Event e1 = new Event("Test event", DateUtils.getDate(2011, 1, 1), 
				DateUtils.getDate(2011, 1, 10));
		pm.makePersistent(e1);
		
		
		
		
		
	}
		
}
