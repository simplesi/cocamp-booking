package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.ValidatableModelObject;

import com.google.appengine.api.datastore.Text;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@PersistenceCapable(detachable="true")
public class Organisation extends KeyBasedDataWithContactInfo implements NamedEntity, DeleteRestricted, ValidatableModelObject{
		
	private static final long serialVersionUID = 1L;

	public Organisation(String name, boolean approved) {
		this.name = name;
		this.approved = approved;
	}
	
	public Organisation() {
		// For JDO
	}

	@Persistent
	private String name;

	@Persistent
	private boolean approved;
	
	@Persistent
	private Text notes; // internal comments about the organisation, non-searchable
	
	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "1", trim = true, message = "Name cannot be empty")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getNotesString() {
		if(notes == null) return "";
		
		return notes.getValue();
		
	}
	
	public void setNotes(Text notes) {
		this.notes = notes;
	}
	
	public void setNotesString(String notes) {
		this.notes = new Text(notes);
	}
	


	public Collection<Unit> getUnits(boolean includeUnapproved) {
	
		return CannedQueries.unitsForOrg(this, includeUnapproved);
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public String getDeleteConditionError() {
		Collection<Unit> unitsForOrg = CannedQueries.unitsForOrg(this.getKeyCheckNotNull(), true);
		
		if (unitsForOrg.size() > 0)
		{
			return String.format("'%s' cannot be deleted as it still has %d units as part of it. These must be deleted first.", 
					getName(), unitsForOrg.size());
		}
		return "";
	}

	@Override
	public boolean deleteRequiresConfirmation() {
		return true;
	}
	
	@Override
	public Map<String, String> getValidationErrors() {
		Map<String,String> errors = null;
		if (getName() != null){
			Organisation clashingOrg = CannedQueries.orgByName(getName(), getKey());
			
			if (clashingOrg != null )
			{
				errors = new HashMap<String, String>(1);
				errors.put("name", "This name is already in the bookings system. Please use another" );
			}
		}
		return errors;
	}
	
}
