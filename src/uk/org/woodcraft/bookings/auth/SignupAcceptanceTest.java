package uk.org.woodcraft.bookings.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.opensymphony.xwork2.ActionSupport;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.BaseAppEngineTestCase;

public class SignupAcceptanceTest extends BaseAppEngineTestCase{

	@Test
	public void testSignupFlow()
	{
		String email = "newuser@example.com";
		String password = "testpassword";
		
		SignupAction signup = new SignupAction();
		try {
			signup.prepare();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		signup.setEmail(email);
		User user = signup.getModel();
		user.setEmail(email);
		user.setName("New user");
		user.setPassword(password);
		assertEquals(ActionSupport.SUCCESS, signup.processSignup());
		
		// Confirm email received
		// can't easily do this
		
		User testUser = CannedQueries.getUserByEmail(email);
		assertEquals(false, testUser.getEmailValidated());
		
		ConfirmEmailAction confirmAction = new ConfirmEmailAction();
		confirmAction.setEmail(email);
		confirmAction.setHash("badhash");
		
		// Confirm failed
		assertEquals (ActionSupport.INPUT, confirmAction.execute());
		testUser = CannedQueries.getUserByEmail(email);
		assertEquals(false, testUser.getEmailValidated());
		
		
		confirmAction.setHash(SignupUtils.generateEmailConfirmHash(testUser));
		assertEquals (ActionSupport.SUCCESS,confirmAction.execute());
		
		// Confirm succeeded	
		testUser = CannedQueries.getUserByEmail(email);
		assertEquals(true, testUser.getEmailValidated());
		assertEquals(false, testUser.getApproved());
		testUser = CannedQueries.getUserByEmail(email);
		assertEquals(Accesslevel.UNIT_ADMIN, testUser.getAccessLevel());
		assertTrue(testUser.checkPassword(password));
	}
	
	@Test
	public void testCantSignUpExistingUser()
	{
		String email = "newuser@example.com";
		String password = "testpassword";
		
		User existingUser = new User(email, "Existing user", password, Accesslevel.ORG_ADMIN);
		CannedQueries.save(existingUser);
		
		SignupAction signup = new SignupAction();
		try {
			signup.prepare();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		signup.setEmail(email);
		User user = signup.getModel();
		user.setEmail(email);
		user.setName("New user");
		user.setPassword(password);
		assertEquals(ActionSupport.INPUT, signup.processSignup());
		
		assertEquals("Existing user", CannedQueries.getUserByEmail(email).getName());
	}
}
