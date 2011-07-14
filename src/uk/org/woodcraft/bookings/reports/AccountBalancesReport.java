package uk.org.woodcraft.bookings.reports;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import uk.org.woodcraft.bookings.CannedReport;
import uk.org.woodcraft.bookings.CannedReportLabel;
import uk.org.woodcraft.bookings.accounts.AccountManager;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

public class AccountBalancesReport extends CannedReport {

	private Event currentEvent;
	
	public AccountBalancesReport(Event event) {
		this.currentEvent = event;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection getRows(CannedReportLabel selectedReport) {
		// FIXME: Assumes one event only
		
		List rows = new ArrayList();
		for(Unit unit : CannedQueries.allUnits(true))
		{
			AccountManager accountManager = new AccountManager(currentEvent, unit);
			rows.add(buildRow(accountManager, unit));
		}
		return rows;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List buildRow(AccountManager acctMgr, Unit unit)
	{
		List cols = new ArrayList();
		cols.add(unit.getOrganisation().getName());
		cols.add(unit.getName());
		cols.add(acctMgr.getTotalBookingsCount());
		cols.add(acctMgr.getTotalCost());
		cols.add(acctMgr.getPaymentTotal());
		cols.add(acctMgr.getBalance());
		return cols;
	}
	
	@Override
	protected List<String> getHeaders(CannedReportLabel selectedReport, List<Method> reportedMethods) {
		return Arrays.asList("Organisation", "Unit", "Total Bookings", "Total Cost", "Total Payments", "Balance");
	}

	@Override
	protected List<Method> getReportedMethods(CannedReportLabel selectedReport) {
		return null;
	}
	
	@Override
	protected List<CannedReportLabel> getAvailableReports() {
		return Arrays.asList(new CannedReportLabel("AccountBalance", "Account Balances", "Balance for each units accounts"));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Object> renderRow(List<Method> reportedMethods, Object data) {
		return (List<Object>)data;
	}

}
