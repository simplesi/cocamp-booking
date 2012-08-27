package uk.org.woodcraft.bookings.test;

import org.junit.After;
import org.junit.Before;

import uk.org.woodcraft.bookings.persistence.CacheSupport;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public abstract class BaseAppEngineTestCase {
	private final LocalServiceTestHelper dataStoreHelper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
//	private final LocalServiceTestHelper memcacheHelper =
//	        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
	
    @Before
    public void setUp() {
    	dataStoreHelper.setUp();
//    	memcacheHelper.setUp();
    }

    @After
    public void tearDown() {
//    	memcacheHelper.tearDown();
    	dataStoreHelper.tearDown();
    	CacheSupport.flush();
    }
}
