package uk.org.woodcraft.bookings.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.auth.SessionConstants;
import uk.org.woodcraft.bookings.datamodel.DeleteRestricted;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public abstract class BasePersistenceAction<ModelObject> extends ActionSupport implements ModelDriven<ModelObject>, Preparable, SessionAware, Validateable{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> session;
	String webKey;
	ModelObject modelObject = null;

	private Collection<ModelObject> modelObjectList;
	private String confirmDelete = "";
	private String cancelDelete = "";
	
	public void setModel(ModelObject modelObject) {
		this.modelObject = modelObject;
	}
	
	@Override
	@VisitorFieldValidator(message="",appendPrefix=false)
	public ModelObject getModel() {
		return modelObject;
	}
	
	public void setWebKey(String key)
	{
		webKey = key;
	}
	
	public String getWebKey()
	{
		return webKey;
	}
	
	public Key getWebKeyAsKey()
	{
		if (webKey == null || webKey.length() ==0) return null;
		return KeyFactory.stringToKey(webKey);
	}
	
	@SkipValidation
	public String edit() {
		if (modelObject == null) return ERROR;
		SecurityModel.checkAllowed(Operation.READ, modelObject);
		return INPUT;
	}
	
	@SkipValidation
	public String create() {
		return INPUT;
	}
	
	public String save() {
		
		SecurityModel.checkAllowed(Operation.WRITE, modelObject);
		CannedQueries.save(modelObject);
		return SUCCESS;
	}
	
	/**
	 * To be overridden to true for those that want a confirmed delete
	 * @return
	 */
	protected boolean deleteRequiresConfirmation()
	{
		return false;
	}
	
	protected boolean checkDeleteConditionsMet()
	{
		return true;
	}
	
	public String delete() {
		SecurityModel.checkAllowed(Operation.WRITE, modelObject);
		
		if(! (getModel() instanceof DeleteRestricted))
		{
			return deleteNoConfirm();
		}
		
		DeleteRestricted objectToDelete = (DeleteRestricted) getModel();
		
		String preDeleteCheckError = objectToDelete.getDeleteConditionError();
		if(preDeleteCheckError.length() > 0)
		{
			addActionError(preDeleteCheckError);
			return ERROR;
		}

		if(objectToDelete.deleteRequiresConfirmation() && getConfirmDelete().length() == 0)
		{
			if (getCancelDelete().length() > 0)
				return "cancel-delete";
			else
				return "confirm-delete";		
		} 
		
		return deleteNoConfirm();
		
	}
	
	private String deleteNoConfirm() {
		CannedQueries.delete(modelObject);
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	protected Map<String,Object> getSession() {
		return session;
	}
	
	protected Object getSessionObject(String key) {
		if (session == null) return null;
		
		return session.get(key);
	}
	
	protected void setSessionObject(String key, Serializable value) {
		if (session == null) return;
		
		session.put(key, value);
	}
	
	public abstract String list();
	
	public void setModelList(Collection<ModelObject> modelObjectList) {
		this.modelObjectList = modelObjectList;
	}

	public Collection<ModelObject> getModelList() {
		return modelObjectList;
	}
	
	public Unit getCurrentUnit()
	{
		return (Unit)getSessionObject(SessionConstants.CURRENT_UNIT);
	}
	
	public Organisation getCurrentOrganisation()
	{
		return (Organisation)getSessionObject(SessionConstants.CURRENT_ORG);
	}
	
	public Event getCurrentEvent()
	{
		return (Event)getSessionObject(SessionConstants.CURRENT_EVENT);
	}

	public User getCurrentUser()
	{
		return (User)getSessionObject(SessionConstants.USER_HANDLE);
	}

	public void setConfirmDelete(String confirmDelete) {
		this.confirmDelete = confirmDelete;
	}

	public String getConfirmDelete() {
		return confirmDelete;
	}

	public void setCancelDelete(String cancelDelete) {
		this.cancelDelete = cancelDelete;
	}

	public String getCancelDelete() {
		return cancelDelete;
	}
	
	public void validate()
	{
		Object model = getModel();
		if(model != null && model instanceof ValidatableModelObject)
		{
			// Delegate to the validation on the model itself
			Map<String,String> validationErrors = ((ValidatableModelObject) model).getValidationErrors();
			if (validationErrors != null)
			{
				for(String field : validationErrors.keySet())
					addFieldError(field, validationErrors.get(field));
			}
		}
	}
}
