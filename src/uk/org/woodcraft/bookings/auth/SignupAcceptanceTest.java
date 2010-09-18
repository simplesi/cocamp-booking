package uk.org.woodcraft.bookings.auth;

import org.junit.Test;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.testdata.BaseAppEngineTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SignupAcceptanceTest extends BaseAppEngineTestCase{

	@Test
	public void testSignupFlow()
	{
		String email = "newuser@example.com";
		String password = "testpassword";
		
		SignupAction signup = new SignupAction();
		signup.setEmail(email);
		signup.setName("New user");
		signup.setPassword(password);
		signup.execute();
		
		// Unclear how to do this
		//assertEquals(ActionSupport.SUCCESS, signup.get)
		
		// Confirm email received
		// can't easily do this
		
		User testUser = CannedQueries.getUserByEmail(email);
		assertEquals(Accesslevel.AWAITING_EMAIL_CONFIRM, testUser.getAccessLevel());
		
		ConfirmEmailAction confirmAction = new ConfirmEmailAction();
		confirmAction.setEmail(email);
		confirmAction.setHash("badhash");
		confirmAction.execute();
		
		// Confirm failed
		// unclear how to do this
		
		testUser = CannedQueries.getUserByEmail(email);
		assertEquals(Accesslevel.AWAITING_EMAIL_CONFIRM, testUser.getAccessLevel());
		
		
		confirmAction.setHash(SignupUtils.generateEmailConfirmHash(testUser));
		confirmAction.execute();
		
		// Confirm succeeded
		// unclear how to do this
		
		testUser = CannedQueries.getUserByEmail(email);
		assertEquals(Accesslevel.UNASSIGNED, testUser.getAccessLevel());
		assertTrue(testUser.checkPassword(password));
		
	}
	
}
