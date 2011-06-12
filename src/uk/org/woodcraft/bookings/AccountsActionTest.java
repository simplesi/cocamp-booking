package uk.org.woodcraft.bookings;

import org.junit.Test;

import uk.org.woodcraft.bookings.AccountsAction.BookingsBucket;
import uk.org.woodcraft.bookings.datamodel.AgeGroup;
import static org.junit.Assert.*;

public class AccountsActionTest {

	@Test
	public void testBookingsBucket()
	{
		AccountsAction action = new AccountsAction();
		
		BookingsBucket booking1a = action.new BookingsBucket(AgeGroup.DF, false, 6, 120d);
		BookingsBucket booking1b = action.new BookingsBucket(AgeGroup.DF, false, 5, 100d);
		assetCompareIsTransitive(booking1a, booking1b);
		
	}
	
	private void assetCompareIsTransitive( BookingsBucket bucketA, BookingsBucket bucketB)
	{
		int compareAB = bucketA.compareTo(bucketB);
		int compareBA = bucketB.compareTo(bucketA);
		
		assertEquals("A reverse comparison should yield the opposite result", compareBA, -compareAB);
	}
}
