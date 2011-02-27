package uk.org.woodcraft.bookings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.opensymphony.xwork2.ActionSupport;

public class CannedReportAction extends ActionSupport {
	
	private static final long serialVersionUID = 4198648529700221398L;
	
	private InputStream stream;
	
	public String execute()
	{
	
		CannedReport report = new BookedUnitsReport();
		
		
		stream = new ByteArrayInputStream(report.getResults());
				
		return SUCCESS;
	}
	
	public InputStream getStream()
	{
		return stream;
	}
	
	public String getFileName()
	{
		return "QueryResult.xls";
	}
	
}
