package uk.org.woodcraft.bookings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Diet;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class RegistrationReport extends CannedReport {

	private static final Log log = LogFactory.getLog (RegistrationReport.class);
	
	@Override
	protected Collection getRows(CannedReportLabel selectedReport) {
		return CannedQueries.allBookings(true);
	}

	@Override
	protected List<String> getHeaders(CannedReportLabel selectedReport,
			List<Method> reportedMethods) {
		return Arrays.asList("Village","Unit","Name", "Diet", "Arrives", "Departs", "AgeGroup", "18+");
	}

	protected List<Object> renderRow(List<Method> reportedMethods, Object data) {
		List<Object> cells = new ArrayList<Object>();
		
		Booking b = (Booking) data;
		Village v = b.getVillage();
		if (v != null)
			cells.add(v.getName());
		else
			cells.add(null);
		
		cells.add(b.getUnit().getName());
		cells.add(b.getName());
		
		Diet d = b.getDiet();
		if (d != null)
			cells.add(b.getDiet().name());
		else
			cells.add(null);
		
		cells.add(b.getArrivalDate());
		cells.add(b.getDepartureDate());
		cells.add(b.getAgeGroup().name());
		cells.add(b.getOver18().toString());
		
		return cells;
	}

	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		return Arrays.asList(new CannedReportLabel("Registration Report","Registration Report","Report for bookings"));
	}

	@Override
	protected List<Method> getReportedMethods(CannedReportLabel selectedReport) {
		try {
			return Arrays.asList(	Booking.class.getMethod("getVillage"),
									Booking.class.getMethod("getUnit"),
									Booking.class.getMethod("getName"),
									Booking.class.getMethod("getDiet"),
									Booking.class.getMethod("getArrivalDate"),
									Booking.class.getMethod("getDepartureDate"),
									Booking.class.getMethod("getAgeGroup"),
									Booking.class.getMethod("getOver18")
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}