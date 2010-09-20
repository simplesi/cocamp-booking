package uk.org.woodcraft.bookings.auth;

import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BasicLoginAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;

	public String login(){
		
		User user = CannedQueries.getUserByEmail(email);
		
		if (user != null && user.checkPassword(password))
		{
			createSession(user);
			
			addActionMessage("You are successfully logged in");
			return SUCCESS;
		}
	
		addActionError("Username or password invalid");
		return INPUT;
	}

	private void createSession(User user)
	{
		ActionContext.getContext().getSession().put(SessionConstants.USER, user);
		//ActionContext.getContext().getSession().put(SessionConstants.CURRENT_ORG, user.);
		//ActionContext.getContext().getSession().put(SessionConstants.CURRENT_UNIT, user.);
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}