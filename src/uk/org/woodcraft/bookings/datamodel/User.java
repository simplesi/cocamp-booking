package uk.org.woodcraft.bookings.datamodel;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import uk.org.woodcraft.bookings.auth.PasswordUtils;

@PersistenceCapable(detachable="true")
public class User {

	@PrimaryKey
	@Persistent
	private String email;
	
	@Persistent
	private String name;
	
	@Persistent
	private String passwordEncrypted;
	
	@Persistent(serialized="true")
	private Accesslevel accessLevel;
	
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
	
}
