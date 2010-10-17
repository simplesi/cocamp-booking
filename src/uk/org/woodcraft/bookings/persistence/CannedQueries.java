package uk.org.woodcraft.bookings.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.EventUnitVillageMapping;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("unchecked")
public class CannedQueries {

	public static Collection<Event> allEvents(boolean showNonOpenEvents)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Event.class);
		query.setOrdering("name");
		
		if(!showNonOpenEvents) query.setFilter("isCurrentlyOpen == true");
		
		return queryDetachAndClose(Event.class, query);
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
		
		return querySingleDetachAndClose(Event.class, query, eventName);
	}
	
	public static Event eventByKey(Key key)
	{
		return getByKey(Event.class, key);
	}
	
	public static Collection<Village> villagesForEvent(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.setOrdering("name");
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("eventKey == eventKeyParam");
		query.declareParameters("Key eventKeyParam");
		
		return queryDetachAndClose(Village.class, query, event.getKeyCheckNotNull());
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
		
		return querySingleDetachAndClose(Village.class, query,  villageName, event.getKeyCheckNotNull());
	}
	
	/**
	 * Get village for event matching a given name
	 * @param villageKey The village key
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByKey (Key villageKey)
	{
		return getByKey(Village.class, villageKey);
	}
	
	public static Collection<Organisation> allOrgs(boolean includeUnapproved )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		return queryDetachAndClose(Organisation.class, query);
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
		
		return querySingleDetachAndClose(Organisation.class, query, orgName);
	}

	public static Organisation orgByKey (Key orgKey)
	{
		return getByKey(Organisation.class, orgKey);
	}
	
	public static Collection<Unit> unitsForOrg(Key orgKey, boolean includeUnapproved)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("organisationKey == organisationKeyParam");
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		query.declareParameters("Key organisationKeyParam");
		return queryDetachAndClose(Unit.class, query, orgKey);
	}
	
	
	public static Collection<Unit> unitsForOrg(Organisation org, boolean includeUnapproved)
	{
		return unitsForOrg(org.getKeyCheckNotNull(), includeUnapproved);
	}
	
	public static Collection<Unit> allUnits(boolean includeUnapproved )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		return queryDetachAndClose(Unit.class, query);
	}
	
	/**
	 * Fetch the units homed in a particular village. 
	 * @param village The village to query for
	 * @return List of units
	 */
	public static Collection<Unit> unitsForVillage(Village village)
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
		
		return queryDetachAndClose(Unit.class, query, unitKeys);
	}
	
	/**
	 * Fetch the units homed in a particular village. 
	 * @param village The village to query for
	 * @return List of units
	 */
	public static Collection<Unit> unitsHomeless(Event event)
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
		
		unitsWithNoVillage = pm.detachCopyAll(unitsWithNoVillage);
		pm.close();
		
		return unitsWithNoVillage;
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
		
		return querySingleDetachAndClose(Unit.class, query, unitName, org.getKeyCheckNotNull());
	}
	
	public static Unit unitByKey(Key key)
	{
		return getByKey(Unit.class, key);
	}
	
	public static Booking bookingByKey(Key key)
	{
		return getByKey(Booking.class, key);
	}
	
	public static Collection<Booking> bookingsForUnit(Unit unit, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("unitKey == unitKeyParam && eventKey == eventKeyParam");
		query.declareParameters("Key unitKeyParam, Key eventKeyParam");
		
		return queryDetachAndClose(Booking.class, query, unit.getKeyCheckNotNull(), event.getKeyCheckNotNull());
	}
	
	public static Collection<Booking> bookingsForOrg(Organisation org, Event event)
	{
		Collection<Unit> unitsInOrg = unitsForOrg(org.getKey(), false);
		
		// FIXME: Batch this?
		if(unitsInOrg.size() > 30)
			throw new RuntimeException("Cannot retrieve bookings for more than 30 units in an organisation");
		
		Collection<Key> unitKeys = new ArrayList<Key>(unitsInOrg.size());
		for(Unit unit : unitsInOrg)
		{
			unitKeys.add(unit.getKey());
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key; import java.util.Collection");
		query.setOrdering("name");
		query.setFilter("unitKeysParam.contains(unitKey) && eventKey == eventKeyParam");
		query.declareParameters("Collection unitKeysParam, Key eventKeyParam");

		
		return queryDetachAndClose(Booking.class, query, unitKeys, event.getKeyCheckNotNull());
	}
	
	/**
	 * Fetch the bookings for a particular village. 
	 * @param village The village to query for. Cannot be null
	 * @return List of bookings
	 */
	public static Collection<Booking> bookingsForVillage(Village village)
	{
		if (village == null) throw new IllegalArgumentException("Cannot retrieve booking for a null village. To find homeless bookings, call bookingsHomeless() instead.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("villageKey == villageKeyParam");
		query.declareParameters("Key villageKeyParam");
		
		return queryDetachAndClose(Booking.class, query, village.getKeyCheckNotNull());
	}
	
	/**
	 * Fetch the bookings for a particular village. 
	 * @param village The village to query for. Cannot be null
	 * @return List of bookings
	 */
	public static Collection<Booking> bookingsHomeless(Event event)
	{
		if (event == null) throw new IllegalArgumentException("Cannot retrieve homeless bookings for a null event.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("eventKey == eventKeyParam && villageKey == null");
		query.declareParameters("Key eventKeyParam");
		
		return queryDetachAndClose(Booking.class, query, event.getKeyCheckNotNull());
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
	    Key defaultVillageKey = null;
		if (mapping != null) 
			defaultVillageKey = mapping.getVillageKey();
		
	    pm.close();
	    return defaultVillageKey;
	}
	
	public static void persistDefaultVillageKeyForUnit(Event event, Unit unit, Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		EventUnitVillageMapping mapping = new EventUnitVillageMapping(event.getKeyCheckNotNull(), unit.getKeyCheckNotNull(), village.getKeyCheckNotNull());
		pm.makePersistent(mapping);
		pm.close();
	}
	
	public static User getUserByEmail(String email)
	{
		return getByKey(User.class, email);
	}
	
	public static Collection<User> allUsers(Organisation org, Unit unit)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(User.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		
		if (org != null) {
			query.setFilter("organisationKey == orgKeyParam");
			query.declareParameters("Key orgKeyParam");
			return queryDetachAndClose(User.class, query, org.getKey());
		}
		
		if (unit != null) {
			query.setFilter("unitKey == unitKeyParam");
			query.declareParameters("Key unitKeyParam");
			return queryDetachAndClose(User.class, query, unit.getKey());
		}
		
		return queryDetachAndClose(User.class, query);
	}

	public static Collection<User> allUsers()
	{
		return allUsers(null, null);
	}
	
	public static Collection<User> allUsersForOrg(Organisation org)
	{
		return allUsers(org, null);
	}
	
	public static Collection<User> allUsersForUnit(Unit unit)
	{
		return allUsers(null, unit);
	}
	
	public static <T> T getByKey(Class<T> clazz, Object key)
	{
		// TODO: Implement caching here
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		T result = null;
		try {
			result = pm.getObjectById(clazz, key);
			result = pm.detachCopy(result);
		}
		catch(JDOObjectNotFoundException exception)
		{
			// Ignore the object not existing
		} finally {
			pm.close();
		}
		return result;
	}
	
	public static <T> T save(T objectToSave)
	{
		System.out.println("Saving object with state: " + JDOHelper.getObjectState(objectToSave));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Transaction tx = pm.currentTransaction();
		try {
			//tx.begin();
			objectToSave = pm.makePersistent(objectToSave);
			//tx.commit();
		} catch (Exception e){
			//if (tx.isActive()) 
				//tx.rollback();
			
			throw new RuntimeException(e);
		}
		finally {
			pm.close();
		}
		return objectToSave;
	}
	
	public static void delete(Object objectToDelete)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.deletePersistent(objectToDelete);
			tx.commit();
		} catch(Exception e) {
			if (tx.isActive()) 
				tx.rollback();
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}
		return;
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @return The detached results
	 */
	private static <T> Collection<T> queryDetachAndClose(Class<T> clazz, Query query)
	{
		return queryDetachAndClose(clazz, query, new Object[]{});
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @param params The query parameter
	 * @return The detached results
	 */
	private static <T> Collection<T> queryDetachAndClose(Class<T> clazz, Query query, Object... params)
	{
		PersistenceManager pm = query.getPersistenceManager();
		Collection<T> results = (Collection<T>) query.executeWithArray(params);
		//pm.refreshAll(results); // hmm
		results = pm.detachCopyAll(results);
		pm.close();
		return results;
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm. Returns null if empty
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @return The detached results
	 */
	private static <T> T querySingleDetachAndClose(Class<T> clazz, Query query, Object... params )
	{
		return querySingleDetachAndClose(clazz, query, true, params);
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @param nullIfEmpty Return null if no matching results, otherwise throw error
	 * @param params The query parameters
	 * @return The detached results
	 */
	private static <T> T querySingleDetachAndClose(Class<T> clazz, Query query, boolean nullIfEmpty, Object... params)
	{
		Collection<T> results = queryDetachAndClose(clazz, query, params);
		
		if (results.size() != 1) 
		{
			if(results.size() == 0 && nullIfEmpty ) return null;
			
			throw new IllegalStateException("Expected result of size 1, was "+results.size());
		}
		return results.iterator().next();
	}
}
