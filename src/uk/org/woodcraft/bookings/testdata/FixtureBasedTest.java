package uk.org.woodcraft.bookings.testdata;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class FixtureBasedTest {
		
	private TestFixture fixture;
	
	public FixtureBasedTest(TestFixture fixture) {
		this.fixture = fixture;
	}
	
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
        fixture.setUp();
    }

    @After
    public void tearDown() {
    	try
    	{
    		fixture.tearDown();
    	}
    	finally {
    		helper.tearDown();
    	}
    }
	
	
}
