package uk.org.woodcraft.bookings;
import java.util.Collection;

import com.opensymphony.xwork2.ActionSupport;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.SystemClock;


public class UpgradeDataAction extends ActionSupport {

	private static final long serialVersionUID = 1091391392623793887L;


	public String upgradeData()
	{
		SecurityModel.checkIsAdminUser();
		
		upgradeData(new SystemClock());
		return SUCCESS;
	}
	
	public void upgradeData(Clock clock)
	{		
		upgradeBookings(clock);
	}
	
	
	public void upgradeBookings(Clock clock)
	{
		addActionMessage("Starting bookings upgrade at "+clock.getTime());
		Collection<Booking> bookings = CannedQueries.allBookings();
		
		for(Booking booking : bookings)
		{
			// BookingUnlockDate wasn't previously updating the fee
			if (booking.getBookingUnlockDate() != null) 
			{
				booking.updateFee();
				CannedQueries.save(booking);
			}
		}
		
		//CannedQueries.save(bookings);
		
		addActionMessage("Completed bookings upgrade at "+clock.getTime());
		
	}
}
