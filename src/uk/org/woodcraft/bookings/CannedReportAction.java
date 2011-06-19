package uk.org.woodcraft.bookings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.org.woodcraft.bookings.persistence.SessionBasedAction;

public class CannedReportAction extends SessionBasedAction {
	
	private static final long serialVersionUID = 4198648529700221398L;
	
	private InputStream stream;
	private String selectedReport;
	
	public String execute()
	{
		if (selectedReport == null) return INPUT;
		
		for(CannedReportMapping mapping : getAvailableReports())
		{
			if (mapping.getLabel().getTag().equals(selectedReport))
			{
				stream = new ByteArrayInputStream(mapping.getReport().getResults(mapping.getLabel()));
				
				return SUCCESS;
			}
		}
		
		addActionError("Unable to find canned report "+selectedReport+" - please try again");
		
		return INPUT;
	}

	public List<CannedReportMapping> getAvailableReports()
	{
		List<CannedReportMapping> reports = new ArrayList<CannedReportMapping>();
		
		reports.addAll(CannedReportMapping.buildReportMappingForReport(new BookedUnitsReport()));
		reports.addAll(CannedReportMapping.buildReportMappingForReport(new AllOfGivenClassReport()));
		reports.addAll(CannedReportMapping.buildReportMappingForReport(new AccountBalancesReport(getCurrentEvent())));
		return reports;
	}
	
	public InputStream getStream()
	{
		return stream;
	}
	
	public String getFileName()
	{
		return selectedReport;
	}
	
	public void setSelectedReport(String selectedReport) {
		this.selectedReport = selectedReport;
	}

	public String getSelectedReport() {
		return selectedReport;
	}
	
	static class CannedReportMapping
	{
		private CannedReport report;
		private CannedReportLabel label;
		
		public CannedReportMapping(CannedReport report, CannedReportLabel label) {
			this.report = report;
			this.label = label;
		}

		public CannedReport getReport() {
			return report;
		}
		
		public CannedReportLabel getLabel() {
			return label;
		}	
		
		public static  List<CannedReportMapping> buildReportMappingForReport(CannedReport report)
		{
			List<CannedReportMapping> mappings = new ArrayList<CannedReportMapping>();
			for(CannedReportLabel label : report.getAvailableReports())
			{
				mappings.add(new CannedReportMapping(report, label));
			}
			return mappings;
		}
	}
}
