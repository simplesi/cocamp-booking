package uk.org.woodcraft.bookings.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.org.woodcraft.bookings.CannedReportDynamicMethods;
import uk.org.woodcraft.bookings.CannedReportLabel;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class BookedUnitsReport extends CannedReportDynamicMethods {

	@Override
	protected List<Unit> getRows(CannedReportLabel selectedReport) {
		// FIXME: This should be doing something event-specific, but event-specific unit bookings not sorted yet
		return new ArrayList<Unit>(CannedQueries.allUnits(true));
	}

	@Override
	protected Class<Unit> getDataType() {
		return Unit.class;
	}


	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		return Collections.singletonList(new CannedReportLabel("BookedUnitsReport", "All Booked Units", "All units booked into the event"));
	}

}
