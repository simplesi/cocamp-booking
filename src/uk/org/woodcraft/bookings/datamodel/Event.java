package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class Event extends KeyBasedData {
	
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
	
	public Event(String name, Date from, Date to) {
		this.name = name;
		this.publicEventStart = this.internalEventStart = from;
		this.publicEventEnd = this.internalEventEnd = to;
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
		
}
