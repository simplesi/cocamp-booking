package uk.org.woodcraft.bookings.utils;

import static org.junit.Assert.*;

import org.junit.Test;


public class ConfigurationTest {
	@Test
	public void testConfig()
	{
		Configuration config = Configuration.get();
		
		assertEquals("cocamp-local", config.getProperty(Configuration.ENVIRONMENT));
		
		assertEquals("http://localhost:8080/", config.getProperty("baseurl"));
		
	}
}
