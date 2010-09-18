package uk.org.woodcraft.bookings.persistence;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.testdata.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.testdata.TestConstants;
import uk.org.woodcraft.bookings.testdata.TestFixture;
import uk.org.woodcraft.bookings.testdata.TestUtils;

import com.google.appengine.api.datastore.Key;

public class CannedQueriesTest extends BaseFixtureTestCase{
	
	public CannedQueriesTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testAllEvents() {
		
		List<Event> allEvents = CannedQueries.allEvents(true);
		assertEquals(3, allEvents.size());
	}
	
	@Test
	public void testOpenEvents() {
		
		List<Event> openEvents = CannedQueries.allEvents(false);
		assertEquals(2, openEvents.size());
	}

	@Test
	public void testEventByName() {
		
		Event event1 = CannedQueries.eventByName("Test event 1");
		
		assertEquals(TestConstants.EVENT1_NAME, event1.getName());
	}
	
	@Test
	public void testVillagesForEvent() {
		
		Event event1 = CannedQueries.eventByName("Test event 1");
		
		List<Village> villages = CannedQueries.villagesForEvent(event1);
		assertEquals(3, villages.size());
	}

	@Test
	public void testAllOrgs() {
		List<Organisation> allOrgs = CannedQueries.allOrgs(true);
		assertEquals(2, allOrgs.size());
		
		List<Organisation> approvedOrgs = CannedQueries.allOrgs(false);		
		assertEquals(1, approvedOrgs.size());
		assertEquals("Woodcraft Folk", approvedOrgs.get(0).getName());
	}

	public void testOrgByName() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");
		assertEquals("Woodcraft Folk", org.getName());
	}
	
	@Test
	public void testUnitsForOrg() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");
		
		List<Unit> units = CannedQueries.unitsForOrg(org, true);
		TestUtils.assertNames(units, "Unit 1", "Unit 2", "Unapproved unit for wcf");

		List<Unit> approvedOnly = CannedQueries.unitsForOrg(org, false);
		TestUtils.assertNames(approvedOnly, "Unit 1", "Unit 2" );
	}

	@Test
	public void testVillageByName() {
		
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		assertEquals("Village 1", village.getName());
	}
	
	
	@Test
	public void testUnitsForVillage() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		
		List<Unit> units = CannedQueries.unitsForVillage(village);
		TestUtils.assertNames(units, "Unit 1");
	}
	
	@Test
	public void testUnitsHomeless() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		
		List<Unit> units = CannedQueries.unitsHomeless(event1);
		TestUtils.assertNames(units, "Unit 2");
	}

	@Test
	public void testUnitByName() {
		
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		assertEquals("Unit 1", unit.getName());
	}
	
	@Test
	public void testBookingsForUnit() {
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit = CannedQueries.unitByName("Unit 1", org);
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		List<Booking> bookings = CannedQueries.bookingsForUnit(unit, event1);
		TestUtils.assertNames(bookings, "Test person", "Test person 2");
	}

	@Test
	public void testBookingsForVillage() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Village village = CannedQueries.villageByName("Village 1", event1);
		
		List<Booking> bookings = CannedQueries.bookingsForVillage(village);
		TestUtils.assertNames(bookings, "Test person", "Test person 2");
		
		Village village2 = CannedQueries.villageByName("Village 2", event1);
		
		List<Booking> bookings2 = CannedQueries.bookingsForVillage(village2);
		assertEquals(0, bookings2.size());

		List<Booking> bookingsHomeless = CannedQueries.bookingsHomeless(event1);
		TestUtils.assertNames(bookingsHomeless, "Person in unapproved, homeless unit");
	}
	
	@Test
	public void testDefaultVillagesForUnit() {
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);		
		Organisation org = CannedQueries.orgByName("Woodcraft Folk");		
		Unit unit1 = CannedQueries.unitByName("Unit 1", org);
		Village expectedVillage = CannedQueries.villageByName("Village 1", event1);
		
		Key villageKey = CannedQueries.defaultVillageKeyForUnit(event1, unit1);
		assertEquals(expectedVillage.getKey(), villageKey);
		
		Unit unit2 = CannedQueries.unitByName("Unit 2", org);
		assertEquals(null, CannedQueries.defaultVillageKeyForUnit(event1, unit2));
		
		Event event2 = CannedQueries.eventByName("Other event");
		assertEquals(null, CannedQueries.defaultVillageKeyForUnit(event2, unit1));
	}
	
	@Test
	public void testUserByEmail() {
		User user1 = CannedQueries.getUserByEmail("globaladmin@example.com");
		assertEquals("Global Admin 1", user1.getName());
		
		User user2 = CannedQueries.getUserByEmail("orgadmin@example.com");
		assertEquals("Org Admin 1", user2.getName());
	}

}
