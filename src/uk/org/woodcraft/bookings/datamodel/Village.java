package uk.org.woodcraft.bookings.datamodel;

import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Village extends KeyBasedData{

	public Village(String name, Event event) {
		this.name = name;
		this.eventKey = event.getKeyCheckNotNull();
	}

	@Persistent
	private String name;
	
	@Persistent
	private Key eventKey;

	public Set<Booking> getBookings()
	{
		// TODO: Implement
		return null;
	}
	
	public Set<Unit> getUnits()
	{
		// TODO: Implement
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEventKey(Key eventKey) {
		this.eventKey = eventKey;
	}

	public Key getEventKey() {
		return eventKey;
	}
	
	
	
}
