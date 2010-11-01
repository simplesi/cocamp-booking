package uk.org.woodcraft.bookings.auth;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class PasswordUtils {

	public static String encodePassword(String password)
	{
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		String encryptedPassword = passwordEncryptor.encryptPassword(password);
		
		return encryptedPassword;
	}
	
	public static boolean checkPassword(String plainPassword, String encryptedPassword)
	{
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		return (passwordEncryptor.checkPassword(plainPassword, encryptedPassword));
	}
}
