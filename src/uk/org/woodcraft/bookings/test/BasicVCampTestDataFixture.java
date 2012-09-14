package uk.org.woodcraft.bookings.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.pricing.RegisteredPricingStrategy;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.TestClock;

public class BasicVCampTestDataFixture extends BasicCoCampTestDataFixture {

	public static final Date VCAMP_START = DateUtils.getDate(2013, 7, 3);
	public static final Date VCAMP_END = DateUtils.getDate(2013, 7, 11);
	
	public static final Date DATE_BEFORE_EARLY_DEADLINE = DateUtils.getDate(2012, 10, 10);
	public static final Date DATE_BEFORE_DEADLINE = DateUtils.getDate(2013, 2, 10);
	public static final Date DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT = DateUtils.getDate(2013, 6, 8);
	public static final Date DATE_AFTER_AMMENDMENT_DEADLINE = DateUtils.getDate(2013, 6, 22);
	public static final Date DATE_AFTER_EVENT_START = DateUtils.getDate(2013, 7, 3);	
	
	@Override
	protected Event getTestEvent() {
		Event event1 = new Event(TestConstants.EVENT1_NAME, VCAMP_START, VCAMP_END, true, RegisteredPricingStrategy.VENTURER_CAMP);
		event1.setEarlyBookingDeadline(DateUtils.getDate(2012, 11, 10));
		event1.setBookingDeadline(DateUtils.getDate(2013, 6, 5));
		event1.setBookingAmendmentDeadline(DateUtils.getDate(2013, 6, 21)); // For VCamp, the amendment deadline is different to the booking deadline
		event1.setBookingSystemLocked(DateUtils.getDate(2011, 9, 3)); // Irrelevant
		
		return event1;
	}

	@Override
	protected List<Booking> getBookings(Event event1, Event event2,	Unit unit1, Unit unit2, Unit unapprovedWcfUnit, Unit otherOrgUnit2) {
		
		List<Booking> bookings = new ArrayList<Booking>();
		TestClock testClock = new TestClock(DATE_BEFORE_EARLY_DEADLINE);
		
		// Before earlybird deadline
		Booking b = Booking.create("Test person", unit1, event1, testClock);
		b.setEmail("email@example.com");
		bookings.add(b);
		
		bookings.add(Booking.create("Test person 2", unit1, event1,testClock));
		bookings.add(Booking.create("Test person in unit 2", unit2, event1,testClock));
		bookings.add(Booking.create("Second person in unit 2", unit2, event1,testClock));
		
		bookings.add(Booking.create("Person in unapproved, homeless unit", unapprovedWcfUnit, event1, testClock));
		bookings.add(Booking.create("Person in other org", otherOrgUnit2, event1, testClock));
		bookings.add(Booking.create("Test person in other event", unit1, event2, testClock));
		
		// After earlybird deadline
		TestClock afterEarlyBird = new TestClock(DATE_BEFORE_DEADLINE);		
		bookings.add(Booking.create("Person booked after earlybird", unit1, event1, afterEarlyBird));
		
		// After booking deadline
		TestClock afterBookingDeadline = new TestClock(DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT);
		bookings.add(Booking.create("Person booked after booking deadline", unit1, event1, afterBookingDeadline));
		
		// After event start
		TestClock afterEventStart = new TestClock(DATE_AFTER_EVENT_START);
		bookings.add(Booking.create("Person booked after booking deadline", unit1, event1, afterEventStart));		
		return bookings;
	}
}
