package uk.org.woodcraft.bookings.admin;

import java.io.IOException;
import javax.servlet.http.*;

import uk.org.woodcraft.bookings.testdata.TestFixture;

@SuppressWarnings("serial")
public class CreateTestDataServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		TestFixture.BASIC_DATA.setUp();
		
		resp.setContentType("text/plain");
		resp.getWriter().println("Successfully created test data");
	}
}
