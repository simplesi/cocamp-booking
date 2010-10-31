package uk.org.woodcraft.bookings.dev;

import com.opensymphony.xwork2.ActionSupport;

/**
 * The set of steps to run prior to deployment, eg building indexes
 * @author simon
 *
 */
public class PreDeploymentAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;

	public String execute()
	{
		CreateTestDataAction createData = new CreateTestDataAction();
		createData.execute();
		
		RunQueriesAction runQueries = new RunQueriesAction();
		runQueries.execute();
		
		return SUCCESS;
	}
	
}
