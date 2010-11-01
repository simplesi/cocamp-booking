package uk.org.woodcraft.bookings.datamodel;

import java.io.Serializable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import uk.org.woodcraft.bookings.auth.PasswordUtils;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(detachable="true")
public class User implements Serializable, Keyed<String>, NamedEntity {

	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent
	private String email;
	
	@Persistent
	private String name;
	
	@Persistent
	private String passwordEncrypted;
	
	@Persistent(serialized="true")
	private Accesslevel accessLevel;
	
	@Persistent
	private Key organisationKey;
	
	@Persistent
	private Key unitKey;
	
	// Track whether this is a new or an existing object
	// Use of Boolean (Rather than boolean) to get round JDO confusion about this property
	@NotPersistent
	private boolean isNew = false;
	
	public User() {
	}
	
	public User(String email, String name, String password, Accesslevel accessLevel) {
		this.email = email;
		this.name = name;
		setPassword(password);
		this.accessLevel = accessLevel;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordEncrypted() {
		return passwordEncrypted;
	}

	/**
	 * Set (and encrypt) a new plaintext password
	 * @param password Plaintext password
	 */
	public void setPassword(String password) {
		this.passwordEncrypted = PasswordUtils.encodePassword(password);
	}
	
	/**
	 * Check whether this password matches the stored password or not
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String password) {
		return PasswordUtils.checkPassword(password, this.passwordEncrypted);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Accesslevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(Accesslevel accessLevel) {
		this.accessLevel = accessLevel;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof User)) return false;
		User other = (User) obj;
		
		if (this.email == null) return (other.email == null);
		
		return this.email.equals(other.email);
	}
	
	@Override
	public int hashCode() {
		if (this.email == null) return 0;
		return this.email.hashCode();
	}

	public Key getOrganisationKey() {
		return organisationKey;
	}

	public void setOrganisationKey(Key organisationKey) {
		this.organisationKey = organisationKey;
	}

	public String getOrganisationWebKey()
	{
		if (this.organisationKey != null) 
			return KeyFactory.keyToString(organisationKey);
		
		return null;
	}
	
	public void setOrganisationWebKey(String organisationWebKey) {
		this.organisationKey = KeyFactory.stringToKey(organisationWebKey);
	}
	
	public Organisation getOrganisation()
	{
		if(organisationKey == null) return null;
		return CannedQueries.orgByKey(organisationKey);
	}
	
	public String getOrganisationName()
	{
		Organisation org = getOrganisation();
		if(org == null) return "";
		return org.getName();
	}
	
	public Key getUnitKey() {
		return unitKey;
	}
	
	public void setUnitKey(Key unitKey) {
		this.unitKey = unitKey;
	}
	
	public String getUnitWebKey() {
		if (unitKey != null)
			return KeyFactory.keyToString(unitKey);
		return null;
	}
	
	public void setUnitWebKey(String unitWebKey) {
		unitKey = KeyFactory.stringToKey(unitWebKey);
	}
	
	public Unit getUnit()
	{
		if(unitKey == null) return null;
		return CannedQueries.unitByKey(unitKey);
	}
	
	public String getUnitName()
	{
		Unit unit = getUnit();
		if (unit == null) return "";
		
		return unit.getName();
	}

	@Override
	public void setKey(String key) {
		setEmail(key);
	}

	@Override
	public String getKey() {
		return getEmail();
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return isNew;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
