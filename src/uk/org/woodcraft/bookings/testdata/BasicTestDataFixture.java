package uk.org.woodcraft.bookings.testdata;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.EventUnitVillageMapping;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.PMF;

public class BasicTestDataFixture extends TestFixture {
	
	@Override
	public void createStorageData() {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Users
		User user1 = new User("globaladmin@example.com", "Global Admin 1", "password", Accesslevel.GLOBAL_ADMIN);
		User user2 = new User("orgadmin@example.com", "Org Admin 1", "password", Accesslevel.ORG_ADMIN);
		User user3 = new User("unitadmin@example.com", "Unit Admin 1", "password", Accesslevel.UNIT_ADMIN);
		User user4 = new User("unassigned@example.com", "Unassigned 1", "password", Accesslevel.UNASSIGNED);
		pm.makePersistentAll(user1, user2, user3, user4);
		
		
		// Events
		List<Event> events = new ArrayList<Event>();
		Event event1 = new Event(TestConstants.EVENT1_NAME, TestConstants.EVENT1_START, TestConstants.EVENT1_END, true);
 		events.add(event1);
 		Event event2 = new Event("Other event", null, null, true);
		events.add(event2);		
		
		// FIXME: For some reason, un-commenting this line causes the fixture to fail with a failure to create an abstract class later...
		events.add(new Event("Closed event", null, null, false));	
		pm.makePersistentAll(events);
		
		// Villages
		List<Village> villages = new ArrayList<Village>();
		Village village1 = new Village("Village 1", event1);
		villages.add(village1);
		villages.add(new Village("Village 2", event1));
		villages.add(new Village("Empty village", event1));
		villages.add(new Village("Village on other event", events.get(1)));
		pm.makePersistentAll(villages);
		
		// Organisations
		List<Organisation> organisations = new ArrayList<Organisation>();
		organisations.add(new Organisation("Woodcraft Folk", true));
		organisations.add(new Organisation("Unapproved organisation", false));
		pm.makePersistentAll(organisations);
		
		// Units
		List<Unit> units = new ArrayList<Unit>();
		Unit unit1 = new Unit("Unit 1", organisations.get(0), true);
		unit1.addEventRegistration(event1);
		unit1.addEventRegistration(event2);	
		units.add(unit1);
		
		Unit unit2 = new Unit("Unit 2", organisations.get(0), true);
		unit2.addEventRegistration(event1);
		units.add(unit2);
		
		units.add(new Unit("Unapproved unit for wcf", organisations.get(0), false));
		units.add(new Unit("Unapproved unit", organisations.get(1), false));
		pm.makePersistentAll(units);
		
		// Unit village defaults
		List<EventUnitVillageMapping> unitVillageMapping = new ArrayList<EventUnitVillageMapping>();
		unitVillageMapping.add(new EventUnitVillageMapping(event1.getKeyCheckNotNull(), unit1.getKeyCheckNotNull(), village1.getKeyCheckNotNull()));
		pm.makePersistentAll(unitVillageMapping);
		
		// Bookings
		List<Booking> bookings = new ArrayList<Booking>();
		bookings.add(new Booking("Test person", unit1, event1));
		bookings.add(new Booking("Test person 2", unit1, event1));		
		bookings.add(new Booking("Person in unapproved, homeless unit", units.get(1), event1));
		bookings.add(new Booking("Test person in other event", unit1, events.get(1)));
		pm.makePersistentAll(bookings);
	}

}
