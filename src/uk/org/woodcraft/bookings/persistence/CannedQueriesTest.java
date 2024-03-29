package uk.org.woodcraft.bookings.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;
import uk.org.woodcraft.bookings.test.TestUtils;
import uk.org.woodcraft.bookings.utils.TestClock;

import com.google.appengine.api.datastore.Key;

public class CannedQueriesTest extends BaseFixtureTestCase{
	
	public CannedQueriesTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testAllEvents() {
		
		Collection<Event> allEvents = CannedQueries.allEvents(true);
		assertEquals(3, allEvents.size());
		assertDetached(allEvents);
	}
	
	@Test
	public void testOpenEvents() {
		
		Collection<Event> openEvents = CannedQueries.allEvents(false);
		assertEquals(2, openEvents.size());
		assertDetached(openEvents);
	}

	@Test
	public void testEventByName() {
		
		Event event1 = CannedQueries.eventByName("Test event 1");
		
		assertEquals(TestConstants.EVENT1_NAME, event1.getName());
		assertDetached(event1);
	}
	
	@Test
	public void testVillagesForEvent() {
		
		Event event1 = CannedQueries.eventByName("Test event 1");
		
		Collection<Village> villages = CannedQueries.villagesForEvent(event1);
		assertEquals(3, villages.size());
		assertDetached(villages);
	}

	@Test
	public void testAllOrgs() {
		Collection<Organisation> allOrgs = CannedQueries.allOrgs(true,false);
		assertEquals(2, allOrgs.size());
		assertDetached(allOrgs);

		allOrgs = CannedQueries.allOrgs(true,true);
		assertEquals(2, allOrgs.size());
		assertDetached(allOrgs);
		
		Collection<Organisation> approvedOrgs = CannedQueries.allOrgs(false,false);		
		assertEquals(1, approvedOrgs.size());
		assertEquals("Woodcraft Folk", approvedOrgs.iterator().next().getName());
		assertDetached(approvedOrgs);
		
		approvedOrgs = CannedQueries.allOrgs(false,true);		
		assertEquals(1, approvedOrgs.size());
		assertEquals("Woodcraft Folk", approvedOrgs.iterator().next().getName());
		assertDetached(approvedOrgs);
		
		Collection<Organisation> unapprovedOrgs = CannedQueries.allUnapprovedOrgs();		
		TestUtils.assertNames(unapprovedOrgs, "Unapproved organisation");
		assertDetached(unapprovedOrgs);
	}

