package uk.org.woodcraft.bookings.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testAgeOnDay() 
	{
		// Months are 0-indexed, thanks sun.
		Date eventDay = DateUtils.getDate(2010, 0, 1);
		
		assertEquals(10, DateUtils.ageOnDay(DateUtils.getDate(1999, 11, 31), eventDay));
		assertEquals(10, DateUtils.ageOnDay(DateUtils.getDate(2000, 0, 1), eventDay));
		assertEquals(9, DateUtils.ageOnDay(DateUtils.getDate(2000, 0, 2), eventDay));
		
		// test the oldies
		assertEquals(100, DateUtils.ageOnDay(DateUtils.getDate(1909, 11, 31), eventDay));
		assertEquals(100, DateUtils.ageOnDay(DateUtils.getDate(1910, 0, 1), eventDay));
		assertEquals(99, DateUtils.ageOnDay(DateUtils.getDate(1910, 0, 2), eventDay));
		
		eventDay = DateUtils.getDate(2010, 5, 1);
		
		// Mid-year dates
		assertEquals(11, DateUtils.ageOnDay(DateUtils.getDate(1999, 4, 31), eventDay));
		assertEquals(11, DateUtils.ageOnDay(DateUtils.getDate(1999, 5, 1), eventDay));
		assertEquals(10, DateUtils.ageOnDay(DateUtils.getDate(1999, 5, 2), eventDay));
		
		// Mid-year dates with a leap year. Oh yeah!
		assertEquals(10, DateUtils.ageOnDay(DateUtils.getDate(2000, 4, 31), eventDay));
		assertEquals(10, DateUtils.ageOnDay(DateUtils.getDate(2000, 5, 1), eventDay));
		assertEquals(9, DateUtils.ageOnDay(DateUtils.getDate(2000, 5, 2), eventDay));
		
		
		// check we throw for people who haven't been born yet...
		try { 
			@SuppressWarnings("unused")
			int age = DateUtils.ageOnDay(DateUtils.getDate(2011, 4, 31), eventDay);
			fail("should have thrown exception if someone hasn't been born yet");
		} catch(IllegalArgumentException e)
		{
			System.err.println(e);
		}
	}
	
}
