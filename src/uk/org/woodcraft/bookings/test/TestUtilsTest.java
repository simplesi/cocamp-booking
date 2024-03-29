package uk.org.woodcraft.bookings.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.NamedEntity;
import uk.org.woodcraft.bookings.pricing.RegisteredPricingStrategy;

public class TestUtilsTest {

	@Test
	public void testAssertNames() {
		
		// Empty case
		TestUtils.assertNames(new ArrayList<NamedEntity>());
		
		
		List<NamedEntity> events = new ArrayList<NamedEntity>();
		events.add(new Event("test1", null, null, false, RegisteredPricingStrategy.COCAMP));
		events.add(new Event("test2", null, null, false, RegisteredPricingStrategy.COCAMP));
		
		TestUtils.assertNames(events, "test1", "test2");
		
		try
		{
			TestUtils.assertNames(events, "test1");
			fail("Should have assertion error");
		}
		catch(AssertionError failure)
		{}
		
		try
		{
			TestUtils.assertNames(events, "test1", "test3");
			fail("Should have assertion error");
		}
		catch(AssertionError failure)
		{}
		
		try
		{
			TestUtils.assertNames(events, "test1", "test2","test3");
			fail("Should have assertion error");
		}
		catch(AssertionError failure)
		{}
		
	}

}
