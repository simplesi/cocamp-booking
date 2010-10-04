package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

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
		return INPUT;
	}
	
	public String save() {
		CannedQueries.save(organisation);
		return SUCCESS;
	}
	
	public String delete() {
		CannedQueries.delete(organisation);
		return SUCCESS;
	}
	
	public String list() {
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
		
		// Avoid re-work
		if (organisation != null) return;
		
		if (webKey != null) 
		{
			organisation = CannedQueries.orgByKey(KeyFactory.stringToKey(webKey));
		} else {
			organisation = new Organisation();
		}
	}

}
