package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.NotPersistent;
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
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.opensymphony.xwork2.validator.annotations.DateRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@PersistenceCapable(detachable = "true")
public class Booking extends KeyBasedDataWithAudit implements NamedEntity,
		DeleteRestricted, ValidatableModelObject, Comparable<Booking> {

	
	
	private static final long serialVersionUID = 1L;

	@Persistent
	private String name;

	@Persistent
	private Key eventKey;

	@Persistent
	private Key unitKey;

	@Persistent
	private Date dob;
	
	@NotPersistent
	private Integer age;

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
	private String role;

	@Persistent
	private boolean becomeMember = false;

	/**
	 * The price of the booking, in pounds Note - cannot be called price since
	 * earlier version of class used this with a long price
	 */
	@Persistent
	private Double fee;

	/**
	 * Has this booking been cancelled, and if so, on what date?
	 */
	@Persistent
	private Date cancellationDate;

	@Persistent
	private Date bookingCreationDate;

	/**
	 * This is the date when the booking was unlocked so that it could be
	 * updated after the deadline, qualifying for late booking fee
	 */
	@Persistent
	private Date bookingUnlockDate;

	/**
	 * The my village key they receive by email, used to validate them
	 */
	@Persistent
	private String myVillageAuthKey;

	/**
	 * The my village username
	 */
	@Persistent
	private String myVillageUsername;

	@SuppressWarnings("unused")
	private Booking() {
		// For JDO
	}

	private Booking(Unit unit, Event event, Clock clock) {
		this.unitKey = unit.getKeyCheckNotNull();

		// Grab the default village from the unit, if it's set
		// This can be overriden by the admin later if required
		Key defaultVillageKey = unit.getVillageKey();
		if (defaultVillageKey != null)
			villageKey = defaultVillageKey;

		this.eventKey = event.getKeyCheckNotNull();
		this.arrivalDate = event.getPublicEventStart();
		this.departureDate = event.getPublicEventEnd();

		this.bookingCreationDate = clock.getTime();
	}

	private Booking(String name, Unit unit, Event event, Clock clock) {
		this(unit, event, clock);
		
		if (name != null) this.name = name;
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
	 * 
	 * @return
	 */	
	public AgeGroup getAgeGroup() {

		if (this.dob == null)
			return AgeGroup.Unknown;

		Event event = CannedQueries.eventByKey(eventKey);
		if (event == null)
			return AgeGroup.Unknown;

		return AgeGroup.groupFor(getAge());
	}

	/**
	 * Age as of the first day of the event
	 * 
	 * @return
	 */
	@SkipInCannedReports
	private int getAge() {
		if (age != null && age != -1) return age;
		
		if (this.dob == null)
			return -1;

		Event event = CannedQueries.eventByKey(eventKey);
		if (event == null)
			return -1;

		age = DateUtils.ageOnDay(dob, event.getPublicEventStart());
		
		return age;
	}
	
	/**
	 * Are they over 18 on the first day?
	 * 
	 * @return
	 */
	@SkipInCannedReports
	public Boolean getOver18() {
		if (getAge() >= 18) 
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	@EmailValidator(type = ValidatorType.FIELD, message = "Email is required")
	public String getEmail() {
		if (email == null)
			return null;
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

	public Unit getUnit() {
		if (unitKey == null) return null;
		return CannedQueries.unitByKey(unitKey);
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

	public void setVillageWebKey(String villageWebKey) {
		this.villageKey = KeyFactory.stringToKey(villageWebKey);
	}
	
	public Key getVillageKey() {
		return villageKey;
	}

	public String getVillageWebKey() {
		if (villageKey == null) return "";
		return KeyFactory.keyToString(villageKey);
	}
	
	public Village getVillage() {
		Village village = null;
		if (villageKey !=null)
			village = CannedQueries.villageByKey(villageKey);
		
		return village;
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
		if (fee == null)
			return 0;
		return fee;
	}

	public void updateFee() {
		Event event = CannedQueries.eventByKey(eventKey);
		PricingStrategy pricer = PricingFactory.getPricingStrategy(event);
		setFee(pricer.priceOf(this));
	}

	@Override
	public String getDeleteConditionError(Clock clock) {
		Event event = CannedQueries.eventByKey(eventKey);
		if (event != null) {
			if (event.getEarlyBookingDeadline().before(clock.getTime())) {
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
		if (otherNeeds == null)
			return null;
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
		if (event != null) {
			if (getArrivalDate() != null
					&& getArrivalDate().before(event.getInternalEventStart()))
				errors.put("arrivalDate",
						"Arrival must be at or after the start of the event");

			if (getDepartureDate() != null
					&& getDepartureDate().after(event.getInternalEventEnd()))
				errors.put("departureDate",
						"Departure must at on or before the end of the event");

			if (getDob() != null
					&& getDob().after(event.getInternalEventStart()))
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
	
	public static final Booking create(String name, Unit unit, Event event, Clock clock) {
		Booking booking = new Booking(name, unit, event, clock);
		
		// Must be done after construction as requires a reference to the object itself.
		booking.updateFee();
		return booking;
	}
	
	public static final Booking create(Unit unit, Event event, Clock clock) {
		return create(null, unit, event, clock);
	}
	
	public static final Booking create(Unit unit, Event event) {
		return create(unit, event, new SystemClock());
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}