package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(detachable="true")
public class Unit extends KeyBasedDataWithContactInfo implements NamedEntity{
	
	private static final long serialVersionUID = 1L;

	public Unit() {
		// For JDO
	}
	
	public Unit(Organisation org)
	{
		this.organisationKey = org.getKeyCheckNotNull();
	}
	
	public Unit(String name, Organisation org, boolean approved) {
		this.name = name;
		this.organisationKey = org.getKeyCheckNotNull();
		this.approved = approved;
	}
	
	@Persistent
	private String name;
	
	@Persistent
	private Key organisationKey;
	
	@Persistent
	private Text notes;
	
	@Persistent
	private boolean approved;
	
	/**
	 * The events that this unit is registered for
	 */
	@Persistent
	private Set<Key> eventsRegistered = new HashSet<Key>();
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public Key getOrganisationKey() {
		return organisationKey;
	}
	
	public void setOrganisationWebKey(String webKey) {
		organisationKey = KeyFactory.stringToKey(webKey);
	}
	
	public String getOrganisationWebKey() {
		if (organisationKey == null) return null;
		return KeyFactory.keyToString(organisationKey);
	}


	public void setOrganisation(Organisation organisation) {
		this.organisationKey = organisation.getKeyCheckNotNull();
	}


	public String getNotesString() {
		if(notes == null) return ""; 
		
		return notes.getValue();
	}


	public void setNotes(Text notes) {
		this.notes = notes;
	}

	public void setNotesString(String notes) {
		this.notes = new Text(notes);
	}

	public boolean isApproved() {
		return approved;
	}


	public void setApproved(boolean approved) {
		this.approved = approved;
	}


	public void setDefaultVillageForEvent(Event event, Village defaultVillage) {
		CannedQueries.persistDefaultVillageKeyForUnit(event, this, defaultVillage);
	}

	// TODO: Use a cache for this
	public Village getDefaultVillageForEvent(Event event) {
		Key key= (CannedQueries.defaultVillageKeyForUnit(event, this));
		
		if (key == null) return null;
		
		return (CannedQueries.villageByKey(key));
	}


	public Collection<Booking> getBookings(Event event)
	{
		return (CannedQueries.bookingsForUnit(this, event));
	}
	
	public String toString() {
		return getName();
	}

    // FIXME: Need to ensure this is called so we know who's going to the event
	public void addEventRegistration(Event event) {
		this.eventsRegistered.add(event.getKeyCheckNotNull());
	}

	public Set<Key> getEventsRegistered() {
		return eventsRegistered;
	}
}
