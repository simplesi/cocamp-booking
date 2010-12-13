package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.ValidatableModelObject;
import uk.org.woodcraft.bookings.pricing.PricingFactory;
import uk.org.woodcraft.bookings.pricing.PricingStrategy;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.DateUtils;
import uk.org.woodcraft.bookings.utils.SystemClock;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.opensymphony.xwork2.validator.annotations.DateRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


@PersistenceCapable(detachable="true")
public class Booking extends KeyBasedData implements NamedEntity, DeleteRestricted, ValidatableModelObject{

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
	
	@Persistent
	private String phoneNumber;
	
	@Persistent
	private String membershipNumber;
	
	@Persistent
	private Diet diet;
	
	@Persistent
	private String dietNotes;
	
	@Persistent
	private Text otherNeeds;
	
	@Persistent
	private boolean becomeMember = true;
	
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
	
	@Persistent
	private Date bookingCreationDate;
	
	@SuppressWarnings("unused")
	private Booking() {
		// For JDO
	}
	
	public Booking(Unit unit, Event event)
	{
		this(unit, event, new SystemClock());
	}
	
	public Booking(Unit unit, Event event, Clock clock)
	{
		this.unitKey = unit.getKeyCheckNotNull();
		
		//FIXME: Could be optimized to not pull back village
		Village village = unit.getDefaultVillageForEvent(event);
		if (village != null) villageKey = village.getKey();
		
		this.eventKey = event.getKeyCheckNotNull();
		this.arrivalDate = event.getPublicEventStart();
		this.departureDate = event.getPublicEventEnd();
		
		this.bookingCreationDate = clock.getTime();
		updatePrice();
	}
	
	public Booking( String name, Unit unit, Event event, Clock clock) {
		this(unit, event, clock);
		this.name = name;
	}
	
	@DateRangeFieldValidator(type = ValidatorType.FIELD, min = "1900/01/01", message = "Valid date of birth required")
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Date of Birth must be provided")
	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = DateUtils.cleanupTime(dob);
		updatePrice();
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
			return "Woodchip (under 6)";
		} else if (age < 10)
		{
			return "Elfin (6-9)";
		} else if (age < 13 )
		{
			return "Pioneer (10-12)";
		} else if (age < 16)
		{
			return "Venturer (13-15)";
		} else if (age < 21)
		{
			return "DF (16-20)";
		} 
		
		return "Adult";
	}

	@EmailValidator(type = ValidatorType.FIELD, message = "Email is required" )
	public String getEmail() {
		if (email == null) return null;
		return email.getEmail();
	}

	public void setEmail(String email) {
		this.email = new Email(email);
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Arrival date must be provided")
	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = DateUtils.cleanupTime(arrivalDate);
		updatePrice();
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Departure date must be provided")
	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = DateUtils.cleanupTime(departureDate);
		updatePrice();
	}

	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "5", trim = true, message = "Full Name is required")
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

	private void setPrice(long price) {
		this.price = price;
	}

	public long getPrice() {
		return price;
	}
	
	public void updatePrice() {
		PricingStrategy pricer = PricingFactory.getPricingStrategy();
		setPrice(pricer.priceOf(this));
	}

	@Override
	public String getDeleteConditionError() {
		return "";
	}

	@Override
	public boolean deleteRequiresConfirmation() {
		return true;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMembershipNumber() {
		return membershipNumber;
	}

	public void setMembershipNumber(String membershipNumber) {
		this.membershipNumber = membershipNumber;
	}

	public Diet getDiet() {
		return diet;
	}

	public void setDiet(Diet diet) {
		this.diet = diet;
	}

	public String getDietNotes() {
		return dietNotes;
	}

	public void setDietNotes(String dietNotes) {
		this.dietNotes = dietNotes;
	}

	public String getOtherNeeds() {
		if (otherNeeds == null) return null;
		return otherNeeds.getValue();
	}

	public void setOtherNeeds(String otherNeeds) {
		this.otherNeeds = new Text(otherNeeds);
	}

	public boolean getBecomeMember() {
		return becomeMember;
	}

	public void setBecomeMember(boolean becomeMember) {
		this.becomeMember = becomeMember;
	}

	@Override
	public Map<String, String> getValidationErrors() {
		Map<String, String> errors = new HashMap<String, String>();
		
		Event event = CannedQueries.eventByKey(getEventKey());
		if (event != null)
		{
			if (getArrivalDate() != null && getArrivalDate().before(event.getInternalEventStart()))
				errors.put("arrivalDate", "Arrival must be at or after the start of the event");
		
			if (getDepartureDate()!= null && getDepartureDate().after(event.getInternalEventEnd()))
				errors.put("departureDate", "Departure must at on or before the end of the event");	
		
			if (getDob()!= null && getDob().after(event.getInternalEventStart()))
				errors.put("dob", "Date of birth cannot be after event start");
		}
		
		if (getArrivalDate().after(getDepartureDate()))
			errors.put("departureDate", "Departure must be after arrival");
		
		return errors;
	}

	public void setBookingCreationDate(Date bookingCreationDate) {
		this.bookingCreationDate = bookingCreationDate;
	}

	public Date getBookingCreationDate() {
		return bookingCreationDate;
	}

	
	
}
