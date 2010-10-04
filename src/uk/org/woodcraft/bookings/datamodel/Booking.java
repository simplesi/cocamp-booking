package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable="true")
public class Booking extends KeyBasedData implements NamedEntity{

	private static final long serialVersionUID = 1L;

	@Persistent
	private String name;

	@Persistent
	private Key eventKey;
	
	@Persistent
	private Key unitKey;
	
	@Persistent
	private Date dob;
	
	@Persistent
	private Email email;
	
	@Persistent
	private Date arrivalDate;
	
	@Persistent
	private Date departureDate;

	@Persistent
	private Key villageKey = null;
	
	@SuppressWarnings("unused")
	private Booking() {
		// For JDO
	}
	
	public Booking( String name, Unit unit, Event event) {
		this.name = name;
		this.unitKey = unit.getKeyCheckNotNull();
		
		//FIXME: Could be optimized to not pull back village
		Village village = unit.getDefaultVillageForEvent(event);
		if (village != null) villageKey = village.getKey();
		
		this.eventKey = event.getKeyCheckNotNull();
		this.arrivalDate = event.getPublicEventStart();
		this.departureDate = event.getPublicEventEnd();
	}
	
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Key getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(Key unitKey) {
		this.unitKey = unitKey;
	}

	public void setVillageKey(Key villageKey) {
		this.villageKey = villageKey;
	}

	public Key getVillageKey() {
		return villageKey;
	}


	public void setEventKey(Key eventKey) {
		this.eventKey = eventKey;
	}


	public Key getEventKey() {
		return eventKey;
	}
	
	public String toString() {
		return getName();
	}
	
}
