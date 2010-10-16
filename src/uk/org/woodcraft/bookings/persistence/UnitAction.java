package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

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
	
	public String save() {
		super.save();
		
		// They're in the signup process, so put it in the session so it appears in the dropdown
		if (! SessionUtils.userIsLoggedIn())
		{
			setSessionObject(SessionConstants.SIGNUP_UNIT, getModel());
		}
		
		return SUCCESS;
	}
	
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allUnits(true));
		return SUCCESS;
	}

	// For current org only
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
				setModel(new Unit());
		}
	}

	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ORG);
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
