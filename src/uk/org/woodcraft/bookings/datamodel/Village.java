package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@PersistenceCapable(detachable="true")
public class Village extends KeyBasedData implements NamedEntity{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Village() {
		// For JDO
	}
	
	public Village(String name, Event event) {
		this.name = name;
		this.eventKey = event.getKeyCheckNotNull();
	}

	@Persistent
	private String name;
	
	@Persistent
	private Key eventKey;

	public Collection<Booking> getBookings()
	{
		return CannedQueries.bookingsForVillage(this);
	}
	
	public Collection<Unit> getUnits()
	{
		return CannedQueries.unitsForVillage(this);
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "1", trim = true, message = "Name cannot be empty")
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
