package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.DeleteRestricted;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

public abstract class BasePersistenceAction<ModelObject> extends SessionBasedAction implements ModelDriven<ModelObject>, Preparable, Validateable{
	
	private static final long serialVersionUID = 1L;
	
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
		
		String preDeleteCheckError = objectToDelete.getDeleteConditionError(getClock());
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
	
	public abstract String list();
	
	public void setModelList(Collection<ModelObject> modelObjectList) {
		this.modelObjectList = modelObjectList;
	}

	public Collection<ModelObject> getModelList() {
		return modelObjectList;
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
	
	public boolean getCanAssignVillage() {
		return SecurityModel.isAdminUser(this);
	}
}
