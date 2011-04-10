package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.org.woodcraft.bookings.datamodel.AuditRecord;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class AllOfGivenClassReport <DataType> extends CannedReport<DataType> {

	@SuppressWarnings("rawtypes")
	private static Map<String, Class> AVAILABLE_CLASSES = new HashMap<String, Class>();
	static {
		AVAILABLE_CLASSES.put("Units", Unit.class);
		AVAILABLE_CLASSES.put("Organisations", Organisation.class);
		AVAILABLE_CLASSES.put("Bookings", Booking.class);
		AVAILABLE_CLASSES.put("Financial Transactions", Transaction.class);
		AVAILABLE_CLASSES.put("Audit logs of data changes", AuditRecord.class);
	}
	
	private Class<DataType> _clazz;
	
	@Override
	protected Class<DataType> getDataType() {
		return _clazz;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Collection<DataType> getRows(CannedReportLabel selectedReport) {
		
		if (AVAILABLE_CLASSES.containsKey(selectedReport.getTag()))
		{
			_clazz = AVAILABLE_CLASSES.get(selectedReport.getTag());
			return CannedQueries.allEntriesForClass(_clazz);
		}
		
		throw new IllegalArgumentException("Unrecognised report type "+ selectedReport.getTag());	
	}

	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		List<CannedReportLabel> reports = new ArrayList<CannedReportLabel>();
		
		for(String name : AVAILABLE_CLASSES.keySet())
			reports.add(new CannedReportLabel(name, "All "+name, "All entries in the database for "+name));
		
		return reports;
	}
}
