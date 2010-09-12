package uk.org.woodcraft.bookings.persistence;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.testdata.FixtureBasedTest;
import uk.org.woodcraft.bookings.testdata.TestFixture;
import static org.junit.Assert.*;

public class CannedQueriesTest extends FixtureBasedTest{

	
	
	public CannedQueriesTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testAllEvents() {
		
		List<Event> openEvents = CannedQueries.allEvents(true);
		assertEquals(2, openEvents.size());
	}
	
	@Test
	public void testOpenEvents() {
		
		//FIXME: This fails with some strange error
		List<Event> openEvents = CannedQueries.allEvents(false);
		assertEquals(2, openEvents.size());
	}

	@Test
	public void testVillagesForEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testAllOrgs() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnitsForOrg() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnitsForVillage() {
		fail("Not yet implemented");
	}

	@Test
	public void testBookingsForUnit() {
		fail("Not yet implemented");
	}

	@Test
	public void testBookingsForVillage() {
		fail("Not yet implemented");
	}

}
