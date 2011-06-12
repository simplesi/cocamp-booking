package uk.org.woodcraft.bookings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;
import uk.org.woodcraft.bookings.utils.TestClock;

public class UpgradeDataActionTest extends BaseFixtureTestCase {

	public UpgradeDataActionTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	//@Test
	/*Upgrade below has since been decomissioned. If we upgrade more data, this can be used
	 * public void testUpgradeBookings() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		// Make some dummy data bad
		
		Collection<Booking> bookings = new ArrayList<Booking>();
		
		TestClock testClock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		Event testEvent = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg.getKey());	
		
		Booking bookingWithNoFee = new Booking("NoFeePerson", testUnit, testEvent, testClock);
		bookingWithNoFee.setArrivalDate(TestConstants.EVENT1_START);
		bookingWithNoFee.setDepartureDate(TestConstants.EVENT1_END);
		
		bookingWithNoFee.setBookingCreationDate(null);
		
		// Use reflection to null out the fee

		Field feeField = Booking.class.getDeclaredField("fee");
		feeField.setAccessible(true);
		feeField.set(bookingWithNoFee, null);
		bookings.add(bookingWithNoFee);
		
		CannedQueries.save(bookings);

		
		// Confirm it's broken
		bookings = CannedQueries.bookingsForName("NoFeePerson");
		Booking noFeePerson = bookings.iterator().next();
		assertEquals(null, noFeePerson.getBookingCreationDate());
		assertTrue(noFeePerson.getFee() == 0.0);
		
		// Do the upgrade
		UpgradeDataAction upgrade = new UpgradeDataAction();
		testClock.add(Calendar.DAY_OF_YEAR, 1);
		upgrade.upgradeData(testClock);
		
		
		// Check the results
		bookings = CannedQueries.bookingsForName("NoFeePerson");
		noFeePerson = bookings.iterator().next();
		assertEquals(noFeePerson.getBookingCreationDate(), testClock.getTime());
		assertTrue(noFeePerson.getFee() > 0);
	
	}
	*/
}
