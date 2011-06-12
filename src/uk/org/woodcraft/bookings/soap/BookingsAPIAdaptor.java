package uk.org.woodcraft.bookings.soap;

import uk.org.woodcraft.bookings.soap.jaxws.Authenticate;
import uk.org.woodcraft.bookings.soap.jaxws.AuthenticateResponse;
import uk.org.woodcraft.bookings.soap.jaxws.MyVillageSignup;
import uk.org.woodcraft.bookings.soap.jaxws.MyVillageSignupResponse;

public class BookingsAPIAdaptor {

	private BookingsAPI api = new BookingsAPI();
	
	public AuthenticateResponse authenticate(Authenticate request)
	{
		String apiSitekey = request.getSiteKey();
		String apiUsername = request.getUsername();
		String apiPassword = request.getPassword();
		
		boolean success = api.authenticate(apiSitekey, apiUsername, apiPassword);
		
		AuthenticateResponse response = new AuthenticateResponse();
		response.setReturn(success);
		return response;
	}
	
	public MyVillageSignupResponse myVillageSignup(MyVillageSignup request)
	{
		String eventKey = request.getEventKey();
		String userEmail = request.getUserEmail();
		String userKey = request.getUserKey();
		String externalUsername = request.getExternalUsername();
		
		MyVillageResponse result = api.myVillageSignup(eventKey, userEmail, userKey, externalUsername);
		
		MyVillageSignupResponse response = new MyVillageSignupResponse();
		response.setResult(result);
		return response;
	}
}
