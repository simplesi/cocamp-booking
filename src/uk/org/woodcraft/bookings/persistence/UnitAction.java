package uk.org.woodcraft.bookings.persistence;

import java.util.Collection;

import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.google.appengine.api.datastore.KeyFactory;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class UnitAction extends ActionSupport implements ModelDriven<Unit>, Preparable{

	private static final long serialVersionUID = 1L;
	
	String webKey;
	Unit unit = null;
	private Collection<Unit> unitList;

	@Override
	public Unit getModel() {
		return unit;
	}
	
	public void setWebKey(String key)
	{
		webKey = key;
	}
	
	public String edit() {
		if (unit == null) return ERROR;
		return INPUT;
	}
	
	public String save() {
		CannedQueries.save(unit);
		return SUCCESS;
	}
	
	public String delete() {
		CannedQueries.delete(unit);
		return SUCCESS;
	}
	
	public String list() {
		setUnitList(CannedQueries.allUnits(true));
		return SUCCESS;
	}

	public void setUnitList(Collection<Unit> unitList) {
		this.unitList = unitList;
	}

	public Collection<Unit> getUnitList() {
		return unitList;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Override
	public void prepare() throws Exception {
		
		if (webKey != null && webKey.length() > 0 ) 
		{
			unit = CannedQueries.unitByKey(KeyFactory.stringToKey(webKey));
		} else {
			unit = new Unit(SessionUtils.getCurrentOrg());
		}
	}

}
