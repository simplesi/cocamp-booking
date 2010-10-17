package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(detachable="true")
public class Organisation extends KeyBasedDataWithContactInfo implements NamedEntity{
		
	private static final long serialVersionUID = 1L;

	public Organisation(String name, boolean approved) {
		this.name = name;
		this.approved = approved;
	}
	
	public Organisation() {
		// For JDO
	}

	@Persistent
	private String name;

	@Persistent
	private boolean approved;
	
	@Persistent
	private Text notes; // internal comments about the organisation, non-searchable
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getNotes() {
		return notes.getValue();
	}
	
	public void setNotes(Text notes) {
		this.notes = notes;
	}
	
	public void setNotes(String notes) {
		this.notes = new Text(notes);
	}
	


	public Collection<Unit> getUnits(boolean includeUnapproved) {
	
		return CannedQueries.unitsForOrg(this, includeUnapproved);
	}
	
	public String toString() {
		return getName();
	}
}
