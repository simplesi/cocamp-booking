package uk.org.woodcraft.bookings;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uk.org.woodcraft.bookings.auth.SignupAcceptanceTest;
import uk.org.woodcraft.bookings.datamodel.KeyBasedDataTest;
import uk.org.woodcraft.bookings.datamodel.KeyBasedDataWithAuditTest;
import uk.org.woodcraft.bookings.datamodel.UserTest;
import uk.org.woodcraft.bookings.persistence.CannedQueriesTest;
import uk.org.woodcraft.bookings.test.FixtureTest;
import uk.org.woodcraft.bookings.test.LocalDatastoreTest;
import uk.org.woodcraft.bookings.test.TestUtilsTest;

// Add your test class here to have it run
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FixtureTest.class,
        TestUtilsTest.class,
        CannedQueriesTest.class,
        LocalDatastoreTest.class,
        UserTest.class,
        SignupAcceptanceTest.class,
        KeyBasedDataTest.class,
        KeyBasedDataWithAuditTest.class
        })

public class AllTests {

}
