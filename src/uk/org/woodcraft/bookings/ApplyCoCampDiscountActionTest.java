package uk.org.woodcraft.bookings;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import uk.org.woodcraft.bookings.ApplyCoCampDiscountsAction.DiscountLine;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.test.TestFixture;
import uk.org.woodcraft.bookings.test.TestUtils;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.TestClock;

import com.google.appengine.api.datastore.Key;

public class ApplyCoCampDiscountActionTest extends BaseFixtureTestCase {

	public ApplyCoCampDiscountActionTest() {
		super(TestFixture.BASIC_DATA);
	}


	@Test
	public void testRelevantBookings()
	{
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		Event event = CannedQueries.eventByName( TestConstants.EVENT1_NAME);
		Map<String, Object> session = new HashMap<String, Object>();
		ApplyCoCampDiscountsAction action = createNewAction(clock, event, session);
		
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		Map<Key, List<Booking>> bookings = action.getRelevantBookings(event);
		
		// Should have filtered out "Person booked after earlybird"
		TestUtils.assertNames(bookings.get(testUnit.getKeyCheckNotNull()), "Test person", "Test person 2");
		
		Unit testUnit2 = CannedQueries.unitByName(TestConstants.UNIT2_NAME, testOrg);
		TestUtils.assertNames(bookings.get(testUnit2.getKeyCheckNotNull()), "Test person in unit 2", "Second person in unit 2");
	}

	@Test
	public void testRelevantTransactions()
	{
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		Event event = CannedQueries.eventByName( TestConstants.EVENT1_NAME);
		Map<String, Object> session = new HashMap<String, Object>();
		ApplyCoCampDiscountsAction action = createNewAction(clock, event, session);
		
		Organisation testOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		Unit testUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, testOrg);
		
		Map<Unit, List<Transaction>> transactions = action.getRelevantTransactions(event);
		
		// Should have filtered out "Person booked after earlybird"
		TestUtils.assertNames(transactions.get(testUnit), "Payment 1", "Payment 2", "Payment 3","Refund 1", "Discount for early payment");
		Unit testUnit2 = CannedQueries.unitByName(TestConstants.UNIT2_NAME, testOrg);
		TestUtils.assertNames(transactions.get(testUnit2), "Unit 2 payment");
	}
	
	@Test
	public void testGenerateDiscounts()
	{
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		Event event = CannedQueries.eventByName( TestConstants.EVENT1_NAME);
		Map<String, Object> session = new HashMap<String, Object>();
		ApplyCoCampDiscountsAction action = createNewAction(clock, event, session);
		
		action.generateDiscounts();
		
		List<DiscountLine> discountlines = action.getDiscountLines();
		
		DiscountLine discount = discountlines.get(0);
		assertEquals(TestConstants.UNIT1_NAME, discount.getUnit().getName());
		assertEquals(10.0d, discount.getDiscount().getAmount(), 0);
		assertEquals(142.0d, discount.getTotalQualifyingPayments(), 0);
		assertEquals(1, discount.getPaidBookings().size());
		assertEquals(1, discount.getUnpaidBookings().size());
		assertEquals("Earlybird booking discount for 1 people", discount.getDiscount().getName());
		assertEquals("142.00 received prior to deadline; There were 1 additional bookings with insufficient funds received to qualify for the discount",
						discount.getDiscount().getComments());
		
		discount = discountlines.get(1);
		assertEquals(TestConstants.UNIT2_NAME, discount.getUnit().getName());
		assertEquals(0.0d, discount.getDiscount().getAmount(), 0);
		assertEquals(70.0d, discount.getTotalQualifyingPayments(), 0);
		assertEquals(0, discount.getPaidBookings().size());
		assertEquals(2, discount.getUnpaidBookings().size());
		assertEquals("Earlybird booking discount for 0 people", discount.getDiscount().getName());
		assertEquals("70.00 received prior to deadline; There were 2 additional bookings with insufficient funds received to qualify for the discount",
				discount.getDiscount().getComments());		
		
		List<Transaction> expectedSessionTransactions = new ArrayList<Transaction>(2);
		expectedSessionTransactions.add(discountlines.get(0).getDiscount());
		expectedSessionTransactions.add(discountlines.get(1).getDiscount());
		
		assertEquals(expectedSessionTransactions, session.get(ApplyCoCampDiscountsAction.SESSION_DISCOUNT_LIST));
	}
	
	@Test
	public void testConfirmDiscounts()
	{		
		TestClock clock = new TestClock(TestConstants.DATE_BEFORE_DEADLINE);
		Event event = CannedQueries.eventByName( TestConstants.EVENT1_NAME);
		Map<String, Object> session = new HashMap<String, Object>();
		ApplyCoCampDiscountsAction action = createNewAction(clock, event, session);
		action.generateDiscounts();
		
		ApplyCoCampDiscountsAction subsequentAction = createNewAction(clock, event, session);
		
		Collection<Transaction> transactions = CannedQueries.transactionsForEvent(event);
		TestUtils.assertNames(transactions, "Payment 1", "Payment 2", "Payment 3","Refund 1", "Discount for early payment", "Unit 2 payment", "Payment after earlybird");
		
		subsequentAction.confirmDiscounts();
		
		transactions = CannedQueries.transactionsForEvent(event);
		TestUtils.assertNames(transactions, "Payment 1", "Payment 2", "Payment 3","Refund 1", "Unit 2 payment", "Payment after earlybird", "Earlybird booking discount for 1 people", "Earlybird booking discount for 0 people");
		
		
	}
	
	private ApplyCoCampDiscountsAction createNewAction(Clock clock, Event event, Map<String, Object> session) {
		ApplyCoCampDiscountsAction action = new ApplyCoCampDiscountsAction();
		
		session.put(SessionConstants.CURRENT_EVENT, event);
		action.setSession(session);
		
		action.setClock(clock);
		
		return action;
	}
	
	
}
