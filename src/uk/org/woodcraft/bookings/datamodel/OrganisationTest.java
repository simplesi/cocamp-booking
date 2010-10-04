package uk.org.woodcraft.bookings.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;

import org.junit.Test;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.PMF;
import uk.org.woodcraft.bookings.testdata.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.testdata.TestFixture;

import com.google.appengine.api.datastore.Text;

public class OrganisationTest extends BaseFixtureTestCase{

	public OrganisationTest() {
		super(TestFixture.BASIC_DATA);
	}

	@Test
	public void testSetName() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		ObjectState state0 = JDOHelper.getObjectState(o1);
		
	
		o1.setName("New Name");
		ObjectState state1 = JDOHelper.getObjectState(o1);
		
		Organisation o2 = CannedQueries.save(o1);
		ObjectState state2 = JDOHelper.getObjectState(o2);
		
		
		Organisation o3 = CannedQueries.orgByName("New Name");
		ObjectState state3 = JDOHelper.getObjectState(o3);
		
		Collection<Organisation> allOrgs = CannedQueries.allOrgs(true);
	
	}
	
	@Test
	public void testSetNotes() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		ObjectState state0 = JDOHelper.getObjectState(o1);
		
		Text t1 = new Text("note1");
		
		o1.setNotes(t1);
		ObjectState state1 = JDOHelper.getObjectState(o1);
		
		Organisation o2 = CannedQueries.save(o1);
		ObjectState state2 = JDOHelper.getObjectState(o2);
		
		
		Organisation o3 = CannedQueries.orgByName("Woodcraft Folk");
		ObjectState state3 = JDOHelper.getObjectState(o3);
		
		Collection<Organisation> allOrgs = CannedQueries.allOrgs(true);
		
		assertEquals(t1, o2.getNotes());
		assertEquals(t1, o3.getAddress());
	}
	
	@Test
	public void testSetContactInfo() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		assertNull(o1.getAddress());
		ObjectState state0 = JDOHelper.getObjectState(o1);
		
		o1.setAddress("address 1");
		ObjectState state1 = JDOHelper.getObjectState(o1);
		
		Organisation o2 = CannedQueries.save(o1);
		ObjectState state2 = JDOHelper.getObjectState(o2);
		
		
		Organisation o3 = CannedQueries.orgByName("Woodcraft Folk");
		ObjectState state3 = JDOHelper.getObjectState(o3);
		
		Collection<Organisation> allOrgs = CannedQueries.allOrgs(true);
		
		assertEquals("address 1", o2.getAddress());
		assertEquals("address 1", o3.getAddress());
	}

}
