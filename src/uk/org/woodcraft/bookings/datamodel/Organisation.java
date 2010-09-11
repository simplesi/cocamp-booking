package uk.org.woodcraft.bookings.datamodel;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Organisation {
	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	private Key key;
	
	@Persistent
	private String name;

	@Persistent(dependent = "true")
	private ContactInfo contactInfo;
	
	@Persistent
	private boolean approved;
	
	@Persistent
	private Text notes; // internal comments about the organisation, non-searchable
	
	
	
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

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

	public Set<Unit> getUnits()
	{
		throw(new RuntimeException("needs to be implemented"));
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}
}
