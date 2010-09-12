package uk.org.woodcraft.bookings.datamodel;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Village extends KeyBasedData implements NamedEntity{

	public Village(String name, Event event) {
		this.name = name;
		this.eventKey = event.getKeyCheckNotNull();
	}

	@Persistent
	private String name;
	
	@Persistent
	private Key eventKey;

	public List<Booking> getBookings()
	{
		return(CannedQueries.bookingsForVillage(this));
	}
	
	public List<Unit> getUnits()
	{
		return(CannedQueries.unitsForVillage(this));
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
	
	public String toString() {
		return getName();
	}
	
}
