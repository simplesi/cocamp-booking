package uk.org.woodcraft.bookings.persistence;

import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Village;

import com.google.appengine.api.datastore.Key;

public class VillageAction extends BasePersistenceAction<Village>{

	private static final long serialVersionUID = 1L;
	

	@SkipValidation
	public String list() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setModelList(CannedQueries.villagesForEvent(getCurrentEvent()));
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null)
		{
			setModel(CannedQueries.villageByKey(key));
		} else {
			setModel(new Village(getCurrentEvent()));
		}
	}
}
