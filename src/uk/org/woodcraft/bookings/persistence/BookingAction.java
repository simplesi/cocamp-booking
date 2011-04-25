package uk.org.woodcraft.bookings.persistence;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Diet;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;

import com.google.appengine.api.datastore.Key;

public class BookingAction extends BasePersistenceAction<Booking>{

	private static final long serialVersionUID = 1L;
	
	private boolean showUnit = false;
	private boolean showOrg = false;
	
	private String confirmCancelBooking = "";
	private String cancelCancelBooking = "";
	private String confirmUnCancelBooking = "";
	private String cancelUnCancelBooking = "";
	
	@SkipValidation
	public String listForUnit() {		
		Unit unit = getCurrentUnit();		
		SecurityModel.checkAllowed(Operation.READ, unit);
		setModelList(CannedQueries.bookingsForUnit(unit, getCurrentEvent()));
		return SUCCESS;
	}
	
	@SkipValidation
	public String listForOrg() {
		Organisation org = getCurrentOrganisation();		
		SecurityModel.checkAllowed(Operation.READ, org);
		setModelList(CannedQueries.bookingsForOrg(org, getCurrentEvent()));
		showUnit = true;
		return SUCCESS;
	}
	
	@SkipValidation
	public String list() {	
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allBookings());
		showUnit = true;
		showOrg = true;
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null)
		{
			setModel(CannedQueries.bookingByKey(key));
		} else {
			setModel(new Booking(getCurrentUnit(), getCurrentEvent()));
		}
	}
	
	public String cancel() {
		if (getConfirmCancelBooking().length() == 0)
		{
			if (getCancelCancelBooking().length() != 0)
				return "cancel-cancel";
			else
				return "confirm-cancel";
		}
		
		return doCancel();
	}

	private String doCancel() {
		getModel().setCancellationDate(getCurrentTime());
		return save();
	}

	
	public String unCancel() {
		if (getConfirmUnCancelBooking().length() == 0)
		{
			if (getCancelUnCancelBooking().length() != 0)
				return "cancel-uncancel";
			else
				return "confirm-uncancel";
		}
		
		return doUnCancel();
	}
	
	private String doUnCancel() {
		getModel().setCancellationDate(null);
		return save();
	}
	
	public Date getEarliestDate() {
		return getCurrentEvent().getPublicEventStart();
	}
	public Date getLatestDate() {
		return getCurrentEvent().getPublicEventEnd();
	}
	public Collection<Diet> getDiets()
	{
		return Arrays.asList(Diet.values());
	}

	public boolean getShowUnit() {
		return showUnit;
	}
	public boolean getShowOrg() {
		return showOrg;
	}

	public String getConfirmCancelBooking() {
		return confirmCancelBooking;
	}

	public void setConfirmCancelBooking(String confirmCancelBooking) {
		this.confirmCancelBooking = confirmCancelBooking;
	}

	public String getCancelCancelBooking() {
		return cancelCancelBooking;
	}

	public void setCancelCancelBooking(String cancelCancelBooking) {
		this.cancelCancelBooking = cancelCancelBooking;
	}

	public String getConfirmUnCancelBooking() {
		return confirmUnCancelBooking;
	}

	public void setConfirmUnCancelBooking(String confirmUnCancelBooking) {
		this.confirmUnCancelBooking = confirmUnCancelBooking;
	}

	public String getCancelUnCancelBooking() {
		return cancelUnCancelBooking;
	}

	public void setCancelUnCancelBooking(String cancelUnCancelBooking) {
		this.cancelUnCancelBooking = cancelUnCancelBooking;
	}
}
