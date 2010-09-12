package uk.org.woodcraft.bookings.testdata;

import static org.junit.Assert.assertEquals;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.PMF;
import uk.org.woodcraft.bookings.utils.DateUtils;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class LocalDatastoreTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testPersistEvent() {
    	
    	PersistenceManager pm = PMF.get().getPersistenceManager();
		Event e1 = new Event("Test event", DateUtils.getDate(2011, 1, 1), DateUtils.getDate(2011, 1, 10), true);
		pm.makePersistent(e1);
		
		Event retrieved = (Event) pm.getObjectById(Event.class, e1.getKey() );
		assertEquals(e1, retrieved); 
		
		Extent<Event> extent = pm.getExtent(Event.class, false);
		for(Event e : extent)
		{
			assertEquals("Test event", e.getName());
		}
    }
}
