package uk.org.woodcraft.bookings.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.utils.SessionUtils;

public class UserAction extends BasePersistenceAction<User>{

	private static final long serialVersionUID = 1L;
	
	private String email;
	
	public String list() {
		return listAll();
	}
	
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(filterSystemUsers(CannedQueries.allUsers()));
		return SUCCESS;
	}

	public String listForOrg() {
		
		Organisation org = SessionUtils.getCurrentOrg();
		
		SecurityModel.checkAllowed(Operation.READ, org);
		setModelList(filterSystemUsers(CannedQueries.allUsersForOrg(org)));
		return SUCCESS;
	}
	
	public String listForUnit() {
		
		Unit unit = SessionUtils.getCurrentUnit();
		
		SecurityModel.checkAllowed(Operation.READ, unit);
		setModelList(filterSystemUsers(CannedQueries.allUsersForUnit(unit)));
		return SUCCESS;
	}
	
	private List<User> filterSystemUsers(Collection<User> userList)
	{
		List<User> results = new ArrayList<User>(userList.size());
		for(User user : userList)
		{
			if (user.getEmail().equals(CoreData.SYSTEM_USER_EMAIL)) 
				continue;
			
			results.add(user);
		}
		return results;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public void prepare() throws Exception {
		if (email != null && email.length() > 0 ) 
		{
			setModel(CannedQueries.getUserByEmail(email));	
		} 
		
		if (getModel() == null)
		{
			User user = new User();
			user.setIsNew(true);
		
			// Take the defaults from the current session
			
			Unit userAddedUnit = (Unit)getSessionObject(SessionConstants.CURRENT_UNIT);
			if (userAddedUnit != null) {
				// use the org of the unit if possible so we get consistency
				user.setOrganisationKey(userAddedUnit.getOrganisationKey());
				user.setUnitKey(userAddedUnit.getKey());
				return;
			}
		}
	}
	
	// For the signup process
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
		}
		
		return orgs;
	}

	// For the signup process
	public Collection<Unit> getAllUnits()
	{
		Collection<Unit> units = CannedQueries.allUnits(false);
		Unit userAddedUnit = (Unit)getSessionObject(SessionConstants.SIGNUP_UNIT);
		if (userAddedUnit != null) {
			units.add(userAddedUnit);
		}
		return units;
	}
	
	protected boolean deleteRequiresConfirmation()
	{
		return true;
	}
}