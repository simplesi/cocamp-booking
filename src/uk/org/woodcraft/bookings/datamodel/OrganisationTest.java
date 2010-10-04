package uk.org.woodcraft.bookings.datamodel;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.testdata.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.testdata.TestFixture;

public class OrganisationTest extends BaseFixtureTestCase{

	public OrganisationTest() {
		super(TestFixture.BASIC_DATA);
	}

	@Test
	public void testSetContactInfo() {
		Organisation o1 = CannedQueries.orgByName("Woodcraft Folk");
		assertNull(o1.getAddress());
		
		o1.setAddress("address 1");
		CannedQueries.save(o1);
		
		Organisation otherO1 = CannedQueries.orgByName("Woodcraft Folk");
		assertEquals("address 1", otherO1.getAddress());
	}

}
