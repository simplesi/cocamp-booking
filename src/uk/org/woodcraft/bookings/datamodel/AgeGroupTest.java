package uk.org.woodcraft.bookings.datamodel;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AgeGroupTest {

	@Test
	public void testFindAgeGroup()
	{
		assertEquals(AgeGroup.Woodchip, AgeGroup.groupFor(0));
		assertEquals(AgeGroup.Woodchip, AgeGroup.groupFor(2));
		assertEquals(AgeGroup.Elfin, AgeGroup.groupFor(6));
		assertEquals(AgeGroup.Pioneer, AgeGroup.groupFor(10));
		assertEquals(AgeGroup.Venturer, AgeGroup.groupFor(13));
		assertEquals(AgeGroup.DF, AgeGroup.groupFor(18));
		assertEquals(AgeGroup.Leader, AgeGroup.groupFor(21));
	}
	
	@Test
	public void testToString()
	{
		assertEquals("Unknown", AgeGroup.Unknown.toString());
		assertEquals("Woodchip (0-5)", AgeGroup.Woodchip.toString());
		assertEquals("Leader (21+)", AgeGroup.Leader.toString());
	}
}
