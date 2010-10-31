package uk.org.woodcraft.bookings;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;

public class SwitchTargetAction extends ActionSupport implements SessionAware{

	private static final long serialVersionUID = 1L;

	private Map<String, Object> sessionData;
	
	private String eventKey;
	private String orgKey;
	private String unitKey;
	
	public String execute() {
		
		if(eventKey != null)
		{
			Event currentEvent = (Event)sessionData.get(SessionConstants.CURRENT_EVENT);
			if (!currentEvent.getWebKey().equals(eventKey))
			{
				Event newEvent = CannedQueries.eventByKey(KeyFactory.stringToKey(eventKey));
				if(newEvent == null)
					throw new IllegalArgumentException("Event matching key "+eventKey+" not found");
				
				// Will throw if not permissioned
				SecurityModel.checkAllowed(Operation.READ, newEvent);
				
				sessionData.put(SessionConstants.CURRENT_EVENT, newEvent);				
			}
		}
		
		if(orgKey != null)
		{
			Organisation currentOrg = (Organisation)sessionData.get(SessionConstants.CURRENT_ORG);
			if (!currentOrg.getWebKey().equals(orgKey))
			{
				Organisation newOrg = CannedQueries.orgByKey(KeyFactory.stringToKey(orgKey));
				if(newOrg == null)
					throw new IllegalArgumentException("Org matching key "+orgKey+" not found");
				
				// Will throw if not permissioned
				SecurityModel.checkAllowed(Operation.READ, newOrg);
				
				sessionData.put(SessionConstants.CURRENT_ORG, newOrg);				
			}
		}
		
		if(unitKey != null)
		{
			Unit currentUnit = (Unit)sessionData.get(SessionConstants.CURRENT_UNIT);
			if (!currentUnit.getWebKey().equals(unitKey))
			{
				Unit newUnit = CannedQueries.unitByKey(KeyFactory.stringToKey(unitKey));
				if(newUnit == null)
					throw new IllegalArgumentException("Unit matching key "+unitKey+" not found");
				
				// Will throw if not permissioned
				SecurityModel.checkAllowed(Operation.READ, newUnit);
				
				sessionData.put(SessionConstants.CURRENT_UNIT, newUnit);				
			}
		}
		
		return SUCCESS;
	}



	public void setNavEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public void setNavOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public void setNavUnitKey(String unitKey) {
		this.unitKey = unitKey;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionData = session;
		
	}
}