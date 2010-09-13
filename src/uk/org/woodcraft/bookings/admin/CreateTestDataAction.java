package uk.org.woodcraft.bookings.admin;

import com.opensymphony.xwork2.ActionSupport;
import uk.org.woodcraft.bookings.testdata.TestFixture;

public class CreateTestDataAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public String setUp(){
		TestFixture.BASIC_DATA.setUp();
		addActionMessage("Successfully created test data");
		return SUCCESS;
	}
}