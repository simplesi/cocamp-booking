package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Booking {

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Key key;
	
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
	private Key villageKey;
	
	public Booking( String name, Unit unit, Event event) {
		this.name = name;
		this.unitKey = unit.getKey();
		this.villageKey = unit.getDefaultVillageKey();
		this.eventKey = event.getKey();
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

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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
	
	
}
