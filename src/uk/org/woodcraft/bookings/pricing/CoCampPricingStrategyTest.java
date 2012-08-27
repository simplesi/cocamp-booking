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
		
		Booking regularBooking = Booking.create(testUnit, cocamp, testClock);
		regularBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		regularBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		assertEquals("Regular bookings are 150", 150d, pricer.priceOf(regularBooking), 0);
		
		Booking bookingWithPreAndPostCamp = Booking.create(testUnit, cocamp, testClock);
		bookingWithPreAndPostCamp.setArrivalDate(DateUtils.getDate(2011, 6, 20));
		bookingWithPreAndPostCamp.setDepartureDate(DateUtils.getDate(2011, 7, 15));
		assertEquals("Bookings capped at 150 including pre and post camp", 150d, pricer.priceOf(bookingWithPreAndPostCamp), 0);
		
		Booking booking = Booking.create(testUnit, cocamp, testClock);
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
		Booking lateBooking = Booking.create(testUnit,cocamp, clockAfterDeadline);
		lateBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		lateBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		
		assertEquals("Bookings after the deadline cost more", 175d, pricer.priceOf(lateBooking), 0);

		Booking lateUpdatedBooking = Booking.create(testUnit,cocamp, testClock);
		lateUpdatedBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		lateUpdatedBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		lateUpdatedBooking.setBookingUnlockDate(TestConstants.DATE_AFTER_DEADLINE);
		
		assertEquals("Bookings unlocked for updating after the deadline cost more", 175d, pricer.priceOf(lateUpdatedBooking), 0);
		
		Booking woodchipBooking = Booking.create(testUnit, cocamp, testClock);
		woodchipBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		woodchipBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		woodchipBooking.setDob(DateUtils.getDate(2005, 6, 30)); // Latest possible date to be 5 at start of camp
		assertEquals("Elfins are not free", 150d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setDob(DateUtils.getDate(2005, 6, 31)); // Latest possible date to be 5 at start of camp
		assertEquals("Woodchips are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		Booking missingDOBBooking = Booking.create(testUnit, cocamp, testClock);
		missingDOBBooking.setDob(null); 
		assertEquals("Those without a DOB are regular price", 150d, pricer.priceOf(missingDOBBooking), 0);
		
		woodchipBooking.setBookingCreationDate(TestConstants.DATE_AFTER_DEADLINE);
		assertEquals("Late woodchip bookings have an admin fee", 25d, pricer.priceOf(woodchipBooking), 0);	
	}
	
	@Test
	public void testCancellation(){
		CoCampPricingStrategy pricer = new CoCampPricingStrategy();
		
		Event cocamp = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		TestClock testClock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		
		Booking regularBooking = Booking.create(testUnit, cocamp, testClock);
		regularBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		regularBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));
		assertEquals("Regular bookings are 150", 150d, pricer.priceOf(regularBooking), 0);
		
		regularBooking.setCancellationDate(TestConstants.DATE_BEFORE_DEADLINE);
		assertEquals("Cancelled bookings before the deadline are £25", 25d, pricer.priceOf(regularBooking), 0);

		regularBooking.setCancellationDate(TestConstants.DATE_AFTER_DEADLINE);
		assertEquals("Cancelled bookings after the deadline are £75", 75d, pricer.priceOf(regularBooking), 0);
		
		regularBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		regularBooking.setDepartureDate(DateUtils.getDate(2011, 7, 1));
		assertEquals("Cancelled bookings which cost less than £75 are capped at their cost if cancelled after the deadline are £75", 65d, pricer.priceOf(regularBooking), 0);
		
		Booking woodchipBooking = Booking.create(testUnit, cocamp, testClock);
		woodchipBooking.setArrivalDate(DateUtils.getDate(2011, 6, 30));
		woodchipBooking.setDepartureDate(DateUtils.getDate(2011, 7, 9));	
		woodchipBooking.setDob(DateUtils.getDate(2005, 6, 31)); // Latest possible date to be 5 at start of camp
		assertEquals("Woodchips are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setCancellationDate(TestConstants.DATE_BEFORE_DEADLINE);
		assertEquals("Cancelled woodchip bookings before the deadline are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setCancellationDate(TestConstants.DATE_AFTER_DEADLINE);
		assertEquals("Woodchip bookings made before the deadline and cancelled after the deadline are £0", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setBookingCreationDate(TestConstants.DATE_AFTER_DEADLINE);
		assertEquals("Woodchip bookings made after the deadline (hence £25) and cancelled after the deadline are £25", 25d, pricer.priceOf(woodchipBooking), 0);
	}
	

}
