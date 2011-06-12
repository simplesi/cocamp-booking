package uk.org.woodcraft.bookings;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uk.org.woodcraft.bookings.auth.SignupAcceptanceTest;
import uk.org.woodcraft.bookings.datamodel.AgeGroupTest;
import uk.org.woodcraft.bookings.datamodel.KeyBasedDataTest;
import uk.org.woodcraft.bookings.datamodel.KeyBasedDataWithAuditTest;
import uk.org.woodcraft.bookings.datamodel.UserTest;
import uk.org.woodcraft.bookings.persistence.CannedQueriesTest;
import uk.org.woodcraft.bookings.pricing.CoCampPricingStrategyTest;
import uk.org.woodcraft.bookings.test.FixtureTest;
import uk.org.woodcraft.bookings.test.LocalDatastoreTest;
import uk.org.woodcraft.bookings.test.TestUtilsTest;
import uk.org.woodcraft.bookings.utils.ConfigurationTest;
import uk.org.woodcraft.bookings.utils.DateUtilsTest;

// Add your test class here to have it run
@RunWith(Suite.class)
@Suite.SuiteClasses({
        FixtureTest.class,
        ConfigurationTest.class,
        TestUtilsTest.class,
        DateUtilsTest.class,
        CannedQueriesTest.class,
        LocalDatastoreTest.class,
        UserTest.class,
        SignupAcceptanceTest.class,
        KeyBasedDataTest.class,
        KeyBasedDataWithAuditTest.class,
        CoCampPricingStrategyTest.class,
     //   UpgradeDataActionTest.class,
        AgeGroupTest.class,
        ApplyCoCampDiscountActionTest.class,
        BookedUnitsReportTest.class, 
        AccountsActionTest.class
        })

public class AllTests {

}
