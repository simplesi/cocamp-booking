package uk.org.woodcraft.bookings.persistence;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.datamodel.TransactionType;

import com.google.appengine.api.datastore.Key;

public class TransactionAction extends BasePersistenceAction<Transaction>{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String save() {
		SecurityModel.checkIsAdminUser();
		return super.save();
	}
	
	@Override
	public String edit() {
		SecurityModel.checkIsAdminUser();
		return super.edit();
	}
	
	@SkipValidation
	public String list() {
		
		SecurityModel.checkIsAdminUser();
		setModelList(CannedQueries.transactionsForEvent(getCurrentEvent()));
		return SUCCESS;
	}
	
	@Override
	public String delete() {
		SecurityModel.checkIsAdminUser();
		return super.delete();
	}

	@Override
	public void prepare() throws Exception {
		
		Key key = getWebKeyAsKey();
		if (key != null)
		{
			setModel(CannedQueries.TransactionByKey(key));
		} else {
			setModel(new Transaction(getCurrentUnit().getKeyCheckNotNull(), getCurrentEvent().getKeyCheckNotNull(), getCurrentTime()));
		}
	}
	
	public List<TransactionType> getTransactionTypes()
	{
		return TransactionType.userUpdatableValues();
	}
}
