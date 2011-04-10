package uk.org.woodcraft.bookings;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import uk.org.woodcraft.bookings.CannedReport.CannedReportLabel;

import com.opensymphony.xwork2.ActionSupport;

public class CannedReportAction extends ActionSupport {
	
	private static final long serialVersionUID = 4198648529700221398L;
	
	private InputStream stream;
	private String selectedReport;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String execute()
	{
		
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CannedReportMapping> getAvailableReports()
	{
		List<CannedReportMapping> reports = new ArrayList<CannedReportMapping>();
		
		reports.addAll(CannedReportMapping.buildReportMappingForReport(new BookedUnitsReport()));
		reports.addAll(CannedReportMapping.buildReportMappingForReport(new AllOfGivenClassReport()));
		
		return reports;
	}
	
	public InputStream getStream()
	{
		return stream;
	}
	
	public String getFileName()
	{
		return "QueryResult.xls";
	}
	
	public void setSelectedReport(String selectedReport) {
		this.selectedReport = selectedReport;
	}

	public String getSelectedReport() {
		return selectedReport;
	}
	
	static class CannedReportMapping <T>
	{
		private CannedReport<T> report;
		private CannedReport<T>.CannedReportLabel label;
		
		public CannedReportMapping(CannedReport<T> report, CannedReport<T>.CannedReportLabel label) {
			this.report = report;
			this.label = label;
		}

		public CannedReport<T> getReport() {
			return report;
		}
		
		public CannedReport<T>.CannedReportLabel getLabel() {
			return label;
		}	
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static <D, T extends CannedReport<D>> List<CannedReportMapping<T>> buildReportMappingForReport(T report)
		{
			List<CannedReportMapping<T>> mappings = new ArrayList<CannedReportMapping<T>>();
			for(CannedReportLabel label : report.getAvailableReports())
			{
				mappings.add(new CannedReportMapping(report, label));
			}
			return mappings;
		}
	}
	
}
