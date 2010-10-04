package uk.org.woodcraft.bookings.datamodel;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class KeyBasedDataWithContactInfo extends KeyBasedData {

	@Persistent
	protected String email;
	
	@Persistent
	protected String phone;
	
	@Persistent
	protected String address;
	
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	public String getAddress()
	{
		return address;
	}
	
}
