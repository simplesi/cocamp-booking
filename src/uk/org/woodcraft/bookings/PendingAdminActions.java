package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.woodcraft.bookings.auth.LoginInterceptor;
import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.email.EmailUtils;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.Configuration;
import uk.org.woodcraft.bookings.utils.SystemClock;

public class PendingAdminActions extends SessionBasedAction {

	private static final long serialVersionUID = 4941769018916760167L;
	private static final Log log = LogFactory.getLog (LoginInterceptor.class);
	
	// TODO: These should use a map for efficiency 
	private Collection<String> selectedUser;
	private Collection<String> selectedOrg;
	private Collection<String> selectedUnit;
	
	private Clock clock = new SystemClock();
	
	
	public Collection<User> getPendingUsers()
	{
		return CannedQueries.allUnapprovedUsers();
	}
	
	public String bulkDeleteUsers()
	{
		return bulkChangeUsers(true);
	}
	
	public String bulkApproveUsers()
	{
		return bulkChangeUsers(false);
	}
	
	public String bulkChangeUsers(boolean delete)
	{
		SecurityModel.checkGlobalOperationAllowed(Operation.WRITE);
		

		Collection<User> userList = getPendingUsers();
		List<User> changedUsers = new ArrayList<User>();
		
		for(User user : userList)
		{
			if(getSelectedUser().contains(user.getEmail()))
			{
				changedUsers.add(user);
			}
		}
		
		if(delete)	{
			CannedQueries.deleteAll(changedUsers);
			
			addActionMessage(String.format("Deleted %d users", changedUsers.size()));
			
		} else {
			for(User user : changedUsers)
			{
				log.info(String.format("%s user %s (%s) approved",getRequestSource(), user.getEmail(), user.getName()));
				user.setApproved(true);
				if (user.getEmailValidated()) emailUserToNotifyApproved(user);
			}
			
			CannedQueries.save(changedUsers);
			addActionMessage(String.format("Approved %d users", changedUsers.size()));
		}
		
		return SUCCESS;
	}
	
	private void emailUserToNotifyApproved(User user)
	{
		String subject = "Woodcraft Folk Bookings - Account approved";
		String body = "Your access to Woodcraft Folk bookings has now been approved, and you can book people in to events. \n\n" 
					+ "You can visit the booking system here : " + Configuration.get().getProperty("baseurl")
					+ "\n\nThanks,The Bookings Team";
		System.out.println(body);
		EmailUtils.emailUser(user, subject, body);
	}
	
	public Collection<Organisation> getPendingOrgs()
	{
		return CannedQueries.allUnapprovedOrgs();
	}
	
	public String bulkDeleteOrgs()
	{
		return bulkChangeOrgs(true);
	}
	
	public String bulkApproveOrgs()
	{
		return bulkChangeOrgs(false);
	}
	
	public String bulkChangeOrgs(boolean delete)
	{
		SecurityModel.checkGlobalOperationAllowed(Operation.WRITE);		

		Collection<Organisation> orgList = getPendingOrgs();
		List<Organisation> changed = new ArrayList<Organisation>();
		
		for(Organisation org : orgList)
		{
			if(getSelectedOrg().contains(org.getWebKey()))
			{
				changed.add(org);
			}
		}
		
		if(delete)	{
			List<Organisation> changedAndPreconditionPassed = new ArrayList<Organisation>();
			List<String> errors = new ArrayList<String>();
			for(Organisation org : changed)
			{
				String error = org.getDeleteConditionError(clock);
				if (error.length() == 0)
					changedAndPreconditionPassed.add(org);
				else
					errors.add(error);
			}
			
			CannedQueries.deleteAll(changedAndPreconditionPassed);
			
			addActionMessage(String.format("Deleted %d orgs", changedAndPreconditionPassed.size()));
			if(errors.size() > 0)
			{
				addActionError(String.format("There were errors deleting %d orgs:", errors.size()));
				for(String error: errors)
					addActionError(error);
			}
			
		} else {
			for(Organisation org : changed)
			{
				log.info(String.format("%s org %s (%s) approved",getRequestSource(), org.getKey(), org.getName()));
				org.setApproved(true);
			}
			CannedQueries.save(changed);
			addActionMessage(String.format("Approved %d orgs", changed.size()));
		}
		
		return SUCCESS;
	}
	
	public Collection<Unit> getPendingUnits()
	{
		return CannedQueries.allUnapprovedUnits();
	}
	
	public String bulkDeleteUnits()
	{
		return bulkChangeUnits(true);
	}
	
	public String bulkApproveUnits()
	{
		return bulkChangeUnits(false);
	}
	
	public String bulkChangeUnits(boolean delete)
	{
		SecurityModel.checkGlobalOperationAllowed(Operation.WRITE);		

		Collection<Unit> unitList = getPendingUnits();
		List<Unit> changed = new ArrayList<Unit>();
		
		for(Unit unit : unitList)
		{
			if(getSelectedUnit().contains(unit.getWebKey()))
			{
				changed.add(unit);
			}
		}
		
		if(delete)	{
			List<Unit> changedAndPreconditionPassed = new ArrayList<Unit>();
			List<String> errors = new ArrayList<String>();
			for(Unit unit : changed)
			{
				String error = unit.getDeleteConditionError(clock);
				if (error.length() == 0)
					changedAndPreconditionPassed.add(unit);
				else
					errors.add(error);
			}
			
			CannedQueries.deleteAll(changedAndPreconditionPassed);
			
			addActionMessage(String.format("Deleted %d units", changedAndPreconditionPassed.size()));
			if(errors.size() > 0)
			{
				addActionError(String.format("There were errors deleting %d units.", errors.size()));
				for(String error: errors)
					addActionError(error);
			}
			
		} else {
			for(Unit unit : changed)
			{
				log.info(String.format("%s unit %s (%s) approved",getRequestSource(), unit.getKey(), unit.getName()));
				unit.setApproved(true);
			}
			CannedQueries.save(changed);
			addActionMessage(String.format("Approved %d units", changed.size()));
		}
		
		return SUCCESS;
	}


	public void setSelectedUser(Collection<String> userKey) {
		this.selectedUser = userKey;
	}

	public Collection<String> getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedOrg(Collection<String> selectedOrg) {
		this.selectedOrg = selectedOrg;
	}

	public Collection<String> getSelectedOrg() {
		return selectedOrg;
	}

	public void setSelectedUnit(Collection<String> selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	public Collection<String> getSelectedUnit() {
		return selectedUnit;
	}
}
