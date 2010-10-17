package uk.org.woodcraft.bookings.persistence;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;

import com.google.appengine.api.datastore.Key;

public class BookingAction extends BasePersistenceAction<Booking>{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String save() {
		return super.save();
	}
	
	public String list() {
		
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.bookingsForUnit(getCurrentUnit(), getCurrentEvent()));
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

}
