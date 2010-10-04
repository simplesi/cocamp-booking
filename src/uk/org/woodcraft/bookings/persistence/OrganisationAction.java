package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

import uk.org.woodcraft.bookings.auth.Operation;
import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Organisation;

import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class OrganisationAction extends ActionSupport implements ModelDriven<Organisation>, Preparable{

	private static final long serialVersionUID = 1L;
	
	String webKey;
	Organisation organisation = null;
	private Collection<Organisation> organisationList;

	@Override
	public Organisation getModel() {
		return organisation;
	}
	
	public void setWebKey(String key)
	{
		webKey = key;
	}
	
	public String edit() {
		if (organisation == null) return ERROR;
		SecurityModel.checkAllowed(Operation.READ, organisation);
		return INPUT;
	}
	
	public String save() {
		
		SecurityModel.checkAllowed(Operation.WRITE, organisation);
		CannedQueries.save(organisation);
		return SUCCESS;
	}
	
	public String delete() {
		SecurityModel.checkAllowed(Operation.WRITE, organisation);
		CannedQueries.delete(organisation);
		return SUCCESS;
	}
	
	public String list() {
		SecurityModel.checkGlobalOperationAllowed(Operation.READ);
		setOrganisationList(CannedQueries.allOrgs(true));
		return SUCCESS;
	}

	public void setOrganisationList(Collection<Organisation> organisationList) {
		this.organisationList = organisationList;
	}

	public Collection<Organisation> getOrganisationList() {
		return organisationList;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	@Override
	public void prepare() throws Exception {
		
		if (webKey != null && webKey.length() > 0 ) 
		{
			organisation = CannedQueries.orgByKey(KeyFactory.stringToKey(webKey));
		} else {
			organisation = new Organisation();
		}
	}

}
