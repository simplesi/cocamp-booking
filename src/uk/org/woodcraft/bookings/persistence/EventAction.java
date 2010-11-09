package uk.org.woodcraft.bookings.persistence;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.Key;

public class EventAction extends BasePersistenceAction<Event>{

	private static final long serialVersionUID = 1L;
	
	
	public String list() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.allEvents(true));
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null)
		{
			setModel(CannedQueries.eventByKey(key));
		} else {
			setModel(new Event());
		}
	}
	
	@Override
	public String save() {
		String result = super.save();
		SessionUtils.syncSessionCacheIfRequired(getSession(), SessionConstants.CURRENT_EVENT, getModel());
		
		return result;
	}

}
