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
public class Booking extends KeyBasedDataWithAudit implements NamedEntity, DeleteRestricted, ValidatableModelObject, Comparable<Booking>{

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
	 * The price of the booking, in pounds
	 * Note - cannot be called price since earlier version of class used this with a long price
	 */
	@Persistent
	private Double fee;
	
	/**
	 *  Has this booking been cancelled, and if so, on what date?
	 */
	@Persistent
	private Date cancellationDate;
	
	@Persistent
	private Date bookingCreationDate;
	
	/**
	 *  This is the date when the booking was unlocked so that it could be updated after the deadline, qualifying for late booking fee
	 */
	@Persistent
	private Date bookingUnlockDate;
	
	/**
	 *  The my village key they receive by email, used to validate them
	 */
	@Persistent
	private String myVillageAuthKey;

	/**
	 *  The my village username
	 */
	@Persistent
	private String myVillageUsername;
	
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
		updateFee();
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
		updateFee();
	}
	
	/**
	 * Age group as of the first day of the event
	 * @return
	 */
	public AgeGroup getAgeGroup() {
		if (this.dob == null) return AgeGroup.Unknown;
		
		Event event = CannedQueries.eventByKey(eventKey);	
		if (event == null) return AgeGroup.Unknown;
		
		int age = DateUtils.ageOnDay(dob, event.getPublicEventStart() );
		
		return AgeGroup.groupFor(age);
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
		updateFee();
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Departure date must be provided")
	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = DateUtils.cleanupTime(departureDate);
		updateFee();
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
	
	public boolean getIsCancelled() {
		return getCancellationDate() != null;
	}

	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
		updateFee();
	}

	public Date getCancellationDate() {
		return cancellationDate;
	}

	private void setFee(double price) {
		this.fee = price;
	}

	public double getFee() {
		if (fee == null ) return 0;
		return fee;
	}
	
	public void updateFee() {
		PricingStrategy pricer = PricingFactory.getPricingStrategy();
		setFee(pricer.priceOf(this));
	}

	@Override
	public String getDeleteConditionError(Clock clock) {
		Event event = CannedQueries.eventByKey(eventKey);
		if (event != null)
		{
			if (event.getEarlyBookingDeadline().before(clock.getTime()))
			{
				return "Cannot delete booking after early booking deadline. Only cancellations are possible";
			}
		}
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
	@SkipInCannedReports
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

	public void setBookingUnlockDate(Date bookingUnlockDate) {
		this.bookingUnlockDate = bookingUnlockDate;
		updateFee();
	}

	public Date getBookingUnlockDate() {
		return bookingUnlockDate;
	}

	@Override
	public int compareTo(Booking o) {
		return this.getName().compareTo(o.getName());
	}

	public void setMyVillageAuthKey(String myVillageAuthKey) {
		this.myVillageAuthKey = myVillageAuthKey;
	}

	public String getMyvillageAuthKey() {
		return myVillageAuthKey;
	}

	public void setMyVillageUsername(String myVillageUsername) {
		this.myVillageUsername = myVillageUsername;
	}

	public String getMyVillageUsername() {
		return myVillageUsername;
	}
}