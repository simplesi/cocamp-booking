package uk.org.woodcraft.bookings.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.email.EmailUtils;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.opensymphony.xwork2.ActionSupport;

public class SignupAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String email;
	private String password;
	private String name;

	public String execute(){
		
		User user = new User(email, name, password, Accesslevel.AWAITING_EMAIL_CONFIRM);
		CannedQueries.save(user);
		
		sendUserConfirmEmail(user);
		
		addActionMessage("Check e-mail to confirm address");
		return SUCCESS;
	}

	public String preSignup()
	{
		// Nothing to do
		return SUCCESS;
	}
	
	public Collection<Organisation> getAllOrgs()
	{
		return CannedQueries.allOrgs(false);
	}

	public Collection<Unit> getAllUnits()
	{
		return CannedQueries.allUnits(false);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}