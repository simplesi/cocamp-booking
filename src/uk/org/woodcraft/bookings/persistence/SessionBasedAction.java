package uk.org.woodcraft.bookings.persistence;

import java.io.Serializable;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;

import com.opensymphony.xwork2.ActionSupport;

public class SessionBasedAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 5509241587017581635L;
	private Map<String, Object> session;

	public SessionBasedAction() {
		super();
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	protected Map<String,Object> getSession() {
		return session;
	}

	protected Object getSessionObject(String key) {
		if (session == null) return null;
		
		return session.get(key);
	}

	protected void setSessionObject(String key, Serializable value) {
		if (session == null) return;
		
		session.put(key, value);
	}

	public Unit getCurrentUnit() {
		return (Unit)getSessionObject(SessionConstants.CURRENT_UNIT);
	}

	public Organisation getCurrentOrganisation() {
		return (Organisation)getSessionObject(SessionConstants.CURRENT_ORG);
	}

	public Event getCurrentEvent() {
		return (Event)getSessionObject(SessionConstants.CURRENT_EVENT);
	}

	public User getCurrentUser() {
		return (User)getSessionObject(SessionConstants.USER_HANDLE);
	}

}