package uk.org.woodcraft.bookings.utils;

import javax.servlet.http.HttpSession;

import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;

import com.opensymphony.xwork2.ActionContext;

public class SessionUtils {

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
