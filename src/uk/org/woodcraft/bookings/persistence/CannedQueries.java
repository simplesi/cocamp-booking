package uk.org.woodcraft.bookings.persistence;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;

public class CannedQueries {

	public static List<Event> allEvents(boolean showNonOpenEvents)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");
		
		if(!showNonOpenEvents) query.setFilter("iscurrentlyopen == true");
		
		return ((List<Event>)query.execute());
	}
	
	public static List<Village> villagesForEvent(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.setOrdering("name");
		query.declareParameters("Key eventKey");
		
		return ((List<Village>)query.execute(event.getKeyCheckNotNull()));
	}
	
	public static List<Organisation> allOrgs(boolean includeUnapproved )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		
		return ((List<Organisation>)query.execute());
	}
	
	
	public static List<Unit> unitsForOrg(Organisation org, boolean includeUnapproved)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		query.declareParameters("Key organisationKey");
		
		return ((List<Unit>)query.execute(org.getKeyCheckNotNull()));
	}
	
	/**
	 * Fetch the approved units homed in a particular village. 
	 * @param village The village to query for. Leave null to get homeless units
	 * @return List of units
	 */
	public static List<Unit> unitsForVillage(Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.setOrdering("name");
		
		query.setFilter("approved == true");
		
		query.declareParameters("Key defaultVillageKey");
		
		List<Unit> results = null;
		
		if(village != null) 
		{
			query.execute(village.getKeyCheckNotNull());
		} else { 
			query.execute(null);
		};
		return (results);
	}
	
	public static List<Booking> bookingsForUnit(Unit unit, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.setOrdering("name");
		query.declareParameters("Key unitKey");
		query.declareParameters("Key eventKey");
		
		return ((List<Booking>)query.execute(unit.getKeyCheckNotNull(), event.getKeyCheckNotNull()));
	}
	
	/**
	 * Fetch the bookings for a particular village. 
	 * @param village The village to query for. Leave null to get homeless bookings
	 * @return List of bookings
	 */
	public static List<Booking> bookingsForVillage(Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.setOrdering("name");
		
		query.declareParameters("Key villageKey");
		
		List<Booking> results = null;
		
		if(village != null) 
		{
			query.execute(village.getKeyCheckNotNull());
		} else { 
			query.execute(null);
		};
		return (results);
	}
	
}
