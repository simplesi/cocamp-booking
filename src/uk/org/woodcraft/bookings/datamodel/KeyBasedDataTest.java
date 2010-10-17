package uk.org.woodcraft.bookings.datamodel;

import static org.junit.Assert.assertEquals;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;

import org.junit.Test;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;

public class KeyBasedDataTest extends BaseFixtureTestCase{

	public KeyBasedDataTest() {
		super(TestFixture.BASIC_DATA);
	}

	@Test
	public void testSetNameForKeyBased() {
		Event e1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		
		e1.setName("New Name");
		assertEquals(ObjectState.DETACHED_DIRTY,JDOHelper.getObjectState(e1) );
	
		Event e2 = CannedQueries.save(e1);
		assertEquals(ObjectState.DETACHED_CLEAN,JDOHelper.getObjectState(e2) );
	
		Event e3 = CannedQueries.eventByName("New Name");
		assertEquals("New Name", e3.getName());
	}
	
	@Test
	public void testSetNameForContactData() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		
		o1.setName("New Name");
		assertEquals(ObjectState.DETACHED_DIRTY,JDOHelper.getObjectState(o1) );
	
		Organisation o2 = CannedQueries.save(o1);
		assertEquals(ObjectState.DETACHED_CLEAN,JDOHelper.getObjectState(o2) );
	
		Organisation o3 = CannedQueries.orgByName("New Name");
		assertEquals("New Name", o3.getName());
	}
	
	@Test
	public void testSetContactsForContactData() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		
		o1.setPhone("New number");
		assertEquals(ObjectState.DETACHED_DIRTY,JDOHelper.getObjectState(o1) );
	
		Organisation o2 = CannedQueries.save(o1);
		assertEquals(ObjectState.DETACHED_CLEAN,JDOHelper.getObjectState(o2) );
	
		Organisation o3 = CannedQueries.orgByName("Woodcraft Folk");
		assertEquals("New number", o3.getPhone());
	}

}
