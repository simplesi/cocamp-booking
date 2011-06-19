package uk.org.woodcraft.bookings;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.org.woodcraft.bookings.accounts.BookingsBucket;
import uk.org.woodcraft.bookings.datamodel.AgeGroup;

public class AccountManagerTest {

	@Test
	public void testBookingsBucket()
	{
		BookingsBucket booking1a = new BookingsBucket(AgeGroup.DF, false, 6, 120d);
		BookingsBucket booking1b = new BookingsBucket(AgeGroup.DF, false, 5, 100d);
		assetCompareIsTransitive(booking1a, booking1b);
		
	}
	
	private void assetCompareIsTransitive( BookingsBucket bucketA, BookingsBucket bucketB)
	{
		int compareAB = bucketA.compareTo(bucketB);
		int compareBA = bucketB.compareTo(bucketA);
		
		assertEquals("A reverse comparison should yield the opposite result", compareBA, -compareAB);
	}
}
