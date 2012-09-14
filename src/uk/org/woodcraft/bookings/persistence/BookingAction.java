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
import uk.org.woodcraft.bookings.datamodel.Village;

import com.google.appengine.api.datastore.Key;

public class BookingAction extends BasePersistenceAction<Booking>{

	private static final long serialVersionUID = 1L;
	
	private boolean showUnit = false;
	private boolean showOrg = false;
	
	private String confirmCancelBooking = "";
	private String cancelCancelBooking = "";
	private String confirmUnCancelBooking = "";
	private String cancelUnCancelBooking = "";
	private String confirmUnlockBooking = "";
	private String cancelUnlockBooking = "";
	
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
			setModel(Booking.create(getCurrentUnit(), getCurrentEvent()));
		}
	}
	
	@SkipValidation
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

	@SkipValidation
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
	
	@SkipValidation
	public String unlock() {
		if (getConfirmUnlockBooking().length() == 0)
		{
			if (getCancelUnlockBooking().length() != 0)
				return "cancel-unlock";
			else
				return "confirm-unlock";
		}
		
		return doUnlock();
	}
	
	private String doUnlock() {
		getModel().setBookingUnlockDate(getCurrentTime());
		return save();
	}
	
	public Date getEarliestDate() {
		return getCurrentEvent().getPublicEventStart();
	}
	public Date getLatestDate() {
		return getCurrentEvent().getPublicEventEnd();
	}
	
	public Date getEditCutoffDate() {
		if (getModel().getBookingUnlockDate() != null){
			Date cutoff = getModel().getBookingUnlockDate();
			cutoff.setTime(cutoff.getTime() + (60 * 60 * 1000 * 24));
			return cutoff;
		}
		
		// TODO: This can potentially end up displaying a date in the past for "This booking can be edited until..."
		//  	 If this is a new booking after the deadline. Need to better handle that case.
		return getCurrentEvent().getBookingAmendmentDeadline();	
	}
	
	public boolean getIsEditable() {
		if (getModel().getKey() == null) return true;
		
		if (getCurrentTime().before(getCurrentEvent().getBookingAmendmentDeadline()) ) return true;
		if (getCurrentTime().after(getCurrentEvent().getBookingSystemLocked()) ) return false;
		
		
		// If the booking was unlocked, handle the unlock window
		Date bookingUnlockDate = getModel().getBookingUnlockDate();
		if (bookingUnlockDate == null) return false;
		
		long millisBetween = getCurrentTime().getTime() - bookingUnlockDate.getTime();
		double hours = Math.floor((double)millisBetween / (60 * 60 * 1000 * 24));
		
		// Can edit up to 24 hours after unlock
		return (hours <= 24);
	}
	


	public Collection<Village> getVillages()
	{
		return CannedQueries.villagesForEvent(getCurrentEvent());
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

	public String getConfirmUnlockBooking() {
		return confirmUnlockBooking;
	}

	public void setConfirmUnlockBooking(String confirmUnlockBooking) {
		this.confirmUnlockBooking = confirmUnlockBooking;
	}

	public String getCancelUnlockBooking() {
		return cancelUnlockBooking;
	}

	public void setCancelUnlockBooking(String cancelUnlockBooking) {
		this.cancelUnlockBooking = cancelUnlockBooking;
	}
}
