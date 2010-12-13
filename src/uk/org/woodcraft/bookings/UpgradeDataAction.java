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
			// creation date not present on earlier versions of class
			if (booking.getBookingCreationDate() == null) 
				booking.setBookingCreationDate(clock.getTime());
			
			// changed to double version of fee (and renamed from price for backwards compatibility)
			booking.updateFee();
		}
		
		CannedQueries.save(bookings);
		
		addActionMessage("Completed bookings upgrade at "+clock.getTime());
		
	}
}
