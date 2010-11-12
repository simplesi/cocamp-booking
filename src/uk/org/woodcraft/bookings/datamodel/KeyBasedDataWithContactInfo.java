package uk.org.woodcraft.bookings.datamodel;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class KeyBasedDataWithContactInfo extends KeyBasedData {

	private static final long serialVersionUID = 1L;

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
	
	@EmailValidator(type = ValidatorType.FIELD, message = "Email must be blank or valid" )
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
