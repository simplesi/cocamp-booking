package uk.org.woodcraft.bookings;

import java.lang.reflect.Method;
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

public class AllOfGivenClassReport extends CannedReportDynamicMethods {

	@SuppressWarnings("rawtypes")
	private static Map<String, Class> AVAILABLE_CLASSES = new HashMap<String, Class>();
	static {
		AVAILABLE_CLASSES.put("Units", Unit.class);
		AVAILABLE_CLASSES.put("Organisations", Organisation.class);
		AVAILABLE_CLASSES.put("Bookings", Booking.class);
		AVAILABLE_CLASSES.put("Financial Transactions", Transaction.class);
		AVAILABLE_CLASSES.put("Audit logs of data changes", AuditRecord.class);
	}
	
	@SuppressWarnings("rawtypes")
	private Class _clazz;
	

	@Override
	public List<Method> getReportedMethods(CannedReportLabel selectedReport) {
		if (AVAILABLE_CLASSES.containsKey(selectedReport.getTag()))
		{
			_clazz = AVAILABLE_CLASSES.get(selectedReport.getTag());
		} else 
			throw new IllegalArgumentException("Unrecognised report type "+ selectedReport.getTag());	
		
		return super.getReportedMethods(selectedReport);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getDataType() {
		return _clazz;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection getRows(CannedReportLabel selectedReport) {
		
		return CannedQueries.allEntriesForClass(_clazz);
	}

	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		List<CannedReportLabel> reports = new ArrayList<CannedReportLabel>();
		
		for(String name : AVAILABLE_CLASSES.keySet())
			reports.add(new CannedReportLabel(name, "All "+name, "All entries in the database for "+name));
		
		return reports;
	}
}
