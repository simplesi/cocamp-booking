package uk.org.woodcraft.bookings;

import java.util.Arrays;
import java.util.Collection;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.pricing.RegisteredPricingStrategy;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class BookingDataProvider {

	public Collection<Event> getAllOpenEvents()
	{
		Collection<Event> events = CannedQueries.allEvents(false);
		return events;
	}
	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false, false);
		return orgs;
	}

	public Collection<Unit> getMyUnits()
	{
		// FIXME - Inject session for this
		Collection<Unit> units = CannedQueries.unitsForOrg(SessionUtils.getCurrentOrg(), false, false);
		return units;
	}
	
	public Collection<Organisation> getAllOrgsCached()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false, true);
		return orgs;
	}

	public Collection<Unit> getMyUnitsCached()
	{
		// FIXME - Inject session for this
		Collection<Unit> units = CannedQueries.unitsForOrg(SessionUtils.getCurrentOrg(), false, true);
		return units;
	}
	
	public Collection<Accesslevel> getAccessLevels()
	{
		return Arrays.asList(Accesslevel.values());
	}
	
	public Collection<RegisteredPricingStrategy> getPricingStrategies()
	{
		return Arrays.asList(RegisteredPricingStrategy.values());
	}
	
}
