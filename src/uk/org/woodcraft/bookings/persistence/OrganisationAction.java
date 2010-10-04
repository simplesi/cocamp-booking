package uk.org.woodcraft.bookings.persistence;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import uk.org.woodcraft.bookings.datamodel.Organisation;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class OrganisationAction extends ActionSupport implements ModelDriven<Organisation>, Preparable, ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Key organisationKey;
	Organisation organisation;
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare() throws Exception {
		if(organisationKey == null)
		{
			organisation = new Organisation();
		} else {
			organisation = CannedQueries.orgByKey(organisationKey);
		}
	}

	@Override
	public Organisation getModel() {
		return organisation;
	}
	
	public void setOrganisationKey(String key)
	{
		organisationKey = KeyFactory.stringToKey(key);
	}
	
	public String saveOrUpdate() {
		CannedQueries.save(organisation);
		return SUCCESS;
	}
	
	public String delete() {
		return ERROR;
	}

}
