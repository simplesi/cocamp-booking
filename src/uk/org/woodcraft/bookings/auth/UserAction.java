package uk.org.woodcraft.bookings.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.email.EmailUtils;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class UserAction extends ActionSupport implements ModelDriven<User>, Preparable, SessionAware{

	private static final long serialVersionUID = 1L;
	
	String email;
	User user = null;
	private Collection<User> userList;
	private String defaultOrgWebKey;
	private String defaultUnitWebKey;

	private Map<String, Object> session;

	public String edit() {
		if (user == null) return ERROR;
		SecurityModel.checkAllowed(Operation.READ, user);
		return INPUT;
	}
	
	// For edit, not for signup
	public String save() {
		SecurityModel.checkAllowed(Operation.WRITE, user);
		
		CannedQueries.save(user);
		return SUCCESS;
	}
	
	public String delete() {
		SecurityModel.checkAllowed(Operation.WRITE, user);
		CannedQueries.delete(user);
		return SUCCESS;
	}
	
	public String listAll() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setUserList(CannedQueries.allUsers());
		return SUCCESS;
	}

	public String listForOrg() {
		
		Organisation org = SessionUtils.getCurrentOrg();
		
		SecurityModel.checkAllowed(Operation.READ, org);
		setUserList(CannedQueries.allUsersForOrg(org));
		return SUCCESS;
	}
	
	public String listForUnit() {
		
		Unit unit = SessionUtils.getCurrentUnit();
		
		SecurityModel.checkAllowed(Operation.READ, unit);
		setUserList(CannedQueries.allUsersForUnit(unit));
		return SUCCESS;
	}
	
	public String processSignup(){
		
		if(user != null) {
			User existingUser = CannedQueries.getUserByEmail(user.getEmail());
			if (existingUser != null)
			{
				addActionError("A user with that email address already exists.");
				return INPUT;
			}
		}
		
		user.setAccessLevel(Accesslevel.AWAITING_EMAIL_CONFIRM);
		
		CannedQueries.save(user);
		sendUserConfirmEmail(user);
		
		addActionMessage("Check e-mail to confirm address");
		return "email-confirm";
	}

	// For the signup process
	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false);
		Organisation userAddedOrg = (Organisation)session.get(SessionConstants.SIGNUP_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
		}
		
		return orgs;
	}
	
	public String getDefaultOrgWebKey()
	{
		return defaultOrgWebKey;
	}

	// For the signup process
	public Collection<Unit> getAllUnits()
	{
		Collection<Unit> units = CannedQueries.allUnits(false);
		Unit userAddedUnit = (Unit)session.get(SessionConstants.SIGNUP_UNIT);
		if (userAddedUnit != null) {
			units.add(userAddedUnit);
		}
		return units;
	}
	
	public String getDefaultUnitWebKey()
	{
		return defaultUnitWebKey;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private void sendUserConfirmEmail(User user)
	{
		String subject = "Please confirm your email address";
		String body = "Someone signed up for CoCamp bookings using your email address. \n\n" 
					+ "If this was you, please go to the following link to confirm this email and enter the booking system. " 
					+ "If this was not you, please disregard this email. \n"
					+ buildUserConfirmUrl(user);
		System.out.println(body);
		EmailUtils.emailUser(user, subject, body);
	}
	
	private String buildUserConfirmUrl(User user)
	{
		// FIXME: Make this work in struts
		StringBuilder url = new StringBuilder("http://localhost:8888/cocamp-booking/confirmEmail?");
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("email", user.getEmail());
		params.put("hash", SignupUtils.generateEmailConfirmHash(user));	
		//UrlHelper.buildParametersString(params, url);
		
		// FIXME: Use UrlHelper, encode properly
		url.append("email=" + params.get("email"));
		url.append("&hash=" + params.get("hash"));		
		return url.toString();
	}

	@Override
	public void prepare() throws Exception {
		if (email != null && email.length() > 0 ) 
		{
			user = CannedQueries.getUserByEmail(email);	
		} 
		
		if (user == null)
		{
			user = new User();
			user.setIsNew(true);
		
			// Handle the defaults if someone's created their own org/unit
			Organisation userAddedOrg = (Organisation)session.get(SessionConstants.SIGNUP_ORG);
			if (userAddedOrg != null) {
				defaultOrgWebKey = userAddedOrg.getWebKey();
			}
			
			Unit userAddedUnit = (Unit)session.get(SessionConstants.SIGNUP_UNIT);
			if (userAddedUnit != null) {
				defaultUnitWebKey = userAddedUnit.getWebKey();
			}
		}
	}

	@Override
	public User getModel() {
		return user;
	}

	public void setUserList(Collection<User> userList) {
		this.userList = userList;
	}

	public Collection<User> getUserList() {
		return userList;
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}