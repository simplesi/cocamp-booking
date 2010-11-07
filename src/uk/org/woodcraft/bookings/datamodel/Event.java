package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

@PersistenceCapable(detachable="true")
public class Event extends KeyBasedDataWithAudit implements NamedEntity, DeleteRestricted{
	
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
	
	/**
	 * Is the event currently open for booking?
	 */
	@Persistent
	private boolean isCurrentlyOpen; 
	
	public Event() {
	}
	
	public Event(String name, Date from, Date to, boolean isCurrentlyOpen) {
		this.name = name;
		this.publicEventStart = from;
		this.publicEventEnd = to;
		this.isCurrentlyOpen = isCurrentlyOpen;
	}
	
	public Date getPublicEventStart() {
		return publicEventStart;
	}
	public void setPublicEventStart(Date publicEventStart) {
		this.publicEventStart = publicEventStart;
	}
	public Date getPublicEventEnd() {
		return publicEventEnd;
	}
	public void setPublicEventEnd(Date publicEventEnd) {
		this.publicEventEnd = publicEventEnd;
	}
	public Date getInternalEventStart() {
		return internalEventStart;
	}
	public void setInternalEventStart(Date internalEventStart) {
		this.internalEventStart = internalEventStart;
	}
	public Date getInternalEventEnd() {
		return internalEventEnd;
	}
	public void setInternalEventEnd(Date internalEventEnd) {
		this.internalEventEnd = internalEventEnd;
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
		
}
