package uk.org.woodcraft.bookings.datamodel;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Unit extends KeyBasedData implements NamedEntity{
	
	public Unit(String name, Organisation org, boolean approved) {
		this.name = name;
		this.organisationKey = org.getKeyCheckNotNull();
		this.approved = approved;
	}
	
	@Persistent
	private String name;
	
	@Persistent
	private Key organisationKey;
	
	@Persistent(dependent = "true")
	private ContactInfo contactInfo;
	
	/*
	 * Which village is home to this unit, and is the default for unit members booked in
	 */
	@Persistent
	private Key defaultVillageKey; 
	
	@Persistent
	private Text notes;
	
	@Persistent
	private boolean approved;
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public Key getOrganisationKey() {
		return organisationKey;
	}


	public void setOrganisation(Organisation organisation) {
		this.organisationKey = organisation.getKeyCheckNotNull();
	}


	public Text getNotes() {
		return notes;
	}


	public void setNotes(Text notes) {
		this.notes = notes;
	}


	public boolean isApproved() {
		return approved;
	}


	public void setApproved(boolean approved) {
		this.approved = approved;
	}


	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}


	public void setDefaultVillage(Key defaultVillageKey) {
		this.defaultVillageKey = defaultVillageKey;
	}


	public Key getDefaultVillageKey() {
		return defaultVillageKey;
	}


	public ContactInfo getContactInfo() {
		return contactInfo;
	}


	public List<Booking> getBookings(Event event)
	{
		return (CannedQueries.bookingsForUnit(this, event));
	}
	
	public String toString() {
		return getName();
	}
}
