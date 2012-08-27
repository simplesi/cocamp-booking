package uk.org.woodcraft.bookings.test;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.AppSetting;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.CoreData;
import uk.org.woodcraft.bookings.persistence.PMF;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.TestClock;

public class BasicTestDataFixture extends TestFixture {
	
	// If you get this failing with an inability to create an abstract class in JDO, ensure the data class has a default constructor...
	
	@Override
	public void createStorageData() {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//Transaction tx = pm.currentTransaction();
		try {
			//tx.begin();
			
			CoreData.createCoreData();
			
			Clock testClock = new TestClock(TestConstants.DATE_BEFORE_EARLY_DEADLINE);
			
			// Events
			List<Event> events = new ArrayList<Event>();
			Event event1 = new Event(TestConstants.EVENT1_NAME, TestConstants.EVENT1_START, TestConstants.EVENT1_END, true);
			event1.setEarlyBookingDeadline(DateUtils.getDate(2011, 0, 1));
			event1.setBookingDeadline(DateUtils.getDate(2011, 4, 2));
			event1.setBookingSystemLocked(DateUtils.getDate(2011, 7, 28));
	 		
			
			events.add(event1);
	 		Event event2 = new Event("Other event", null, null, true);
			events.add(event2);		
			
			events.add(new Event("Closed event", null, null, false));	
			pm.makePersistentAll(events);
			
			// Villages
			List<Village> villages = new ArrayList<Village>();
			Village village1 = new Village(TestConstants.VILLAGE1_NAME, event1);
			villages.add(village1);
			villages.add(new Village("Village 2", event1));
			villages.add(new Village("Empty village", event1));
			villages.add(new Village("Village on other event", events.get(1)));
			pm.makePersistentAll(villages);
			
			// Organisations
			List<Organisation> organisations = new ArrayList<Organisation>();
			Organisation orgWcf = new Organisation(TestConstants.ORG1_NAME, true);
			organisations.add(orgWcf);
			
			Organisation otherOrg = new Organisation("Unapproved organisation", false);
			organisations.add(otherOrg);
			pm.makePersistentAll(organisations);
			
			// Units
			List<Unit> units = new ArrayList<Unit>();
			Unit unit1 = new Unit(TestConstants.UNIT1_NAME, organisations.get(0), true);
			unit1.setVillageKey(village1.getKeyCheckNotNull());
			
			// FIXME: Note, all units are in all events for now...
			
			//unit1.addEventRegistration(event1);
			//unit1.addEventRegistration(event2);	
			units.add(unit1);
			
			Unit unit2 = new Unit(TestConstants.UNIT2_NAME, organisations.get(0), true);
			
			// FIXME: Note, all units are in all events for now...
			//unit2.addEventRegistration(event1);
			units.add(unit2);
			
			Unit unapprovedWcfUnit = new Unit("Unapproved unit for wcf", organisations.get(0), false);
			units.add(unapprovedWcfUnit);
			Unit otherOrgUnit = new Unit("Unapproved unit", organisations.get(1), false);
			units.add(otherOrgUnit);
			Unit otherOrgUnit2 = new Unit("Approved unit in other org", organisations.get(1), true);
			units.add(otherOrgUnit2);
			pm.makePersistentAll(units);
			
			// Bookings
			List<Booking> bookings = new ArrayList<Booking>();
			
			// Before earlybird deadline
			Booking b = Booking.create("Test person", unit1, event1, testClock);
			b.setEmail("email@example.com");
			bookings.add(b);
			
			bookings.add(Booking.create("Test person 2", unit1, event1,testClock));
			bookings.add(Booking.create("Test person in unit 2", unit2, event1,testClock));
			bookings.add(Booking.create("Second person in unit 2", unit2, event1,testClock));
			
			bookings.add(Booking.create("Person in unapproved, homeless unit", unapprovedWcfUnit, event1, testClock));
			bookings.add(Booking.create("Person in other org", otherOrgUnit2, event1, testClock));
			bookings.add(Booking.create("Test person in other event", unit1, events.get(1), testClock));
			
			// After earlybird deadline
			TestClock afterEarlyBird = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
			bookings.add(Booking.create("Person booked after earlybird", unit1, event1, afterEarlyBird));
			pm.makePersistentAll(bookings);
			
			
			// Transactions
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), testClock.getTime(), TransactionType.Payment, "Payment 1", "Comment 1", 23.32d));
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), testClock.getTime(), TransactionType.Payment, "Payment 2", "", 12.00d));
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), testClock.getTime(), TransactionType.Payment, "Payment 3", "", 130.00d));
			
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), testClock.getTime(), TransactionType.Adjustment, "Refund 1", "Refund test", -23.32d));
			
			transactions.add(new Transaction(unit2.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), testClock.getTime(), TransactionType.Payment, "Unit 2 payment", "", 70.00d));
			
			// after earlybird deadline
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), afterEarlyBird.getTime(), TransactionType.Payment, "Payment after earlybird", "", 10.00d));
			transactions.add(new Transaction(unit1.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), afterEarlyBird.getTime(), TransactionType.Discount, "Discount for early payment", "", 5.00d));
			pm.makePersistentAll(transactions);
			
			// Users
			User user1 = new User(TestConstants.USER_ADMIN_EMAIL, "Global Admin 1", "password", Accesslevel.GLOBAL_ADMIN);
			user1.setOrganisationKey(orgWcf.getKeyCheckNotNull());
			user1.setUnitKey(unit1.getKeyCheckNotNull());
			
			
			User user2 = new User("orgadmin@example.com", "Org Admin 1", "password", Accesslevel.ORG_ADMIN);
			user2.setOrganisationKey(orgWcf.getKeyCheckNotNull());
			user2.setUnitKey(unit1.getKeyCheckNotNull());
			
			User user3 = new User("unitadmin@example.com", "Unit Admin 1", "password", Accesslevel.UNIT_ADMIN);
			user3.setOrganisationKey(orgWcf.getKeyCheckNotNull());
			user3.setUnitKey(unit1.getKeyCheckNotNull());
			
			User user4 = new User("unassigned@example.com", "Unassigned 1", "password", Accesslevel.UNIT_ADMIN);
			user4.setOrganisationKey(orgWcf.getKeyCheckNotNull());
			user4.setUnitKey(unit2.getKeyCheckNotNull());
			user4.setApproved(false);
			
			User user5 = new User("otherorg@example.com", "Other Org 1", "password", Accesslevel.ORG_ADMIN);
			user5.setOrganisationKey(otherOrg.getKeyCheckNotNull());
			user5.setUnitKey(otherOrgUnit2.getKeyCheckNotNull());
			
			pm.makePersistentAll(user1, user2, user3, user4, user5);
			
			
			
			
			
			// Application settings
			AppSetting defaultEventSetting = new AppSetting(AppSetting.DEFAULT_EVENT, event1.getWebKey());
			pm.makePersistent(defaultEventSetting);
			
			//tx.commit();
		} catch(Exception e) {
			//tx.rollback();
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}
	}

}
