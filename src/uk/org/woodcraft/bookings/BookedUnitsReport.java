package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.List;

import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class BookedUnitsReport extends CannedReport<Unit> {

	
	
	@Override
	protected List<Unit> getRows() {
		// FIXME: This should be doing something event-specific, but event-specific unit bookings not sorted yet
		return new ArrayList<Unit>(CannedQueries.allUnits(true));
	}

	@Override
	protected Class<Unit> getDataType() {
		return Unit.class;
	}

}
