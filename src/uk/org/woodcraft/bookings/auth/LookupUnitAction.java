package uk.org.woodcraft.bookings.auth;

import java.util.List;

import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.opensymphony.xwork2.ActionSupport;

public class LookupUnitAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Key organisationKey;
	private List<Unit> units = null;

	public String execute(){
		
		if (organisationKey != null)
		{
			setUnits(CannedQueries.unitsForOrg(organisationKey, false));
		}
		
		return SUCCESS;
	}

	public List<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public void setOrganisation(Key organisationKey) {
		this.organisationKey = organisationKey;
	}

	public Key getOrganisation() {
		return organisationKey;
	}
	
}