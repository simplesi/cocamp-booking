package uk.org.woodcraft.bookings.auth;

import com.opensymphony.xwork2.ActionSupport;

public class BasicLoginAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;

	public String login(){
		if(email.equals("foo@cocamp") && password.equals("cocamp")){
			addActionMessage("You are successfully logged in");
			return SUCCESS;
		}
		addActionError("Username or password invalid");
		return INPUT;
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