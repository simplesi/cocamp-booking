package uk.org.woodcraft.bookings.pricing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.TestClock;

public class CoCampPricingStrategyTest extends BaseFixtureTestCase {

	public CoCampPricingStrategyTest() {
		super(TestFixture.BASIC_DATA);
	}

	@Test
	public void testPriceOf() {
		
		CoCampPricingStrategy pricer = new CoCampPricingStrategy();
		
		Event cocamp = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		TestClock testClock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		
		Booking regularBooking = new Booking(testUnit, cocamp, testClock);
		regularBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		regularBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		assertEquals("Regular bookings are 150", 150d, pricer.priceOf(regularBooking), 0);
		
		Booking bookingWithPreAndPostCamp = new Booking(testUnit, cocamp, testClock);
		bookingWithPreAndPostCamp.setArrivalDate(DateUtils.getDate(2011, 6, 20));
		bookingWithPreAndPostCamp.setDepartureDate(DateUtils.getDate(2011, 7, 15));
		assertEquals("Bookings capped at 150 including pre and post camp", 150d, pricer.priceOf(bookingWithPreAndPostCamp), 0);
		
		Booking booking = new Booking(testUnit, cocamp, testClock);
		booking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		booking.setDepartureDate(DateUtils.getDate(2011, 6, 31));
		assertEquals("1 night", 35 + 15d, pricer.priceOf(booking), 0);
		
		booking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		booking.setDepartureDate(DateUtils.getDate(2011, 7, 1));
		assertEquals("2 nights", 35 + 30d, pricer.priceOf(booking), 0);

		booking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		booking.setDepartureDate(DateUtils.getDate(2011, 7, 8));
		assertEquals("9 nights", 150d, pricer.priceOf(booking), 0);
		
		
		Clock clockAfterDeadline = new TestClock(TestConstants.DATE_AFTER_DEADLINE);	
		Booking lateBooking = new Booking(testUnit,cocamp, clockAfterDeadline);
		lateBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		lateBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		
		assertEquals("Bookings after the deadline cost more", 175d, pricer.priceOf(lateBooking), 0);
		
		
		Booking woodchipBooking = new Booking(testUnit, cocamp, testClock);
		woodchipBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		woodchipBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		woodchipBooking.setDob(DateUtils.getDate(2005, 6, 30)); // Latest possible date to be 5 at start of camp
		assertEquals("Elfins are not free", 150d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setDob(DateUtils.getDate(2005, 6, 31)); // Latest possible date to be 5 at start of camp
		assertEquals("Woodchips are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setBookingCreationDate(TestConstants.DATE_AFTER_DEADLINE);
		assertEquals("Late woodchip bookings have an admin fee", 25d, pricer.priceOf(woodchipBooking), 0);
		
	}

}
