package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.opensymphony.xwork2.ActionSupport;

public class ConfirmEmailAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private String email;
	private String hash;

	public String execute(){
		
		if(email == null || hash == null) return INPUT;
		
		User user = CannedQueries.getUserByEmail(email);

		if (user == null)
		{
			addActionError("User "+email+" was not recognised, please try again.");
			return INPUT;
		}
		
		if(!SignupUtils.checkEmailHash(user, hash))
		{
			addActionError("Unable to validate input for user, please try again");
			return INPUT;
		}
		
		if (user.getEmailValidated())
		{
			addActionMessage("User email has already been confirmed, no need to do this again.");
			return SUCCESS;
		}
		
		user.setEmailValidated(true);
		CannedQueries.save(user);
		
		if (user.getApproved())
		{
			addActionMessage("User email address was confirmed. You can now log in.");
		} else {
			
			addActionMessage("User email address was confirmed. Now awaiting admin approval - you will receive a further email when you have been granted access to the booking system by our staff.");
		}
		
		
		return SUCCESS;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	
	
}