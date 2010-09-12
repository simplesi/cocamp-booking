package uk.org.woodcraft.bookings.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.EventUnitVillageMapping;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("unchecked")
public class CannedQueries {

	public static List<Event> allEvents(boolean showNonOpenEvents)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Event.class);
		query.setOrdering("name");
		
		if(!showNonOpenEvents) query.setFilter("isCurrentlyOpen == true");
		
		return ((List<Event>)query.execute());
	}
	
	/**
	 * Get event matching a given name
	 * @param eventName The event name
	 * @return The event, or null if nothing matched
	 */
	public static Event eventByName(String eventName)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Event.class);
		query.setFilter("name == nameParam");
		query.declareParameters("String nameParam");
		
		List<Event> events = ((List<Event>)query.execute(eventName));
		if (events.size() == 1) return events.get(0);
		
		return null;
	}
	
	public static List<Village> villagesForEvent(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.setOrdering("name");
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("eventKey == eventKeyParam");
		query.declareParameters("Key eventKeyParam");
		
		return ((List<Village>)query.execute(event.getKeyCheckNotNull()));
	}
	
	/**
	 * Get village for event matching a given name
	 * @param villageName The village name
	 * @param event The event
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByName (String villageName, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("name == nameParam && eventKey == eventKeyParam");
		query.declareParameters("String nameParam, Key eventKeyParam");
		
		List<Village> villages = ((List<Village>)query.execute(villageName, event.getKeyCheckNotNull()));
		if (villages.size() == 1) return villages.get(0);
		
		return null;
	}
	
	/**
	 * Get village for event matching a given name
	 * @param villageKey The village key
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByKey (Key villageKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();	
		return (pm.getObjectById(Village.class, villageKey));
	}
	
	public static List<Organisation> allOrgs(boolean includeUnapproved )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		
		return ((List<Organisation>)query.execute());
	}
	
	/**
	 * Get org matching a given name
	 * @param orgName The org name
	 * @return The org, or null if nothing matched
	 */
	public static Organisation orgByName(String orgName)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setFilter("name == nameParam");
		query.declareParameters("String nameParam");
		
		List<Organisation> orgs = ((List<Organisation>)query.execute(orgName));
		if (orgs.size() == 1) return orgs.get(0);
		
		return null;
	}
	
	
	public static List<Unit> unitsForOrg(Organisation org, boolean includeUnapproved)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("organisationKey == organisationKeyParam");
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		query.declareParameters("Key organisationKeyParam");
		
		return ((List<Unit>)query.execute(org.getKeyCheckNotNull()));
	}
	
	/**
	 * Fetch the units homed in a particular village. 
	 * @param village The village to query for
	 * @return List of units
	 */
	public static List<Unit> unitsForVillage(Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(EventUnitVillageMapping.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		query.setFilter("villageKey == villageKeyParam");
		query.declareParameters("Key villageKeyParam");
		
		List<EventUnitVillageMapping> mappings = (List<EventUnitVillageMapping>) query.execute(village.getKeyCheckNotNull());
		
		Collection<Key> unitKeys = CollectionUtils.collect(mappings, new Transformer() {
			public Object transform(Object input) {
				return ((EventUnitVillageMapping)input).getUnitKey();
			}
		});
			
		query = pm.newQuery(Unit.class, ":keys.contains(key)");
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		List<Unit> units = (List<Unit>) query.execute(unitKeys);
		return units;
	}
	
	/**
	 * Fetch the units homed in a particular village. 
	 * @param village The village to query for
	 * @return List of units
	 */
	public static List<Unit> unitsHomeless(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Get the list of village mappings in place for this event
		Query query = pm.newQuery(EventUnitVillageMapping.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		query.setFilter("eventKey == eventKeyParam");
		query.declareParameters("Key eventKeyParam");
		
		List<EventUnitVillageMapping> mappings = (List<EventUnitVillageMapping>) query.execute(event.getKeyCheckNotNull());
		
		final Collection<Key> unitKeysWithMappings = CollectionUtils.collect(mappings, new Transformer() {
			public Object transform(Object input) {
				return ((EventUnitVillageMapping)input).getUnitKey();
			}
		});
		
		
		// Fetch the set of units registered for this event
		query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		query.setFilter("eventsRegistered == eventsRegisteredParam");
		query.declareParameters("Key eventsRegisteredParam");
		
		List<Unit> unitsAttendingEvent = (List<Unit>)query.execute(event.getKeyCheckNotNull());
		
		Collection<Unit> unitsWithNoVillage = CollectionUtils.select(unitsAttendingEvent, new Predicate() {
			
			public boolean evaluate(Object unit) {
				return (!unitKeysWithMappings.contains(((Unit)unit).getKey()));
			}
		});
		
		return new ArrayList<Unit>(unitsWithNoVillage);
	}
	
	/**
	 * Get unit matching a given name
	 * @param unitName The unit name 
	 * @param org The organization - units in different orgs can have the same name
	 * @return The unit, or null if nothing matched
	 */
	public static Unit unitByName(String unitName, Organisation org)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("name == nameParam && organisationKey == organisationKeyParam");
		query.declareParameters("String nameParam, Key organisationKeyParam");
		
		List<Unit> units = ((List<Unit>)query.execute(unitName, org.getKeyCheckNotNull()));
		if (units.size() == 1) return units.get(0);
		
		return null;
	}
	
	public static List<Booking> bookingsForUnit(Unit unit, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("unitKey == unitKeyParam && eventKey == eventKeyParam");
		query.declareParameters("Key unitKeyParam, Key eventKeyParam");
		
		return ((List<Booking>)query.execute(unit.getKeyCheckNotNull(), event.getKeyCheckNotNull()));
	}
	
	/**
	 * Fetch the bookings for a particular village. 
	 * @param village The village to query for. Cannot be null
	 * @return List of bookings
	 */
	public static List<Booking> bookingsForVillage(Village village)
	{
		if (village == null) throw new IllegalArgumentException("Cannot retrieve booking for a null village. To find homeless bookings, call bookingsHomeless() instead.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("villageKey == villageKeyParam");
		query.declareParameters("Key villageKeyParam");
		
		return ((List<Booking>) query.execute(village.getKeyCheckNotNull()));
	}
	
	/**
	 * Fetch the bookings for a particular village. 
	 * @param village The village to query for. Cannot be null
	 * @return List of bookings
	 */
	public static List<Booking> bookingsHomeless(Event event)
	{
		if (event == null) throw new IllegalArgumentException("Cannot retrieve homeless bookings for a null event.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("eventKey == eventKeyParam && villageKey == null");
		query.declareParameters("Key eventKeyParam");
		
		return ((List<Booking>) query.execute(event.getKeyCheckNotNull()));
	}
	

	public static Key defaultVillageKeyForUnit(Event event, Unit unit)
	{	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Key key = EventUnitVillageMapping.getKeyFor(event, unit);
		EventUnitVillageMapping mapping = null;
		try {
			mapping = pm.getObjectById(EventUnitVillageMapping.class, key);
		}
		catch(JDOObjectNotFoundException exception)
		{
			// If the object didn't exist, that's fine
		}
		
		if (mapping != null) 
			return mapping.getVillageKey();
		else
			return null;
	}
	
	public static void persistDefaultVillageKeyForUnit(Event event, Unit unit, Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		EventUnitVillageMapping mapping = new EventUnitVillageMapping(event.getKeyCheckNotNull(), unit.getKeyCheckNotNull(), village.getKeyCheckNotNull());
		pm.makePersistent(mapping);
	}
}
