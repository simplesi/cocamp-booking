package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.Key;

public class UnitAction extends BasePersistenceAction<Unit>{

	private static final long serialVersionUID = 1L;

	private String defaultOrgWebKey;
	
	@SkipValidation
	public String editCurrent() {
		setModel((Unit)getSessionObject(SessionConstants.CURRENT_UNIT));
		return edit();
	}
	
	public String save() {
		super.save();
		
		// They're in the signup process, so put it in the session so it appears in the dropdown
		if (! SessionUtils.userIsLoggedIn())
		{
			setSessionObject(SessionConstants.SIGNUP_ADDED_UNIT, getModel());
			setSessionObject(SessionConstants.SIGNUP_UNIT, getModel());
		}
		SessionUtils.syncSessionCacheIfRequired(getSession(), SessionConstants.CURRENT_UNIT, getModel());
		return SUCCESS;
	}
	
	@SkipValidation
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allUnits(true));
		return SUCCESS;
	}

	// For current org only
	@SkipValidation
	public String list() {
		
		Organisation org = SessionUtils.getCurrentOrg();
		
		SecurityModel.checkAllowed(Operation.READ, org);
		setModelList(CannedQueries.unitsForOrg(org, true));
		return SUCCESS;
	}
	

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null ) 
		{
			setModel(CannedQueries.unitByKey(key));
		} else {
			Organisation currentOrg = SessionUtils.getCurrentOrg(); 
			if (currentOrg != null)
				setModel(new Unit(currentOrg));
			else 
			{
				Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ADDED_ORG);
				if (userAddedOrg != null) {
					setModel(new Unit(userAddedOrg));
				} else 
					setModel(new Unit());
			}
		}
	}

	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ADDED_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
		}
		
		return orgs;
	}
	
	public String getDefaultOrgWebKey()
	{
		return defaultOrgWebKey;
	}

}
