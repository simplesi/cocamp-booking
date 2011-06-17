package uk.org.woodcraft.bookings.datamodel;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UnitVillageMapping
{ 
	private Unit unit;
	private Key villageKey;
	
	public UnitVillageMapping(Unit unit, Key villageKey) {
		this.unit = unit;
		this.villageKey = villageKey;
	}

	public Unit getUnit() {
		return unit;
	}
	
	public String getUnitName() {
		return unit.getName();
	}
	
	public void setUnitWebKey(String unitWebKey)
	{
		if (unit.getKey() != KeyFactory.stringToKey(unitWebKey))
			throw new IllegalStateException(String.format("Unable to match unit keys when allocating mappings - was %s, expecting %s", 
					unitWebKey, KeyFactory.keyToString(unit.getKey())));
	}
	
	public String getUnitWebKey()
	{
		return unit.getWebKey();
	}
	
	public void setVillageKey(Key villageKey) {
		this.villageKey = villageKey;
	}
	public void setVillageWebKey(String villageWebKey) {
		this.villageKey = KeyFactory.stringToKey(villageWebKey);			
	}
	public String getVillageWebKey() {
		if (villageKey == null) return null;
		return KeyFactory.keyToString(getVillageKey());
	}
	public Key getVillageKey() {
		return villageKey;
	}
}
