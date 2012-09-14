package uk.org.woodcraft.bookings.pricing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.BasicVCampTestDataFixture;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.TestClock;

public class VenturerCampPricingStrategyTest extends BaseFixtureTestCase {

	
	public VenturerCampPricingStrategyTest() {
		super(TestFixture.BASIC_VCAMP_DATA);
	}

	@Test
	public void testPriceOf() {
		
		VenturerCampPricingStrategy pricer = new VenturerCampPricingStrategy();
		
		Event vcamp = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		TestClock testClock = new TestClock(BasicVCampTestDataFixture.DATE_BEFORE_DEADLINE);
		
		Booking regularBooking = Booking.create(testUnit, vcamp, testClock);
		regularBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		regularBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		assertEquals("Regular bookings are 112", 112d, pricer.priceOf(regularBooking), 0);
		
		Booking bookingWithPreAndPostCamp = Booking.create(testUnit, vcamp, testClock);
		bookingWithPreAndPostCamp.setArrivalDate(DateUtils.getDate(2013, 6, 30));
		bookingWithPreAndPostCamp.setDepartureDate(DateUtils.getDate(2011, 7, 15));
		assertEquals("Bookings capped at 112 including pre and post camp", 112d, pricer.priceOf(bookingWithPreAndPostCamp), 0);
		
		Booking booking = Booking.create(testUnit, vcamp, testClock);
		booking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		booking.setDepartureDate(DateUtils.getDate(2013, 7, 4));
		assertEquals("1 night", 10 + 13d, pricer.priceOf(booking), 0);
		
		booking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		booking.setDepartureDate(DateUtils.getDate(2013, 7, 5));
		assertEquals("2 nights", 10d + (2*13), pricer.priceOf(booking), 0);

		booking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		booking.setDepartureDate(DateUtils.getDate(2013, 7, 10));
		assertEquals("7 nights", 101d, pricer.priceOf(booking), 0);
		
		
		Clock clockAfterDeadline = new TestClock(BasicVCampTestDataFixture.DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);	
		Booking lateBooking = Booking.create(testUnit,vcamp, clockAfterDeadline);
		lateBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		lateBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		assertEquals("Bookings after the deadline cost more", 122, pricer.priceOf(lateBooking), 0);

		Clock clockAfterEventStart = new TestClock(BasicVCampTestDataFixture.VCAMP_START);	
		Booking atEventBooking = Booking.create(testUnit,vcamp, clockAfterEventStart);
		atEventBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		atEventBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		assertEquals("Bookings made at camp cost even more", 142, pricer.priceOf(atEventBooking), 0);

		
		Booking lateUpdatedBooking = Booking.create(testUnit,vcamp, testClock);
		lateUpdatedBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		lateUpdatedBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		lateUpdatedBooking.setBookingUnlockDate(BasicVCampTestDataFixture.DATE_AFTER_AMMENDMENT_DEADLINE);	
		assertEquals("Bookings unlocked for updating after the amendment deadline cost more", 117d, pricer.priceOf(lateUpdatedBooking), 0);
		
		
		
		
		Booking woodchipBooking = Booking.create(testUnit, vcamp, testClock);
		woodchipBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		woodchipBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		woodchipBooking.setDob(DateUtils.getDate(2007, 7, 3)); // Earliest possible date to be 5 at start of camp
		assertEquals("Elfins are not free", 112d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setDob(DateUtils.getDate(2007, 7, 4)); // Latest possible date to be 4 at start of camp
		assertEquals("Woodchips are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		Booking missingDOBBooking = Booking.create(testUnit, vcamp, testClock);
		missingDOBBooking.setDob(null); 
		assertEquals("Those without a DOB are regular price", 112d, pricer.priceOf(missingDOBBooking), 0);
		
		woodchipBooking.setBookingCreationDate(BasicVCampTestDataFixture.DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);
		assertEquals("Late woodchip bookings have an admin fee", 10d, pricer.priceOf(woodchipBooking), 0);	

		woodchipBooking.setBookingCreationDate(BasicVCampTestDataFixture.DATE_AFTER_EVENT_START);
		assertEquals("Woodchip bookings on camp have an admin fee", 30d, pricer.priceOf(woodchipBooking), 0);	
	}
	
	@Test
	public void testCancellation(){
		VenturerCampPricingStrategy pricer = new VenturerCampPricingStrategy();
		
		Event cocamp = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		TestClock testClock = new TestClock(BasicVCampTestDataFixture.DATE_BEFORE_DEADLINE);
		
		Booking regularBooking = Booking.create(testUnit, cocamp, testClock);
		regularBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		regularBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));
		assertEquals("Regular bookings are 112", 112d, pricer.priceOf(regularBooking), 0);
		
		regularBooking.setCancellationDate(BasicVCampTestDataFixture.DATE_BEFORE_DEADLINE);
		assertEquals("Cancelled bookings before the deadline are £0", 0d, pricer.priceOf(regularBooking), 0);

		regularBooking.setCancellationDate(BasicVCampTestDataFixture.DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);
		assertEquals("Cancelled bookings after the deadline are £50", 50d, pricer.priceOf(regularBooking), 0);
		
		regularBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		regularBooking.setDepartureDate(DateUtils.getDate(2013, 7, 4));
		assertEquals("Cancelled bookings which cost less than £50 are capped at their cost if cancelled after the deadline", 10d + 13, pricer.priceOf(regularBooking), 0);
		
		Booking woodchipBooking = Booking.create(testUnit, cocamp, testClock);
		woodchipBooking.setArrivalDate(DateUtils.getDate(2013, 7, 3));
		woodchipBooking.setDepartureDate(DateUtils.getDate(2013, 7, 11));	
		woodchipBooking.setDob(DateUtils.getDate(2007, 7, 4)); // Latest possible date to be 5 at start of camp
		assertEquals("Woodchips are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setCancellationDate(BasicVCampTestDataFixture.DATE_BEFORE_DEADLINE);
		assertEquals("Cancelled woodchip bookings before the deadline are free", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setCancellationDate(BasicVCampTestDataFixture.DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);
		assertEquals("Woodchip bookings made before the deadline and cancelled after the deadline are £0", 0d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setBookingCreationDate(BasicVCampTestDataFixture.DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);
		assertEquals("Woodchip bookings made after the deadline (hence £10) and cancelled after the deadline are £10", 10d, pricer.priceOf(woodchipBooking), 0);
		
		woodchipBooking.setBookingCreationDate(BasicVCampTestDataFixture.DATE_AFTER_EVENT_START);
		assertEquals("Woodchip bookings made after the start of camp (hence £30) and cancelled after the start of camp are £30", 30d, pricer.priceOf(woodchipBooking), 0);
	}
	

}
