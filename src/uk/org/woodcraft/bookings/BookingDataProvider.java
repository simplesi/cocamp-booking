package uk.org.woodcraft.bookings;

import java.util.Collection;

import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class BookingDataProvider {

	public Collection<Event> getAllOpenEvents()
	{
		Collection<Event> events = CannedQueries.allEvents(false);
		return events;
	}
	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		return orgs;
	}

	public Collection<Unit> getMyUnits()
	{
		// FIXME - Inject session for this
		Collection<Unit> units = CannedQueries.unitsForOrg(SessionUtils.getCurrentOrg(), false);
		return units;
	}
	
}
