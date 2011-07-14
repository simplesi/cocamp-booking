package uk.org.woodcraft.bookings.auth;

import java.util.Collection;

import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.Key;
import com.opensymphony.xwork2.ActionSupport;

public class LookupUnitAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Key organisationKey;
	private Collection<Unit> units = null;

	public String execute(){
		
		if (organisationKey != null)
		{
			setUnits(CannedQueries.unitsForOrg(organisationKey, false, false));
		}
		
		return SUCCESS;
	}

	public Collection<Unit> getUnits()
	{
		return units;
	}

	public void setUnits(Collection<Unit> units) {
		this.units = units;
	}

	public void setOrganisation(Key organisationKey) {
		this.organisationKey = organisationKey;
	}

	public Key getOrganisation() {
		return organisationKey;
	}
	
}