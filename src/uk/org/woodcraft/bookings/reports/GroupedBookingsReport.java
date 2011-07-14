package uk.org.woodcraft.bookings.reports;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.woodcraft.bookings.BookingGrouper;
import uk.org.woodcraft.bookings.BookingGrouper.AllDatesAtEventTransformer;
import uk.org.woodcraft.bookings.BookingGrouper.BookingTransformer;
import uk.org.woodcraft.bookings.BookingGrouper.DietTransformer;
import uk.org.woodcraft.bookings.BookingGrouper.VillageTransformer;
import uk.org.woodcraft.bookings.CannedReport;
import uk.org.woodcraft.bookings.CannedReportLabel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class GroupedBookingsReport extends CannedReport {

	private static Map<String, List<? extends BookingTransformer>> GroupedReports = new HashMap<String, List<? extends BookingTransformer>>();
	static{
		GroupedReports.put("Bookings count by village", Arrays.asList(new VillageTransformer()));
		GroupedReports.put("Bookings count by village, diet", Arrays.asList(new VillageTransformer(), new DietTransformer()));
		GroupedReports.put("Bookings count by day, village, diet", Arrays.asList(new AllDatesAtEventTransformer(), new VillageTransformer(), new DietTransformer()));
	}
	
	private Event currentEvent;
	
	public GroupedBookingsReport(Event event) {
		this.currentEvent = event;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection getRows(CannedReportLabel selectedReport) {
		// FIXME: Assumes one event only
		
		List<BookingTransformer> transformers = (List<BookingTransformer>) GroupedReports.get(selectedReport.getTag());
		BookingGrouper grouper = new BookingGrouper(transformers);
		
		Map<List<Object>, List<Booking>> groupedBookings = grouper.group(CannedQueries.allBookings(true));
		
		List rows = new ArrayList();
		for(Map.Entry<List<Object>, List<Booking>> entry : groupedBookings.entrySet())
		{
			rows.add(buildRow(entry.getKey(), entry.getValue()));
		}
		return rows;
	}

	@SuppressWarnings({ "rawtypes" })
	private List buildRow(List<Object> group, List<Booking> bookings)
	{
		List<Object> cols = new ArrayList<Object>();
		cols.addAll(group);
		cols.add(bookings.size());
		
		return cols;
	}
	
	@Override
	protected List<String> getHeaders(CannedReportLabel selectedReport, List<Method> reportedMethods) {
		@SuppressWarnings("unchecked")
		List<BookingTransformer> transformers = (List<BookingTransformer>) GroupedReports.get(selectedReport.getTag());
		List<String> headers = new ArrayList<String>();
		for(BookingTransformer transformer : transformers)
			headers.add(transformer.columnName());
		
		headers.add("Count");
		
		return headers;
	}

	@Override
	protected List<Method> getReportedMethods(CannedReportLabel selectedReport) {
		return null;
	}
	
	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		List<CannedReportLabel> reports = new ArrayList<CannedReportLabel>();
		for (String reportName : GroupedReports.keySet())
			reports.add(new CannedReportLabel(reportName, reportName, reportName));
		
		return reports;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Object> renderRow(List<Method> reportedMethods, Object data) {
		return (List<Object>)data;
	}

}
