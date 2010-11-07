package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.Key;

public class OrganisationAction extends BasePersistenceAction<Organisation>{

	private static final long serialVersionUID = 1L;
	
	public String editCurrent() {
		setModel((Organisation)getSessionObject(SessionConstants.CURRENT_ORG));
		return edit();
	}
	
	public String save() {
		
		super.save();
		
		// They're in the signup process, so put it in the session so it appears in the dropdown
		if (! SessionUtils.userIsLoggedIn())
		{
			setSessionObject(SessionConstants.SIGNUP_ORG, getModel());
		}
		SessionUtils.syncSessionCacheIfRequired(getSession(), SessionConstants.CURRENT_ORG, getModel());
		
		return SUCCESS;
	}
	
	public String list() {
		
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allOrgs(true));
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null)
		{
			setModel(CannedQueries.orgByKey(key));
		} else {
			setModel(new Organisation());
		}
	}
	
	protected boolean deleteRequiresConfirmation()
	{
		return false;
	}
	
	protected boolean checkDeleteConditionsMet()
	{
		Collection<Unit> unitsForOrg = CannedQueries.unitsForOrg(getWebKeyAsKey(), true);
		
		if (unitsForOrg.size() > 0)
		{
			addActionError(String.format("This organisation cannot be deleted as it still has %d units as part of it. These must be deleted first.", 
					unitsForOrg.size()));
			return false;
		}
		return true;
	}

}
