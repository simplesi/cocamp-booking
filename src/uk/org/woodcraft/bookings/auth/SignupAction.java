package uk.org.woodcraft.bookings.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.email.EmailUtils;
import uk.org.woodcraft.bookings.persistence.BasePersistenceAction;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.Configuration;

public class SignupAction extends BasePersistenceAction<User>{

	private static final long serialVersionUID = 1L;
	
	private String email;
	private Key organisationKey;
	private Key unitKey;
	
	
	// not used for signup
	public String save() {
		throw new IllegalStateException("Save not supported for SignupAction" );
	}
	
	@SkipValidation
	public String noValidationEntry()
	{
		return INPUT;
	}
	
	public String processSignup(){
		
		if(getModel() != null) {
			User existingUser = CannedQueries.getUserByEmail(getModel().getEmail());
			if (existingUser != null)
			{
				addActionError("A user with that email address already exists.");
				return INPUT;
			}
		}
		
		getModel().setAccessLevel(Accesslevel.UNIT_ADMIN);
		
		CannedQueries.save(getModel());
		sendUserConfirmEmail(getModel());
		
		addActionMessage("Check e-mail to confirm address");
		return SUCCESS;
	}

	public Collection<Organisation> getAllOrgs()
	{
		Collection<Organisation> orgs = CannedQueries.allOrgs(false, false);
		Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ADDED_ORG);
		if (userAddedOrg != null) {
			orgs.add(userAddedOrg);
		}
		
		return orgs;
	}

	public Collection<Unit> getUnitsForCurrentOrg()
	{
		Organisation currentOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ORG);
		
		Collection<Unit> units = new ArrayList<Unit>();
		if (currentOrg != null) units = CannedQueries.unitsForOrg(currentOrg.getKey(), false, false);
		Unit userAddedUnit = (Unit)getSessionObject(SessionConstants.SIGNUP_ADDED_UNIT);
		if (userAddedUnit != null && currentOrg != null && userAddedUnit.getOrganisationKey().equals(currentOrg.getKey())) {
			units.add(userAddedUnit);
		}
		return units;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private void sendUserConfirmEmail(User user)
	{
		
		String subject = "Woodcraft Folk Bookings - Activate your account";
		String body = "Someone signed up for Woodcraft Folk Bookings using your email address. \n\n" 
					+ "If this was you, please go to the following link to confirm this email. " 
					+ "If this was not you, please disregard this email. \n"
					+"Once you have confirmed your email address, your account will be checked and approved.  Please note that this approval is checked by our staff so there may be a delay, especially if you have signed up outside of office hours.\n\n"
					+ buildUserConfirmData(user)
					+ "\n\nThanks,\nThe Bookings Team";
		System.out.println(body);
		EmailUtils.emailUser(user, subject, body);
	}
	
	
	private String buildUserConfirmData(User user)
	{
		String baseUrl = Configuration.get().getProperty("baseurl");
		
		String confirmUrl = baseUrl + "signup/confirmEmail";
		StringBuilder url = new StringBuilder( confirmUrl );
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("email", user.getEmail());
		params.put("hash", SignupUtils.generateEmailConfirmHash(user));	
		//UrlHelper.buildParametersString(params, url);
		
		// FIXME: Use UrlHelper
		try {
			url.append("?");
			url.append("email=" + URLEncoder.encode(params.get("email"), "UTF-8"));
			url.append("&hash=" + URLEncoder.encode(params.get("hash"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}		
		
		return String.format("%s\n\nAlternatively, please visit %s and enter your email address and the following code: %s", 
							url.toString(), confirmUrl, params.get("hash"));
	}

	@Override
	public void prepare() throws Exception {
		if (email != null && email.length() > 0 ) 
		{
			setModel(CannedQueries.getUserByEmail(email));	
		} 
		
		// Default to user added values
		
		if (getModel() == null)
		{
			User user = new User();
			user.setIsNew(true);
			
			Organisation selectedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ORG);
			if (selectedOrg != null)
				user.setOrganisationKey(selectedOrg.getKey());
			
			Unit selectedUnit = (Unit)getSessionObject(SessionConstants.SIGNUP_UNIT);
			if (selectedUnit != null)
				user.setUnitKey(selectedUnit.getKey());
			
			setModel(user);
		}
	}

	@Override
	public String list() {
		throw new IllegalStateException("List method of signupAction not supported");
	}

	public void setOrganisationWebKey(String organisationWebKey) {
		this.organisationKey = KeyFactory.stringToKey(organisationWebKey);
	}

	public String getOrganisationWebKey() {
		if (organisationKey == null) return null;
		return KeyFactory.keyToString(organisationKey);
	}
	
	public Organisation getOrganisation() {
		return CannedQueries.orgByKey(organisationKey);
	}

	public void setUnitWebKey(String unitWebKey) {
		this.unitKey = KeyFactory.stringToKey(unitWebKey);
	}
	
	public Unit getUnit() {
		return CannedQueries.unitByKey(unitKey);
	}

	public String getUnitWebKey() {
		if (unitKey == null) return null;
		return KeyFactory.keyToString(unitKey);
	}
	
	@SkipValidation
	public String signupOrg() {
		if (organisationKey == null) return INPUT;
		Organisation org = CannedQueries.orgByKey(organisationKey);
		if (org == null) return INPUT;
		setSessionObject(SessionConstants.SIGNUP_ORG, org);
		return SUCCESS;
	}
	
	@SkipValidation
	public String signupUnit() {
		if (unitKey == null) return INPUT;
		Unit unit = CannedQueries.unitByKey(unitKey);
		if (unit == null) return INPUT;
		
		setSessionObject(SessionConstants.SIGNUP_UNIT, unit);
		
		return SUCCESS;
	}

}