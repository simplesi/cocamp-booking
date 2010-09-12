package uk.org.woodcraft.bookings.datamodel;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Organisation extends KeyBasedData implements NamedEntity{
		
	public Organisation(String name, boolean approved) {
		this.name = name;
		this.approved = approved;
	}
	
	@Persistent
	private String name;

	@Persistent(dependent = "true")
	private ContactInfo contactInfo;
	
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

	public Text getNotes() {
		return notes;
	}

	public void setNotes(Text notes) {
		this.notes = notes;
	}

	public List<Unit> getUnits(boolean includeUnapproved)
	{
		return (CannedQueries.unitsForOrg(this, includeUnapproved));
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	
	public String toString() {
		return getName();
	}
}
