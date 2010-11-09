package uk.org.woodcraft.bookings.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;
import javax.jdo.PersistenceManager;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.PMF;

@SuppressWarnings("unchecked")
public class FixtureTest extends BaseFixtureTestCase {

	public FixtureTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testFixtureCreatesData()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Collection<Event> events = (Collection<Event>) pm.newQuery(Event.class).execute();
		events = pm.detachCopyAll(events);
		pm.close();
		
		List<String> expectedEvents = Arrays.asList("Test event 1", "Other event", "Closed event");
		
		List<String> foundEvents = new ArrayList<String>(2);
		
		for(Event e: events)
		{
			foundEvents.add(e.getName());
			assertEquals(ObjectState.DETACHED_CLEAN, JDOHelper.getObjectState(e));
		}
		
		assertEquals( expectedEvents, foundEvents );
	}
}
