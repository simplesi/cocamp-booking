package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class UnitAction extends ActionSupport implements ModelDriven<Unit>, Preparable, SessionAware{

	private static final long serialVersionUID = 1L;
	
	String webKey;
	Unit unit = null;
	private Collection<Unit> unitList;

	private String defaultOrgWebKey;
	private Map<String, Object> session;

	@Override
	public Unit getModel() {
		return unit;
	}
	
	public void setWebKey(String key)
	{
		webKey = key;
	}
	
	public String edit() {
		if (unit == null) return ERROR;
		SecurityModel.checkAllowed(Operation.READ, unit);
		return INPUT;
	}
	
	public String save() {
		SecurityModel.checkAllowed(Operation.WRITE, unit);
		CannedQueries.save(unit);
		
		// They're in the signup process, so put it in the session so it appears in the dropdown
		if (! SessionUtils.userIsLoggedIn())
		{
			session.put(SessionConstants.SIGNUP_UNIT, unit);
		}
		
		return SUCCESS;
	}
	
	public String delete() {
		SecurityModel.checkAllowed(Operation.WRITE, unit);
		CannedQueries.delete(unit);
		return SUCCESS;
	}
	
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setUnitList(CannedQueries.allUnits(true));
		return SUCCESS;
	}

	public String listForOrg() {
		
		Organisation org = SessionUtils.getCurrentOrg();
		
		SecurityModel.checkAllowed(Operation.READ, org);
		setUnitList(CannedQueries.unitsForOrg(org, true));
		return SUCCESS;
	}
	
	public void setUnitList(Collection<Unit> unitList) {
		this.unitList = unitList;
	}

	public Collection<Unit> getUnitList() {
		return unitList;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public void prepare() throws Exception {
		
		if (webKey != null && webKey.length() > 0 ) 
		{
			unit = CannedQueries.unitByKey(KeyFactory.stringToKey(webKey));
		} else {
			Organisation currentOrg = SessionUtils.getCurrentOrg(); 
			if (currentOrg != null)
				unit = new Unit(currentOrg);
			else 
				unit = new Unit();
		}
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)session.get(SessionConstants.SIGNUP_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
			defaultOrgWebKey = userAddedOrg.getWebKey();
		}
		
		return orgs;
	}
	
	public String getDefaultOrgWebKey()
	{
		return defaultOrgWebKey;
	}

}
