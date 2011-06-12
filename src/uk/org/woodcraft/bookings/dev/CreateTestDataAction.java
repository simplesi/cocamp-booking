package uk.org.woodcraft.bookings.dev;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.PMF;
import uk.org.woodcraft.bookings.test.TestFixture;

import com.opensymphony.xwork2.ActionSupport;

public class CreateTestDataAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	public String execute(){
		
		SecurityModel.checkIsDevMode();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Wipe out old data
		Class[] classesToDelete = new Class[]{
				Event.class, Organisation.class, Unit.class, Village.class, Booking.class
		};
		
		for(Class clazz: classesToDelete)
		{
			Query query = pm.newQuery(clazz);
		    query.deletePersistentAll();
		}
		
	
		// Insert new data
		TestFixture.BASIC_DATA.setUp();
		addActionMessage("Successfully created test data");
		return SUCCESS;
	}
}