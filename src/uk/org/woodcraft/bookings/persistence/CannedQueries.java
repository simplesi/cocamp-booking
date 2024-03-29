package uk.org.woodcraft.bookings.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.commons.collections.CollectionUtils;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.KeyBasedData;
import uk.org.woodcraft.bookings.datamodel.Keyed;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@SuppressWarnings("unchecked")
public class CannedQueries {

 @SuppressWarnings("unused")
private static final Logger log = Logger.getLogger(CannedQueries.class.getName());

	public static Collection<Event> allEvents(boolean showNonOpenEvents)
	{	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Event.class);
		query.setOrdering("name");
		
		if(!showNonOpenEvents) query.setFilter("isCurrentlyOpen == true");
		
		return queryDetachAndClose(Event.class, query, true);
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
		
		return querySingleDetachAndClose(Event.class, query, false, eventName);
	}
	
	/**
	 * Get event matching a given name
	 * @param eventName The event name
	 * @return The event, or null if nothing matched
	 */
	public static Event eventByName(String eventName, Key ignoreKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Event.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		if (ignoreKey == null)
		{
			query.setFilter("name == nameParam");
			query.declareParameters("String nameParam");
			return querySingleDetachAndClose(Event.class, query, false, eventName);
		} else {
			query.setFilter("name == nameParam && key != ignoreKeyParam");
			query.declareParameters("String nameParam, Key ignoreKeyParam");
			return querySingleDetachAndClose(Event.class, query, false, eventName, ignoreKey);
		}
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
		
		return queryDetachAndClose(Village.class, query, true, event.getKeyCheckNotNull());
	}
	
	
	/**
	 * Get village for event matching a given name
	 * @param villageName The village name
	 * @param event The event
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByName(String villageName, Event event)
	{
		return villageByName(villageName, event.getKeyCheckNotNull());
	}
	
	/**
	 * Get village for event matching a given name
	 * @param villageName The village name
	 * @param event The event
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByName(String villageName, Key eventKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setFilter("name == nameParam && eventKey == eventKeyParam");
		query.declareParameters("String nameParam, Key eventKeyParam");
		
		return querySingleDetachAndClose(Village.class, query, false, villageName, eventKey);
	}
	
	/**
	 * Get village for event matching a given name
	 * @param villageName The village name
	 * @param event The event
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByName(String villageName, Key eventKey, Key ignoreKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Village.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		if(ignoreKey == null)
		{
			query.setFilter("name == nameParam && eventKey == eventKeyParam");
			query.declareParameters("String nameParam, Key eventKeyParam");
			return querySingleDetachAndClose(Village.class, query, false, villageName, eventKey);
		} else {
			query.setFilter("name == nameParam && eventKey == eventKeyParam && key != ignoreKeyParam");
			query.declareParameters("String nameParam, Key eventKeyParam, Key ignoreKeyParam");
			return querySingleDetachAndClose(Village.class, query, false, villageName, eventKey, ignoreKey);
		}
		
	}
	
	/**
	 * Get village for event matching a given key
	 * @param villageKey The village key
	 * @return The village, or null if nothing matched
	 */
	public static Village villageByKey (Key villageKey)
	{
		return getByKey(Village.class, villageKey);
	}
	
	public static Collection<Organisation> allOrgs(boolean includeUnapproved, boolean cache )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		return queryDetachAndClose(Organisation.class, query, cache);
	}
	
	public static Collection<Organisation> allUnapprovedOrgs()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.setOrdering("name");	
		query.setFilter("approved == false");
		
		return queryDetachAndClose(Organisation.class, query, false);
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
		
		return querySingleDetachAndClose(Organisation.class, query, false, orgName);
	}

	/**
	 * Get org matching a given name
	 * @param orgName The org name
	 * @param ignoreKey The key to ignore in the result set
	 * @return The org, or null if nothing matched
	 */
	public static Organisation orgByName(String orgName, Key ignoreKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Organisation.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		if (ignoreKey == null)
		{
			query.setFilter("name == nameParam");
			query.declareParameters("String nameParam");
			return querySingleDetachAndClose(Organisation.class, query, false, orgName);			
		} else {	
			query.setFilter("name == nameParam && key != ignoreKeyParam");
			query.declareParameters("String nameParam, Key ignoreKeyParam");
			return querySingleDetachAndClose(Organisation.class, query, false, orgName, ignoreKey);
		}
	}
	
	public static Organisation orgByKey (Key orgKey)
	{
		return getByKey(Organisation.class, orgKey);
	}
	
	public static Collection<Unit> unitsForOrg(Key orgKey, boolean includeUnapproved, boolean cache)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		if(includeUnapproved) 
			query.setFilter("organisationKey == organisationKeyParam");
		else
			query.setFilter("organisationKey == organisationKeyParam && approved == true");
		
		query.setOrdering("name");
		
		
		
		query.declareParameters("Key organisationKeyParam");
		return queryDetachAndClose(Unit.class, query, cache, orgKey);
	}
	
	
	public static Collection<Unit> unitsForOrg(Organisation org, boolean includeUnapproved, boolean cache)
	{
		return unitsForOrg(org.getKeyCheckNotNull(), includeUnapproved, cache);
	}
	
	public static Collection<Unit> allUnits(boolean includeUnapproved, boolean cache )
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.setOrdering("name");
		
		if(!includeUnapproved) query.setFilter("approved == true");
		
		return queryDetachAndClose(Unit.class, query, cache);
	}
	
	public static Collection<Unit> allUnapprovedUnits()
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.setOrdering("name");
		query.setFilter("approved == false");
		
		return queryDetachAndClose(Unit.class, query, false);
	}
	
	/**
	 * Fetch the units homed in a particular village. 
	 * @param village The village to query for
	 * @return List of units
	 */
	public static Collection<Unit> unitsForVillage(Village village)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		query.setFilter("villageKey == villageKeyParam");
		query.declareParameters("Key villageKeyParam");
		
		return queryDetachAndClose(Unit.class, query, false, village.getKeyCheckNotNull());
	}
	
	/**
	 * Fetch the units that have no home
	 * @return List of units
	 */
	public static Collection<Unit> unitsHomeless(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		// Get the list of village mappings in place for this event
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		query.setFilter("villageKey == null");
		
		return queryDetachAndClose(Unit.class, query, false);
	}

	/**
	 * Get unit matching a given name
	 * @param unitName The unit name 
	 * @param orgKey The organization key - units in different orgs can have the same name
	 * @return The unit, or null if nothing matched
	 */
	public static Unit unitByName(String unitName, Key orgKey)
	{
		return unitByName(unitName, orgKey, null);
	}
	
	public static Unit unitByName(String unitName, Key orgKey, Key ignoreKey)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Unit.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		
		if (ignoreKey == null)
		{
			query.setFilter("name == nameParam && organisationKey == organisationKeyParam");
			query.declareParameters("String nameParam, Key organisationKeyParam");
			return querySingleDetachAndClose(Unit.class, query, false, unitName, orgKey);
		} else 
		{
			query.setFilter("name == nameParam && organisationKey == organisationKeyParam && key != ignoreKeyParam");
			query.declareParameters("String nameParam, Key organisationKeyParam, Key ignoreKeyParam");
			return querySingleDetachAndClose(Unit.class, query, false, unitName, orgKey, ignoreKey);
		}
		
	}
	
	/**
	 * Get unit matching a given name
	 * @param unitName The unit name 
	 * @param org The organization - units in different orgs can have the same name
	 * @return The unit, or null if nothing matched
	 */
	public static Unit unitByName(String unitName, Organisation org)
	{
		return unitByName(unitName, org.getKeyCheckNotNull());
	}
	
	public static Unit unitByKey(Key key)
	{
		return getByKey(Unit.class, key);
	}
	
	public static Booking bookingByKey(Key key)
	{
		return getByKey(Booking.class, key);
	}
	
	public static Collection<Booking> allBookings()
	{
		return allBookings(false);
	}
	
	public static Collection<Booking> allBookings(boolean validOnly)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);	
		if (validOnly)
			query.setFilter("cancellationDate == null");
		return queryDetachAndClose(Booking.class, query, true);
	}
	
	public static Collection<Booking> bookingsForUnit(Unit unit, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("unitKey == unitKeyParam && eventKey == eventKeyParam");
		query.declareParameters("Key unitKeyParam, Key eventKeyParam");
		
		return queryDetachAndClose(Booking.class, query, false, unit.getKeyCheckNotNull(), event.getKeyCheckNotNull());
	}
	
	public static Collection<Booking> bookingsForUnitAllEvents(Unit unit)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("unitKey == unitKeyParam");
		query.declareParameters("Key unitKeyParam");
		
		return queryDetachAndClose(Booking.class, query, false, unit.getKeyCheckNotNull());
	}
	
	public static Collection<Booking> bookingsForOrg(Organisation org, Event event)
	{
		Collection<Key> keysForOrg = CollectionUtils.collect(unitsForOrg(org.getKey(), false, true), new KeyBasedData.ToKey());
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key; import java.util.Collection");
		query.setOrdering("name");
		query.setFilter("unitKeysParam.contains(unitKey) && eventKey == eventKeyParam");
		query.declareParameters("Collection unitKeysParam, Key eventKeyParam");
		
		return batchQuery(query, keysForOrg, event.getKeyCheckNotNull());
	}
	
	private static <T extends Comparable<T>> Collection<T> batchQuery(Query query, Collection<Key> batchedParam, Object... otherArgs)
	{
		List<T> result = new ArrayList<T>();		
		Iterator<Key> iterator = batchedParam.iterator();
		
		while (iterator.hasNext())
		{
			List<Key> keyBatch = new ArrayList<Key>(30);
			while(iterator.hasNext() && keyBatch.size() < 30)
			{
				keyBatch.add(iterator.next());
			}
			
			List<Object> params = new ArrayList<Object>(1 + otherArgs.length);
			params.add(keyBatch);
			params.addAll(Arrays.asList(otherArgs));
				
			Collection<T> resultsForBatch = (Collection<T>) query.executeWithArray(params.toArray());
			result.addAll(query.getPersistenceManager().detachCopyAll(resultsForBatch));
		}
		
		Collections.sort(result);
		
		return result;
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
		
		return queryDetachAndClose(Booking.class, query, false, village.getKeyCheckNotNull());
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
		
		return queryDetachAndClose(Booking.class, query, false, event.getKeyCheckNotNull());
	}
	
	public static Collection<Booking> bookingsForEvent(Event event)
	{
		if (event == null) throw new IllegalArgumentException("Cannot retrieve bookings for a null event.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("eventKey == eventKeyParam");
		query.declareParameters("Key eventKeyParam");
		
		return queryDetachAndClose(Booking.class, query, true, event.getKeyCheckNotNull());
	}
	
	public static Collection<Booking> bookingsForEventAndEmail(Key eventKey, Email email)
	{
		if (eventKey == null) throw new IllegalArgumentException("Cannot retrieve bookings for an empty event.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key; import com.google.appengine.api.datastore.Email");
		query.setOrdering("name");
		query.setFilter("eventKey == eventKeyParam && email == emailParam");
		query.declareParameters("Key eventKeyParam, Email emailParam");
		
		return queryDetachAndClose(Booking.class, query, false, eventKey,email);
	}
	
	/* NB - App engine doesn't support partial like matching for strings, so this is equality only...
	 * 
	 */
	public static Collection<Booking> bookingsForName(String name)
	{
		if (name == null) throw new IllegalArgumentException("Cannot retrieve bookings for an empty name.");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(Booking.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
		query.setFilter("name == nameParam");
		query.declareParameters("Key nameParam");
		
		return queryDetachAndClose(Booking.class, query, false, name);
	}
	
	public static uk.org.woodcraft.bookings.datamodel.Transaction TransactionByKey(Key key)
	{
		return getByKey(uk.org.woodcraft.bookings.datamodel.Transaction.class, key);
	}
	
	public static Collection<uk.org.woodcraft.bookings.datamodel.Transaction> transactionsForUnit(Unit unit, Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(uk.org.woodcraft.bookings.datamodel.Transaction.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("timestamp");
		query.setFilter("unitKey == unitKeyParam && eventKey == eventKeyParam");
		query.declareParameters("Key unitKeyParam, Key eventKeyParam");
		
		return queryDetachAndClose(uk.org.woodcraft.bookings.datamodel.Transaction.class, query, false, unit.getKeyCheckNotNull(), event.getKeyCheckNotNull());
	}
	
	public static Collection<uk.org.woodcraft.bookings.datamodel.Transaction> transactionsForOrg(Organisation org, Event event)
	{
		Collection<Key> keysForOrg = CollectionUtils.collect(unitsForOrg(org.getKey(), false, false), new KeyBasedData.ToKey());
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(uk.org.woodcraft.bookings.datamodel.Transaction.class);
		query.declareImports("import com.google.appengine.api.datastore.Key; import java.util.Collection");
		query.setOrdering("timestamp");
		query.setFilter("unitKeysParam.contains(unitKey) && eventKey == eventKeyParam");
		query.declareParameters("Collection unitKeysParam, Key eventKeyParam");
		
		return batchQuery(query, keysForOrg, event.getKeyCheckNotNull());
	}
	
	public static Collection<uk.org.woodcraft.bookings.datamodel.Transaction> transactionsForEvent(Event event)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(uk.org.woodcraft.bookings.datamodel.Transaction.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("timestamp");
		query.setFilter("eventKey == eventKeyParam");
		query.declareParameters("Key eventKeyParam");
		
		return queryDetachAndClose(uk.org.woodcraft.bookings.datamodel.Transaction.class, query, false, event.getKeyCheckNotNull());
	}

	
	public static User getUserByEmail(String email)
	{
		return getByKey(User.class, email);
	}
	
	private static Collection<User> allUsers(Organisation org, Unit unit, boolean unapprovedOnly)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(User.class);
		query.declareImports("import com.google.appengine.api.datastore.Key");
		query.setOrdering("name");
			
		if (org != null) {
			query.setFilter("organisationKey == orgKeyParam");
			query.declareParameters("Key orgKeyParam");
			return queryDetachAndClose(User.class, query, false, org.getKey());
		}
		
		if (unit != null) {
			query.setFilter("unitKey == unitKeyParam");
			query.declareParameters("Key unitKeyParam");
			return queryDetachAndClose(User.class, query, false, unit.getKey());
		}
		
		if(unapprovedOnly) 
			query.setFilter("approved == false");
		return queryDetachAndClose(User.class, query, false);
	}

	public static Collection<User> allUsers()
	{
		return allUsers(null, null, false);
	}
	
	public static Collection<User> allUnapprovedUsers()
	{
		return allUsers(null, null, true);
	}
	
	public static Collection<User> allUsersForOrg(Organisation org)
	{
		return allUsers(org, null, false);
	}
	
	public static Collection<User> allUsersForUnit(Unit unit)
	{
		return allUsers(null, unit, false);
	}
	
	public static <T> Collection<T> allEntriesForClass(Class<T> clazz)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(clazz);
		return queryDetachAndClose(clazz, query, false);
	}
	
	
	public static <T> T getByKey(Class<T> clazz, Object key)
	{
		// TODO: Implement caching here
		T result = null;
		result = (T) CacheSupport.cacheGet(key);
		if (result != null) return result;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

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
		
		if (result instanceof Serializable)
			CacheSupport.cachePut(key, (Serializable) result);
		
		return result;
	}
	
	public static <T> T save(T objectToSave)
	{
		return save(Arrays.asList(objectToSave)).iterator().next();
	}
	
	public static <T> Collection<T> save(Collection<T> objectsToSave)
	{
//		System.out.println("Saving object with state: " + JDOHelper.getObjectState(objectToSave));
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		//Transaction tx = pm.currentTransaction();
		try {
			//tx.begin();
			objectsToSave = pm.makePersistentAll(objectsToSave);
			//tx.commit();
		} catch (Exception e){
			//if (tx.isActive()) 
				//tx.rollback();
			
			throw new RuntimeException(e);
		}
		finally {
			pm.close();
		}
		
		// Update in cache
		for(T object : objectsToSave)
		{
			try {
				if (object instanceof Keyed)
					CacheSupport.cachePut(((Keyed<T>)object).getKey(), (Keyed<T>)object);
				} 
			catch(Exception e)
			{};
		}
		
		return objectsToSave;
	}
	
	public static void delete(Object objectToDelete)
	{
		@SuppressWarnings("rawtypes")
		Collection list = new ArrayList();
		list.add(objectToDelete);
		deleteAll(list);
	}
	
	@SuppressWarnings("rawtypes")
	public static void deleteAll(Collection objectsToDelete)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.deletePersistentAll(objectsToDelete);
			tx.commit();
			
			for(Object obj: objectsToDelete)
			{
				try{
				if (obj instanceof Keyed)
					CacheSupport.cacheDelete(((Keyed)obj).getKey());
				} catch(Exception e)
				{}
			}
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
	private static <T> Collection<T> queryDetachAndClose(Class<T> clazz, Query query, boolean cache)
	{
		return queryDetachAndClose(clazz, query, cache, new Object[]{} );
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @param params The query parameter
	 * @return The detached results
	 */
	private static <T> Collection<T> queryDetachAndClose(Class<T> clazz, Query query, boolean cache, Object... params)
	{
		String cacheKey = null;
		Collection<T> results;
		if (cache)
		{
			if (params.length != 0)
			{
				// Can't rely on toString of params, since it frequently uses hashcode
				cache = false;
			}
			else 
			{
				cacheKey = clazz.toString() + query.toString();
				results = (Collection<T>) CacheSupport.cacheGet(cacheKey);
				if (results != null) return results;
			}
		}
		
		PersistenceManager pm = query.getPersistenceManager();
		results = (Collection<T>) query.executeWithArray(params);
		//pm.refreshAll(results); // hmm
		results = pm.detachCopyAll(results);
		pm.close();
		
		if (cache)
		{
			CacheSupport.cachePutExp(cacheKey, (Serializable) results, 600);
		}
		return results;
	}
	
	/***
	 * Helper to run a query, detach its results and close the pm. Returns null if empty
	 * @param <T>
	 * @param clazz The type of results
	 * @param query The query
	 * @return The detached results
	 */
	private static <T> T querySingleDetachAndClose(Class<T> clazz, Query query, boolean cache, Object... params )
	{
		return querySingleDetachAndClose(clazz, query, true, cache, params);
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
	private static <T> T querySingleDetachAndClose(Class<T> clazz, Query query, boolean nullIfEmpty, boolean cache, Object... params)
	{
		Collection<T> results = queryDetachAndClose(clazz, query, cache, params);
		
		if (results.size() != 1) 
		{
			if(results.size() == 0 && nullIfEmpty ) return null;
			
			throw new IllegalStateException("Expected result of size 1, was "+results.size());
		}
		return results.iterator().next();
	}
}
