package uk.org.woodcraft.bookings.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
	
	// not used for signup
	public String save() {
		throw new IllegalStateException("Save not supported for SignupAction" );
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
		return "email-confirm";
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	private void sendUserConfirmEmail(User user)
	{
		
		String subject = "Co-Camp Booking - Activate your account";
		String body = "Someone signed up for CoCamp bookings using your email address. \n\n" 
					+ "If this was you, please go to the following link to confirm this email and enter the booking system. " 
					+ "If this was not you, please disregard this email. \n"
					+ buildUserConfirmData(user)
					+ "\n\nThanks,The Co-Camp Team";
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
		
		if (getModel() == null)
		{
			User user = new User();
			user.setIsNew(true);
		
			// Handle the defaults if someone's created their own org/unit	
			Unit userAddedUnit = (Unit)getSessionObject(SessionConstants.SIGNUP_UNIT);
			if (userAddedUnit != null) {
				// use the org of the unit if possible so we get consistency
				user.setOrganisationKey(userAddedUnit.getOrganisationKey());
				user.setUnitKey(userAddedUnit.getKey());
				setModel(user);
				return;
			}
			
			Organisation userAddedOrg = (Organisation)getSessionObject(SessionConstants.SIGNUP_ORG);
			if (userAddedOrg != null) {
				user.setOrganisationKey(userAddedOrg.getKey());
			}
			
			setModel(user);	
		}
	}

	@Override
	public String list() {
		throw new IllegalStateException("List method of signupAction not supported");
	}

}