package uk.org.woodcraft.bookings.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.collections.MapUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * For each event, holds the default village for each unit
 * @author simon
 *
 */
@PersistenceCapable(detachable="true")
public class EventUnitVillageMapping {
	
	@SuppressWarnings("unused")
	private EventUnitVillageMapping() {
		// For JDO
	}
	
	public EventUnitVillageMapping( Key eventKey, Key unitKey, Key villageKey) {
		
		if( eventKey == null ) throw new IllegalArgumentException("eventKey cannot be null");
		if( unitKey == null ) throw new IllegalArgumentException("unitKey cannot be null");
		if( villageKey == null ) throw new IllegalArgumentException("villageKey cannot be null");
		
		this.eventKey = eventKey;
		this.unitKey = unitKey;
		this.villageKey = villageKey;
		
		this.key = getKeyFor(eventKey, unitKey);	
	}
	
	/** 
	 * Composite key to ensure uniqueness of mapping (event,unit) -> village 
	 */
	@Persistent
	@PrimaryKey
	private Key key;
	
	@Persistent
	private Key eventKey;
	
	@Persistent
	private Key unitKey;
	
	@Persistent
	private Key villageKey;

	public Key getEventKey() {
		return eventKey;
	}

	public void setEventKey(Key eventKey) {
		this.eventKey = eventKey;
	}

	public Key getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(Key unitKey) {
		this.unitKey = unitKey;
	}

	public Key getVillageKey() {
		return villageKey;
	}

	public void setVillageKey(Key villageKey) {
		this.villageKey = villageKey;
	}
	
	public static Key getKeyFor(Event event, Unit unit)
	{
		return (getKeyFor(event.getKeyCheckNotNull(), unit.getKeyCheckNotNull()));
	}
	
	public static Key getKeyFor(Key eventKey, Key unitKey)
	{
		return (KeyFactory.createKey(EventUnitVillageMapping.class.getSimpleName(), 
				KeyFactory.keyToString(eventKey) + "|" + KeyFactory.keyToString(unitKey)));
	}
	
	/**
	 * Get mappings as a mapping from event to village which is unmodifiable since this won't auto-persist
	 * @param mappings
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Key, Key> toEventVillageMap(List<EventUnitVillageMapping> mappings)
	{
		Map<Key, Key> eventVillageMap = new HashMap<Key, Key>(mappings.size());
		for(EventUnitVillageMapping mapping : mappings)
			eventVillageMap.put(mapping.getEventKey(), mapping.getVillageKey());
		
		return(MapUtils.unmodifiableMap(eventVillageMap));
	}

	public Key getKey() {
		return key;
	}
}
