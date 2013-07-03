package uk.org.woodcraft.bookings.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class UnitVillageMappingAction extends SessionBasedAction{

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UnitVillageMappingAction.class.getName());
	
	private Map<String, String> map = new HashMap<String,String>();
	private Collection<Booking> problemBookings = new ArrayList<Booking>();

	
	public Map<String, String> getMap() {
		return map;
	}
	
	public void setMap(Map<String, String> map) {
		this.map=map;
	}
	
	private void populateMappings() 
	{
		map.clear();
		for(Unit unit : CannedQueries.allUnits(false, false))
		{
			map.put(new String(Base64.encodeBase64(unit.getWebKey().getBytes())), unit.getVillageWebKey());
		}
	}
	
	public Collection<Unit> getUnits()
	{
		return CannedQueries.allUnits(false, false);
	}
	
	public String list()
	{
		SecurityModel.checkIsAdminUser(this);
		populateMappings();
		
		return SUCCESS;
	}
	
	public String applyMappings()
	{
		SecurityModel.checkIsAdminUser(this);
		
		int unitsChanged = 0;
		int bookingsChanged = 0;
		
		problemBookings = new ArrayList<Booking>();
		
		log.fine("Map of unit mappings to change " + map.toString() );
		
		for(Unit unit : getUnits())
		{

			String proposedNewVillageKey = map.get(new String(Base64.encodeBase64(unit.getWebKey().getBytes())));
			if (proposedNewVillageKey == null || proposedNewVillageKey.equals("")) 
			{
				log.fine(String.format("Skipping %s (%s) as proposedKey was %s", unit.getName(), new String(Base64.encodeBase64(unit.getWebKey().getBytes())), proposedNewVillageKey) );
				continue;
			}
			
			Key newKey = KeyFactory.stringToKey(proposedNewVillageKey);
			
			if (unit.getVillageKey() == null || !unit.getVillageKey().equals(newKey))
			{			
				Collection<Booking> bookings = CannedQueries.bookingsForUnit(unit, getCurrentEvent());
				Collection<Booking> bookingsToUpdate = new ArrayList<Booking>();
				
				for(Booking booking : bookings)
				{
					// Don't touch those where the village key was not the old village key for the unit 
					// (ie that had already been altered manually)
					if (booking.getVillageKey() == null 
						|| booking.getVillageKey().equals(unit.getVillageKey()))
					{
						booking.setVillageKey(newKey);
						bookingsToUpdate.add(booking);
						
					} else {
						getProblemBookings().add(booking);
					}
				}
				
				unit.setVillageKey(newKey);
				
				CannedQueries.save(bookingsToUpdate);
				CannedQueries.save(unit);
				
				// Ensure session has the right unit mapping too
				SessionUtils.syncSessionCacheIfRequired(getSession(), SessionConstants.CURRENT_UNIT, unit);
				
				unitsChanged++;
				bookingsChanged += bookingsToUpdate.size();
			}
			else
			{
				log.fine(String.format("Skipping %s as existing key was %s and new key %s", unit.getName(), unit.getVillageKey(), newKey.toString()) );
			}
		}
		
		// pick up new mappings
		populateMappings();
		
		addActionMessage(String.format("Updated %d units with new village information, and updated %d bookings as a result", unitsChanged, bookingsChanged));
		return SUCCESS;
	}


	public Collection<Booking> getProblemBookings() {
		return problemBookings;
	}

	public Collection<Village> getVillages() {
		Collection<Village> villages = new ArrayList<Village>();
		Village noVillage = new Village("No village assigned", getCurrentEvent());
		villages.add(noVillage);
		villages.addAll(CannedQueries.villagesForEvent(getCurrentEvent()));
		return villages;
	}

	
}
