package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
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
			addActionMessage("User "+email+" was not recognised, please try again.");
			return INPUT;
		}
		
		if(!SignupUtils.checkEmailHash(user, hash))
		{
			addActionMessage("Unable to validate input for user, please try again");
			return INPUT;
		}
		
		if (user.getAccessLevel() != Accesslevel.AWAITING_EMAIL_CONFIRM)
		{
			addActionMessage("User email has already been confirmed, no need to do this again. You can log in");
			return SUCCESS;
		}
		
		// FIXME: Need to work out how to assign their true role
		user.setAccessLevel(Accesslevel.UNASSIGNED);
		CannedQueries.save(user);
		
		addActionMessage("User email address was confirmed. Please log in");
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