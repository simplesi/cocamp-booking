package uk.org.woodcraft.bookings.utils;

import javax.servlet.http.HttpSession;

import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;

import com.opensymphony.xwork2.ActionContext;

public class SessionUtils {

	
	public static boolean userIsLoggedIn()
	{
		return (currentUser(false) != null);
	}
	
	public static User currentUser(boolean throwIfNotSignedIn)
	{
		ActionContext context = ActionContext.getContext();
		User user = null;
		
		// Fetch it from the session
		if(context != null)
			user = (User) ActionContext.getContext().getSession().get(SessionConstants.USER_HANDLE);
		
		if(user == null && throwIfNotSignedIn) throw new SecurityException("You must be logged in to perform this action");
		
		return user;
	}
	
	public static Organisation getCurrentOrg()
	{
		return (Organisation) ActionContext.getContext().getSession().get(SessionConstants.CURRENT_ORG);
	}
	
	public static Unit getCurrentUnit()
	{
		return (Unit) ActionContext.getContext().getSession().get(SessionConstants.CURRENT_UNIT);
	}
	
	public static Event getCurrentEvent()
	{
		return (Event) ActionContext.getContext().getSession().get(SessionConstants.CURRENT_EVENT);
	}
	
	public static void setCurrentOrgAndUnit(HttpSession session, Organisation org, Unit unit)
	{
		session.setAttribute(SessionConstants.CURRENT_ORG, org);
		session.setAttribute(SessionConstants.CURRENT_UNIT, unit);
	}
}
