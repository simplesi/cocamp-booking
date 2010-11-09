package uk.org.woodcraft.bookings.datamodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void testCheckPassword() {
		String password = "hellopass";
		User user = new User("test@example.com", "name", password, Accesslevel.GLOBAL_ADMIN);
		
		assertTrue(user.checkPassword(password));
		assertFalse(user.checkPassword("otherpassword"));
		
	}
}
