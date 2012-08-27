package uk.org.woodcraft.bookings.test;

import org.junit.After;
import org.junit.Before;

import uk.org.woodcraft.bookings.persistence.CacheSupport;

public abstract class BaseFixtureTestCase extends BaseAppEngineTestCase{
		
	private TestFixture fixture;
	
	public BaseFixtureTestCase(TestFixture fixture) {
		this.fixture = fixture;
	}

    @Before
    public void setUp() {
        super.setUp();
        fixture.setUp();
    }

    @After
    public void tearDown() {
    	try
    	{
    		fixture.tearDown();
    	}
    	finally {
    		super.tearDown();
    	}
    }
	
	
}
