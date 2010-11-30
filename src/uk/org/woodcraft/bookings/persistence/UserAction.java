package uk.org.woodcraft.bookings.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

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
	private String oldPassword;
	private String newPassword;
	private String newPasswordConfirm;
	
	@SkipValidation
	public String list() {
		return listAll();
	}
	
	@SkipValidation
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(filterSystemUsers(CannedQueries.allUsers()));
		return SUCCESS;
	}

	@SkipValidation
	public String listForOrg() {
		
		Organisation org = SessionUtils.getCurrentOrg();	
		
		SecurityModel.checkAllowed(Operation.READ, org);
		setModelList(filterSystemUsers(CannedQueries.allUsersForOrg(org)));
		return SUCCESS;
	}
	
	@SkipValidation
	public String listForUnit() {
		
		Unit unit = SessionUtils.getCurrentUnit();
		
		SecurityModel.checkAllowed(Operation.READ, unit);
		setModelList(filterSystemUsers(CannedQueries.allUsersForUnit(unit)));
		return SUCCESS;
	}
	
	@SkipValidation
	public String editCurrent() {
		setModel((User)getSessionObject(SessionConstants.USER_HANDLE));
		return edit();
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
	
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ADDED_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
		}
		
		return orgs;
	}

	public Collection<Unit> getAllUnits()
	{
		Collection<Unit> units = CannedQueries.allUnits(false);
		Unit userAddedUnit = (Unit)getSessionObject(SessionConstants.SIGNUP_ADDED_UNIT);
		if (userAddedUnit != null) {
			units.add(userAddedUnit);
		}
		return units;
	}
	
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String changePassword() {
		setModel((User)getSessionObject(SessionConstants.USER_HANDLE));
		setEmail(getModel().getEmail());
		return changePasswordGeneric();
	}
	
	public String changePasswordGeneric() {
		User user = (User) getModel();
		if (user == null) return ERROR;
		
		SecurityModel.checkAllowed(Operation.WRITE, user);
		
		if (oldPassword == null && newPassword == null) return INPUT;
		
		if (!getCurrentUser().getAccessLevel().getIsSuperUser() && !user.checkPassword(oldPassword)) 
		{
			addActionError("Current password supplied is incorrect. Please try again");
			return INPUT;
		}
		if (newPassword == null || newPassword.length() == 0 )
		{
			addActionError("A new password must be supplied.");
			return INPUT;
		}
		if (!newPassword.equals(newPasswordConfirm))
		{
			addActionError("The new password and the confirmation of the new password must be the same.");
			return INPUT;
		}
		
		user.setPassword(newPassword);
		CannedQueries.save(user);
		
		addActionMessage("Password for user '"+user.getName()+"' successfully changed.");
		
		return SUCCESS;
	}

	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}

	public String getNewPasswordConfirm() {
		return newPasswordConfirm;
	}
	
	public String save() {
		super.save();
		SessionUtils.syncSessionCacheIfRequired(getSession(), SessionConstants.USER_HANDLE, getModel());
		return SUCCESS;
	}
}