package uk.org.woodcraft.bookings.soap;

import java.util.Collection;

import javax.jws.WebMethod;
import javax.jws.WebService;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.AppSetting;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@WebService
public class BookingsAPI {

	@WebMethod
	public boolean authenticate(String apiSitekey, String apiUsername, String apiPassword)
	{
		String actualApiKey = AppSetting.get(AppSetting.SITE_API_KEY);
		
		if (!apiSitekey.equals(actualApiKey))
			return false;
		
		// Note apiUsername is an email for these purposes
	    User user = CannedQueries.getUserByEmail(apiUsername);
	    if (user != null && user.getAccessLevel().equals(Accesslevel.API)
	    		&& user.checkPassword(apiPassword))
	    {
	    	return true;
	    }
		return false;
	}
	
	@WebMethod
	public MyVillageResponse myVillageSignup(String eventWebKey, String userEmailString, String userKey,
												String externalUsername)
	{
		Key eventKey = KeyFactory.stringToKey(eventWebKey);
		Event event = CannedQueries.eventByKey(eventKey);
		
		if (event == null)
			return new MyVillageResponse("Event with key '"+eventWebKey+"' not found");
		
		Email userEmail = new Email(userEmailString);
		Collection<Booking> bookings = CannedQueries.bookingsForEventAndEmail(eventKey, userEmail);
		
		for(Booking booking : bookings)
		{
			if (booking.getMyvillageAuthKey().equals(userKey))
			{
				// We have a match		
				Village village = CannedQueries.villageByKey(booking.getVillageKey());
				if (village == null)
					return new MyVillageResponse("Booking for user located, but no village mapping found. A booking must be assigned to a village before you can use My Village.");
				
				booking.setMyVillageUsername(externalUsername);
				CannedQueries.save(booking);
				
				return new MyVillageResponse(booking.getName(), village.getName(), 
						KeyFactory.keyToString(village.getKeyCheckNotNull()));	
			}
		}	
		
		return new MyVillageResponse("No matching bookings found. Please check email and key and try again");
	}
}