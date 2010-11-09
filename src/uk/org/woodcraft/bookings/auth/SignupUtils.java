package uk.org.woodcraft.bookings.auth;

import org.jasypt.digest.StandardStringDigester;

import uk.org.woodcraft.bookings.datamodel.User;

public class SignupUtils {
	public static String generateEmailConfirmHash(User user)
	{
		//FIXME: This should really use its own salt, but this is sufficient for now, since the encrypted password uses a salt
		StandardStringDigester digestor = new StandardStringDigester();
		return digestor.digest(user.getName() + user.getPasswordEncrypted());	
	}
	
	public static boolean checkEmailHash(User user, String hash)
	{
		StandardStringDigester digestor = new StandardStringDigester();
		return digestor.matches(user.getName() + user.getPasswordEncrypted(), hash);	
	}
}
