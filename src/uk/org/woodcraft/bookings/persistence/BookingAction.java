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
		return SUCCESS;
	}
	
	@SkipValidation
	public String list() {	
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allBookings());
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
}
