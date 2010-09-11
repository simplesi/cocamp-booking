package uk.org.woodcraft.bookings.datamodel;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceAware;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

@PersistenceAware
public class ContactInfo {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private PostalAddress address;
	
	@Persistent
	private PhoneNumber phone;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PostalAddress getAddress() {
		return address;
	}

	public void setAddress(PostalAddress address) {
		this.address = address;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	@Persistent
	private Email email;
		
}
