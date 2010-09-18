package uk.org.woodcraft.bookings;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uk.org.woodcraft.bookings.auth.SignupAcceptanceTest;
import uk.org.woodcraft.bookings.datamodel.UserTest;
import uk.org.woodcraft.bookings.persistence.CannedQueriesTest;
import uk.org.woodcraft.bookings.testdata.FixtureTest;
import uk.org.woodcraft.bookings.testdata.LocalDatastoreTest;
import uk.org.woodcraft.bookings.testdata.TestUtilsTest;

// Add your test class here to have it run
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FixtureTest.class,
        TestUtilsTest.class,
        CannedQueriesTest.class,
        LocalDatastoreTest.class,
        UserTest.class,
        SignupAcceptanceTest.class
        })

public class AllTests {

}