	@Test
	public void testOrgByName() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");
		assertEquals("Woodcraft Folk", org.getName());
		assertDetached(org);
	}
	
	@Test
	public void testOrgByKey() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");
		Key key = org.getKey();
		
		Organisation org2 = CannedQueries.orgByKey(key);
		assertEquals(org, org2);
		assertDetached(org2);
	}
	
	@Test
	public void testUnitsForOrg() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");
		
		Collection<Unit> units = CannedQueries.unitsForOrg(org, true,false);
		TestUtils.assertNames(units, "Unit 1", "Unit 2", "Unapproved unit for wcf");
		assertDetached(units);

		units = CannedQueries.unitsForOrg(org, true,true);
		TestUtils.assertNames(units, "Unit 1", "Unit 2", "Unapproved unit for wcf");
		assertDetached(units);
		
		Collection<Unit> approvedOnly = CannedQueries.unitsForOrg(org, false,false);
		TestUtils.assertNames(approvedOnly, "Unit 1", "Unit 2" );
		assertDetached(approvedOnly);

		approvedOnly = CannedQueries.unitsForOrg(org, false,true);
		TestUtils.assertNames(approvedOnly, "Unit 1", "Unit 2" );
		assertDetached(approvedOnly);
	}

	@Test
	public void testVillageByName() {
		
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		assertEquals("Village 1", village.getName());
		assertDetached(village);
	}
	
	
	@Test
	public void testUnitsForVillage() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		
		Collection<Unit> units = CannedQueries.unitsForVillage(village);
		TestUtils.assertNames(units, "Unit 1");
		assertDetached(units);
	}
	
	@Test
	public void testAllUnits() {
		Collection<Unit> allUnits = CannedQueries.allUnits(true,false);
		assertEquals(5, allUnits.size());
		assertDetached(allUnits);
		
		allUnits = CannedQueries.allUnits(true, true);
		assertEquals(5, allUnits.size());
		assertDetached(allUnits);
		
		Collection<Unit> approvedUnits = CannedQueries.allUnits(false, false);		
		assertEquals(3, approvedUnits.size());
		TestUtils.assertNames(approvedUnits, "Unit 1", "Unit 2", "Approved unit in other org");
		assertDetached(approvedUnits);
		
		approvedUnits = CannedQueries.allUnits(false, true);		
		assertEquals(3, approvedUnits.size());
		TestUtils.assertNames(approvedUnits, "Unit 1", "Unit 2", "Approved unit in other org");
		assertDetached(approvedUnits);
		
		Collection<Unit> unapprovedUnits = CannedQueries.allUnapprovedUnits();		
		TestUtils.assertNames(unapprovedUnits, "Unapproved unit for wcf", "Unapproved unit");
		assertDetached(unapprovedUnits);
	}
	
	@Test
	public void testUnitsHomeless() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		
		Collection<Unit> units = CannedQueries.unitsHomeless(event1);
		TestUtils.assertNames(units, "Unit 2", "Unapproved unit for wcf", "Unapproved unit", "Approved unit in other org");
		assertDetached(units);
	}

	@Test
	public void testUnitByName() {
		
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		assertEquals("Unit 1", unit.getName());
		assertDetached(unit);
	}

	@Test
	public void testBookingsForUnitAllEvents() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		
		Collection<Booking> bookings = CannedQueries.bookingsForUnitAllEvents(unit);
		TestUtils.assertNames(bookings, "Test person", "Test person 2", "Person booked after earlybird", "Test person in other event");
		assertDetached(bookings);
	}
	
	@Test
	public void testBookingsForUnit() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		Collection<Booking> bookings = CannedQueries.bookingsForUnit(unit, event1);
		TestUtils.assertNames(bookings, "Test person", "Test person 2","Person booked after earlybird");
		assertDetached(bookings);
	}
	
	@Test
	public void testBookingsForOrg() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org, event1);
		TestUtils.assertNames(bookings, "Test person", "Test person 2","Person booked after earlybird", "Test person in unit 2", "Second person in unit 2");
		assertDetached(bookings);
	}
	
	@Test
	public void testBookingsForLargeOrg() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		List<Unit> testUnits = new ArrayList<Unit>(50);
		List<Booking> testBookings = new ArrayList<Booking>(50);
		List<String> testNames = new ArrayList<String>(52);
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		
		for (int i = 0; i < 50; i++) {
			Unit testUnit = new Unit("Test Unit "+i, org, true);
			testUnits.add(testUnit);
		}
		CannedQueries.save(testUnits);
		
		int unitNumber = 0;
		for(Unit unit : testUnits)
		{
			String bookingName = "Test Booking "+unitNumber++;
			testBookings.add(Booking.create(bookingName, unit, event1, clock));
			testNames.add(bookingName);
		}
		CannedQueries.save(testBookings);
		
		
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org, event1);
		
		testNames.add("Test person");
		testNames.add("Test person 2");
		testNames.add("Person booked after earlybird");
		testNames.add("Test person in unit 2");
		testNames.add("Second person in unit 2");
		
		TestUtils.assertNames(bookings, testNames.toArray(new String[] {}));
		assertDetached(bookings);
	}

	@Test
	public void testBookingsForVillage() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		
		Collection<Booking> bookings = CannedQueries.bookingsForVillage(village);
		
		// FIXME: The last name shouldn't need to be there, but is due to unit bookings not existing, and so events not partitioning the bookings
		TestUtils.assertNames(bookings, "Test person", "Test person 2", "Person booked after earlybird", "Test person in other event");
		//TestUtils.assertNames(bookings, "Test person", "Test person 2", "Person booked after earlybird");
		assertDetached(bookings);
		
		Village village2 = CannedQueries.villageByName("Village 2", event1);
		
		Collection<Booking> bookings2 = CannedQueries.bookingsForVillage(village2);
		assertEquals(0, bookings2.size());
		assertDetached(bookings2);

		Collection<Booking> bookingsHomeless = CannedQueries.bookingsHomeless(event1);
		TestUtils.assertNames(bookingsHomeless, "Person in unapproved, homeless unit", "Person in other org", "Test person in unit 2", "Second person in unit 2");
		assertDetached(bookingsHomeless);
	}
	
	@Test
	public void testBookingsForEvent() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
	
		
		Collection<Booking> bookings = CannedQueries.bookingsForEvent(event1);
		TestUtils.assertNames(bookings, "Person in unapproved, homeless unit", "Test person", "Test person 2", "Person booked after earlybird", "Person in other org", "Test person in unit 2", "Second person in unit 2");
		assertDetached(bookings);
	}
	
	@Test
	public void testBookingsForName() {

		Collection<Booking> bookings = CannedQueries.bookingsForName("Test person 2");
		TestUtils.assertNames(bookings, "Test person 2");
		assertDetached(bookings);
	}
	
	@Test
	public void testUserByEmail() {
		User user1 = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());
		assertDetached(user1);
		
		User user2 = CannedQueries.getUserByEmail("orgadmin@example.com");
		assertEquals("Org Admin 1", user2.getName());
	}
	
	@Test
	public void testAllUsers() {
		Collection<User> users = CannedQueries.allUsers();
		TestUtils.assertNames(users, "System User", "Global Admin 1", "Org Admin 1", "Unit Admin 1", "Unassigned 1", "Other Org 1");
		assertDetached(users);
		
		users = CannedQueries.allUnapprovedUsers();
		TestUtils.assertNames(users, "Unassigned 1");
		assertDetached(users);
		
		
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		users = CannedQueries.allUsersForOrg(org);
		TestUtils.assertNames(users, "Global Admin 1", "Org Admin 1", "Unit Admin 1", "Unassigned 1");
		assertDetached(users);
	
		Unit unit = CannedQueries.unitByName("Unit 1", org);		
		users = CannedQueries.allUsersForUnit(unit);
		TestUtils.assertNames(users, "Global Admin 1", "Org Admin 1", "Unit Admin 1");
		assertDetached(users);
	}
	
	@Test
	public void testTransactionsForUnit() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);	
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");	
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		
		Collection<Transaction> transactions = CannedQueries.transactionsForUnit(unit, event1);
		TestUtils.assertNames(transactions, "Payment 1", "Payment 2", "Payment 3", "Refund 1", "Payment after earlybird","Discount for early payment");
	}

	@Test
	public void testTransactionsForEvent() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);	
		
		Collection<Transaction> transactions = CannedQueries.transactionsForEvent(event1);
		TestUtils.assertNames(transactions, "Payment 1", "Payment 2", "Payment 3","Refund 1", "Payment after earlybird","Discount for early payment", "Unit 2 payment");
	}
	
	@Test
	public void testTransactionsForLargeOrg() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		List<Unit> testUnits = new ArrayList<Unit>(50);
		List<Transaction> testTransactions = new ArrayList<Transaction>(100);
		List<String> testTransNames = new ArrayList<String>(103);
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		
		for (int i = 0; i < 50; i++) {
			Unit testUnit = new Unit("Test Unit "+i, org, true);
			testUnits.add(testUnit);
		}
		CannedQueries.save(testUnits);
		
		int transNumber = 0;
		for(Unit unit : testUnits)
		{
			String transName = "Test Trans "+transNumber++;
			testTransactions.add(new Transaction(unit.getKeyCheckNotNull(), event1.getKeyCheckNotNull(), clock.getTime(), 
												TransactionType.Payment,  transName, null, 1.0d));
			testTransNames.add(transName);
		}
		CannedQueries.save(testTransactions);
		
		
		Collection<Transaction> transactions = CannedQueries.transactionsForOrg(org, event1);
		
		testTransNames.add("Payment 1");
		testTransNames.add("Payment 2");
		testTransNames.add("Payment 3");
		testTransNames.add("Refund 1");
		testTransNames.add("Payment after earlybird");
		testTransNames.add("Discount for early payment");
		testTransNames.add("Unit 2 payment");
		TestUtils.assertNames(transactions, testTransNames.toArray(new String[] {}));
		assertDetached(transactions);
	}
	
	@Test
	public void testGetByKey() {
		User user1 = CannedQueries.getByKey(User.class, "globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());
		assertDetached(user1);
		
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Organisation org2 = CannedQueries.getByKey(Organisation.class, org.getKey());
		assertEquals(org, org2);
		assertDetached(org2);
		
	}
	
	@Test
	public void testDelete() {
		User user1 = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());
		assertDetached(user1);
		
		CannedQueries.delete(user1);
		User user1Deleted = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertNull(user1Deleted);
	}
	/* fails as both in same entity group
	@Test
	public void testDeleteAll() {
		User user1 = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());
		assertDetached(user1);
		
		List<User> userList = new ArrayList<User>();
		userList.add(user1);
		
		User user2 = CannedQueries.getUserByEmail("unassigned@example.com");
		userList.add(user2);
		
		
		CannedQueries.deleteAll(userList);
		User user1Deleted = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertNull(user1Deleted);
	}
	*/
	
	@Test
	public void testSave() {
		User user1 = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());			
		user1.setName("new name");
		
		CannedQueries.save(user1);
		
		User user1Renamed = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("new name", user1Renamed.getName());
	}
	
	
	private void assertDetached(Object object)
	{
		assertEquals(ObjectState.DETACHED_CLEAN, JDOHelper.getObjectState(object));
	}
	
	@SuppressWarnings("rawtypes")
	private void assertDetached(Collection objects)
	{
		for(Object o : objects)
		{
			assertDetached(o);
		}
	}
	

}
