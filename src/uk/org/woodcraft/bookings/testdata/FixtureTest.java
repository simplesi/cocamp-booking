package uk.org.woodcraft.bookings.testdata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.PMF;

import static org.junit.Assert.*;

@SuppressWarnings("unchecked")
public class FixtureTest extends FixtureBasedTest {

	public FixtureTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testFixtureCreatesData()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Collection<Event> events = (Collection<Event>) pm.newQuery(Event.class).execute();
		List<String> expectedEvents = Arrays.asList("Test event 1", "Other event");
		
		List<String> foundEvents = new ArrayList<String>(2);
		
		for(Event e: events)
		{
			foundEvents.add(e.getName());
		}
		
		assertEquals( expectedEvents, foundEvents );
	}
}
