package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.pricing.PricingFactory;
import uk.org.woodcraft.bookings.pricing.PricingStrategy;
import uk.org.woodcraft.bookings.utils.DateUtils;

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
	
	/**
	 * The price of the booking, in pence
	 */
	@Persistent
	private long price;
	
	/**
	 *  Has this booking been cancelled, and if so, on what date?
	 */
	@Persistent
	private Date cancellationDate;
	
	@SuppressWarnings("unused")
	private Booking() {
		// For JDO
	}
	
	public Booking(Unit unit, Event event)
	{
		this.unitKey = unit.getKeyCheckNotNull();
		
		//FIXME: Could be optimized to not pull back village
		Village village = unit.getDefaultVillageForEvent(event);
		if (village != null) villageKey = village.getKey();
		
		this.eventKey = event.getKeyCheckNotNull();
		this.arrivalDate = event.getPublicEventStart();
		this.departureDate = event.getPublicEventEnd();
		updatePrice();
	}
	
	public Booking( String name, Unit unit, Event event) {
		this(unit, event);
		this.name = name;
	}
	
	
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	/**
	 * Age group as of the first day of the event
	 * @return
	 */
	public String getAgeGroup() {
		if (this.dob == null) return "";
		
		Event event = CannedQueries.eventByKey(eventKey);		
		int age = DateUtils.ageOnDay(dob, event.getPublicEventStart() );
		
		if(age < 6)
		{
			return "Woodchip";
		} else if (age < 10)
		{
			return "Elfin";
		} else if (age < 13 )
		{
			return "Pioneer";
		} else if (age < 16)
		{
			return "Venturer";
		} else if (age < 21)
		{
			return "DF";
		} 
		
		return "Adult";
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
		updatePrice();
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
		updatePrice();
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
	
	public boolean isCancelled() {
		return getCancellationDate() != null;
	}

	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
		updatePrice();
	}

	public Date getCancellationDate() {
		return cancellationDate;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getPrice() {
		return price;
	}
	
	public void updatePrice() {
		PricingStrategy pricer = PricingFactory.getPricingStrategy();
		setPrice(pricer.priceOf(this));
	}
	
}
