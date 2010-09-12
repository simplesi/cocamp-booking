package uk.org.woodcraft.bookings.testdata;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.PMF;

public class BasicTestDataFixture extends TestFixture {

	
	@Override
	public void createStorageData() {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Events
		List<Event> events = new ArrayList<Event>();
		events.add(new Event("Test event 1", TestConstants.EVENT_START, TestConstants.EVENT_END, true));
		events.add(new Event("Other event", null, null, false));		
		
		// FIXME: For some reason, uncommenting this line causes the fixture to fail with a failure to create an abstract class later...
//		events.add(new Event("Closed event", null, null, false));	
		pm.makePersistentAll(events);
		
		// Villages
		List<Village> villages = new ArrayList<Village>();
		villages.add(new Village("Village 1", events.get(0)));
		villages.add(new Village("Village 2", events.get(0)));
		villages.add(new Village("Empty village", events.get(0)));
		pm.makePersistentAll(villages);
		
		// Organisations
		List<Organisation> organisations = new ArrayList<Organisation>();
		organisations.add(new Organisation("Woodcraft Folk", true));
		organisations.add(new Organisation("Unapproved organisation", false));
		pm.makePersistentAll(organisations);
		
		// Units
		List<Unit> units = new ArrayList<Unit>();
		units.add(new Unit("Unit 1", organisations.get(0), true));
		units.add(new Unit("Unapproved unit", organisations.get(1), false));
		pm.makePersistentAll(units);
		
		// Bookings
		List<Booking> bookings = new ArrayList<Booking>();
		bookings.add(new Booking("Test person", units.get(0), events.get(0)));
		bookings.add(new Booking("Test person 2", units.get(0), events.get(0)));		
		bookings.add(new Booking("Person in unapproved unit", units.get(1), events.get(0)));
		pm.makePersistentAll(units);
	}

}
