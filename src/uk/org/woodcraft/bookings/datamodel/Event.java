package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.ValidatableModelObject;
import uk.org.woodcraft.bookings.utils.DateUtils;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@PersistenceCapable(detachable="true")
public class Event extends KeyBasedDataWithAudit implements NamedEntity, DeleteRestricted, ValidatableModelObject{
	
	private static final long serialVersionUID = 1L;

	@Persistent
	private String name; 
	
	@Persistent
	private Date publicEventStart; // When do booked people arrive?
	@Persistent
	private Date publicEventEnd;
	@Persistent
	private Date internalEventStart; // Earliest possible date on site (working parties, etc)
	@Persistent
	private Date internalEventEnd;
	
	@Persistent
	private Date earlyBookingDeadline; // Used for early booking discounts
	@Persistent
	private Date bookingDeadline; // Lose deposit after this point
	@Persistent
	private Date bookingSystemLocked; // Absolutely no changes whatsoever after this point
	
	/**
	 * Is the event currently open for booking?
	 */
	@Persistent
	private boolean isCurrentlyOpen; 
	
	public Event() {
	}
	
	public Event(String name, Date from, Date to, boolean isCurrentlyOpen) {
		this.name = name;
		setPublicEventStart(from);
		setPublicEventEnd(to);
		this.isCurrentlyOpen = isCurrentlyOpen;
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "Start date must be provided")
	public Date getPublicEventStart() {
		return publicEventStart;
	}
	public void setPublicEventStart(Date publicEventStart) {
		this.publicEventStart = DateUtils.cleanupTime(publicEventStart);
		if (this.internalEventStart == null) 
			this.internalEventStart = this.publicEventStart;
	}
	
	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "End date must be provided")
	public Date getPublicEventEnd() {
		return publicEventEnd;
	}
	public void setPublicEventEnd(Date publicEventEnd) {
		this.publicEventEnd = DateUtils.cleanupTime(publicEventEnd);
		if (this.internalEventEnd == null) 
			this.internalEventEnd = this.publicEventEnd;
	}
	public Date getInternalEventStart() {
		return internalEventStart;
	}
	public void setInternalEventStart(Date internalEventStart) {
		this.internalEventStart = DateUtils.cleanupTime(internalEventStart);
	}
	public Date getInternalEventEnd() {
		return internalEventEnd;
	}
	public void setInternalEventEnd(Date internalEventEnd) {
		this.internalEventEnd = DateUtils.cleanupTime(internalEventEnd);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setIsCurrentlyOpen(boolean isCurrentlyOpen) {
		this.isCurrentlyOpen = isCurrentlyOpen;
	}

	public boolean getIsCurrentlyOpen() {
		return isCurrentlyOpen;
	}
	
	public Collection<Village> getVillages() {
		return CannedQueries.villagesForEvent(this);
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public String getDeleteConditionError() {
		Collection<Booking> bookingsForEvent = CannedQueries.bookingsForEvent(this);
		
		if (bookingsForEvent.size() > 0)
		{
			return String.format("'%s' cannot be deleted as it still has %d bookings registered. These must be deleted first.", 		
					getName(), bookingsForEvent.size());
		}
		return "";
	}

	@Override
	public boolean deleteRequiresConfirmation() {
		return true;
	}

	@Override
	public Map<String, String> getValidationErrors() {
		Map<String, String> errors = new HashMap<String,String>();
		
		if (getInternalEventStart() != null && getPublicEventStart() != null
				&& getInternalEventStart().after(getPublicEventStart()))
			errors.put("internalEventStart", "The internal event start date must be the same as or earlier than the public event start");
		
		if (getInternalEventEnd() != null && getPublicEventEnd() != null
				&& getInternalEventEnd().before(getPublicEventEnd()))
			errors.put("internalEventEnd", "The internal event end date must be the same as or later than the public event end");
	
		if (getInternalEventStart() != null && getInternalEventEnd() != null
				&& getInternalEventStart().after(getInternalEventEnd()))
			errors.put("internalEventEnd", "The event event date must be after the start date");
		
		if (getName() != null){
			Event clashingEvent = CannedQueries.eventByName(getName(), getKey());
			
			if (clashingEvent != null )
			{
				errors.put("name", "This name is already in the bookings system. Please use another" );
			}
		}
		
		return errors;
	}

	public Date getEarlyBookingDeadline() {
		return earlyBookingDeadline;
	}

	public void setEarlyBookingDeadline(Date earlyBookingDeadline) {
		this.earlyBookingDeadline = earlyBookingDeadline;
	}

	public Date getBookingDeadline() {
		return bookingDeadline;
	}

	public void setBookingDeadline(Date bookingDeadline) {
		this.bookingDeadline = bookingDeadline;
	}

	public Date getBookingSystemLocked() {
		return bookingSystemLocked;
	}

	public void setBookingSystemLocked(Date bookingSystemLocked) {
		this.bookingSystemLocked = bookingSystemLocked;
	}
}
