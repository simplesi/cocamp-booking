package uk.org.woodcraft.bookings.auth;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;

	private Map<String, Object> session;
	
	public String execute(){
		session.clear();
		
		addActionMessage("You were successfully logged out");	
		return SUCCESS;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
