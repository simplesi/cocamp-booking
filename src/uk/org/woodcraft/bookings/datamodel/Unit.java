package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.ValidatableModelObject;
import uk.org.woodcraft.bookings.utils.Clock;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

/**
 * This class should really be UnitBooking, since it's an event-specific mapping, even if it didn't 
 * start out that way. At some point, this will need to be factored out into an Unit and a UnitBooking.
 * But this is hard to do now given the proximity to CoCamp
 * 
 * @author simon
 *
 */
@PersistenceCapable(detachable="true")
public class Unit extends KeyBasedDataWithContactInfo implements NamedEntity, DeleteRestricted, ValidatableModelObject{
	
	private static final long serialVersionUID = 1L;

	public Unit() {
		// For JDO
	}
	
	public Unit(Organisation org)
	{
		this.organisationKey = org.getKeyCheckNotNull();
	}
	
	public Unit(String name, Organisation org, boolean approved) {
		this.name = name;
		this.organisationKey = org.getKeyCheckNotNull();
		this.approved = approved;
	}
	
	@Persistent
	private String name;
	
	@Persistent
	private Key organisationKey;
	
	@Persistent
	private Key villageKey = null;
	
	@Persistent
	private Text notes;
	
	@Persistent
	private boolean approved;
	
	/*
	Estimate: how many Woodchips will your district bring to camp?
			Estimate: how many Elfins will your district bring to camp?
			Estimate: how many Pioneers will your district bring to camp?
			Estimate: how many Venturers will your district bring to camp?
			Estimate: how many DFs will your district bring to camp?
			Estimate: how many Adults will your district bring to camp?
*/
	
//	@Persistent
//	private int estimateWoodchip=0;
//	
//	@Persistent
//	private int estimateElfin=0;
//	
//	@Persistent
//	private int estimatePioneer=0;
//	
	@Persistent
	private int estimateVenturer=0;
	
	@Persistent
	private int estimateDF=0;
	
	@Persistent
	private int estimateAdult=0;
	

	
/*	
	What equipment will your district provide? [someone help develop this further!]
			1.	Kitchen
			2.	Tables
			3.	Benches / chairs
			4.	Lighting
			5.	Other ________________________
	*/	
	@Persistent
	private boolean equipmentKitchen=false;
	@Persistent
	private boolean equipmentTables=false;
	@Persistent
	private boolean equipmentBenches=false;
	@Persistent
	private boolean equipmentLighting=false;
	@Persistent
	private Text equipmentOther;
	
/*
	What large canvas will your district provide (for use in village)?
			1.	Village marquee (for approx 90 campers)
			2.	Kitchen tent
			3.	Store tent
			4.	Other _____________________
*/
	@Persistent
	private boolean canvasMarquee=false;
	@Persistent
	private boolean canvasKitchen=false;
	@Persistent
	private boolean canvasStore=false;
	@Persistent
	private Text canvasOther;
	
/*
	What large canvas can your district offer (for use in town centre etc.)?
	1.	Town Marquee (for approx 500 people)
	2.	Activity Marquee
	3.	Cafe Marquee
	4.	Other _______________________
*/
	@Persistent
	private boolean largeCanvasTown=false;
	@Persistent
	private boolean largeCanvasActivity=false;
	@Persistent
	private boolean largeCanvasCafe=false;
	@Persistent
	private Text largeCanvasOther;
	
	
/*	Is there another district you would like to be in a village with?
	Do you have a delegation in mind that you wish to host?
*/
	@Persistent
	private String villagePartner;
	@Persistent
	private Text delegation;
	                                					
	
	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "5", trim = true, message = "Name is required for unit")
	@CannedReportColumn(priority = 1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SkipInCannedReports
	public Key getOrganisationKey() {
		return organisationKey;
	}
	
	public void setOrganisationWebKey(String webKey) {
		organisationKey = KeyFactory.stringToKey(webKey);
	}
	
	@SkipInCannedReports
	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "1", trim = true, message = "Organisation must be specified")
	public String getOrganisationWebKey() {
		if (organisationKey == null) return null;
		return KeyFactory.keyToString(organisationKey);
	}

	@CannedReportColumn(priority = 2)
	public Organisation getOrganisation() {
		return CannedQueries.orgByKey(getOrganisationKey());
	}
	
	public void setOrganisation(Organisation organisation) {
		this.organisationKey = organisation.getKeyCheckNotNull();
	}

	@CannedReportColumn(priority = 21)
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

	public boolean isApproved() {
		return approved;
	}


	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public Collection<Booking> getBookings(Event event)
	{
		return (CannedQueries.bookingsForUnit(this, event));
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public String getDeleteConditionError(Clock clock) {
		Collection<Booking> bookingsForUnit = CannedQueries.bookingsForUnitAllEvents(this);
		
		if (bookingsForUnit.size() > 0)
		{
			return String.format("'%s' cannot be deleted as it still has %d bookings registered (possibly across different events). These must be deleted first.", 		
					getName(), bookingsForUnit.size());
		}
		return "";
	}

	@Override
	public boolean deleteRequiresConfirmation() {
		return true;
	}

	@SkipInCannedReports
	@Override
	public Map<String, String> getValidationErrors() {
		Map<String,String> errors = null;
		if (getName() != null){
			Unit clashingUnit = CannedQueries.unitByName(getName(), getOrganisationKey(), getKey());
			
			if (clashingUnit != null )
			{
				errors = new HashMap<String, String>(1);
				errors.put("name", "This name is already in the bookings system. Please use another" );
			}
		}
		return errors;
	}

//	@CannedReportColumn(priority = 31)
//	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
//	public int getEstimateWoodchip() {
//		return estimateWoodchip;
//	}
//
//	public void setEstimateWoodchip(int estimateWoodchip) {
//		this.estimateWoodchip = estimateWoodchip;
//	}
//
//	@CannedReportColumn(priority = 32)
//	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
//	public int getEstimateElfin() {
//		return estimateElfin;
//	}
//
//	public void setEstimateElfin(int estimateElfin) {
//		this.estimateElfin = estimateElfin;
//	}
//
//	@CannedReportColumn(priority = 33)
//	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
//	public int getEstimatePioneer() {
//		return estimatePioneer;
//	}
//
//	public void setEstimatePioneer(int estimatePioneer) {
//		this.estimatePioneer = estimatePioneer;
//	}

	@CannedReportColumn(priority = 34)
	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateVenturer() {
		return estimateVenturer;
	}

	public void setEstimateVenturer(int estimateVenturer) {
		this.estimateVenturer = estimateVenturer;
	}

	@CannedReportColumn(priority = 35)
	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateDF() {
		return estimateDF;
	}

	public void setEstimateDF(int estimateDF) {
		this.estimateDF = estimateDF;
	}

	@CannedReportColumn(priority = 36)
	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateAdult() {
		return estimateAdult;
	}

	public void setEstimateAdult(int estimateAdult) {
		this.estimateAdult = estimateAdult;
	}

	@CannedReportColumn(priority = 40)
	public boolean getEquipmentKitchen() {
		return equipmentKitchen;
	}

	public void setEquipmentKitchen(boolean equipmentKitchen) {
		this.equipmentKitchen = equipmentKitchen;
	}

	@CannedReportColumn(priority = 41)
	public boolean getEquipmentTables() {
		return equipmentTables;
	}

	public void setEquipmentTables(boolean equipmentTables) {
		this.equipmentTables = equipmentTables;
	}

	@CannedReportColumn(priority = 42)
	public boolean getEquipmentBenches() {
		return equipmentBenches;
	}

	public void setEquipmentBenches(boolean equipmentBenches) {
		this.equipmentBenches = equipmentBenches;
	}

	@CannedReportColumn(priority = 43)
	public boolean getEquipmentLighting() {
		return equipmentLighting;
	}

	public void setEquipmentLighting(boolean equipmentLighting) {
		this.equipmentLighting = equipmentLighting;
	}

	@CannedReportColumn(priority = 44)
	public String getEquipmentOther() {
		if (equipmentOther == null) return null;
		return equipmentOther.getValue();
	}

	public void setEquipmentOther(String equipmentOther) {
		this.equipmentOther = new Text(equipmentOther);
	}

	@CannedReportColumn(priority = 50)
	public boolean getCanvasMarquee() {
		return canvasMarquee;
	}

	public void setCanvasMarquee(boolean canvasMarquee) {
		this.canvasMarquee = canvasMarquee;
	}

	@CannedReportColumn(priority = 51)
	public boolean getCanvasKitchen() {
		return canvasKitchen;
	}

	public void setCanvasKitchen(boolean canvasKitchen) {
		this.canvasKitchen = canvasKitchen;
	}

	@CannedReportColumn(priority = 52)
	public boolean getCanvasStore() {
		return canvasStore;
	}

	public void setCanvasStore(boolean canvasStore) {
		this.canvasStore = canvasStore;
	}

	@CannedReportColumn(priority = 53)
	public String getCanvasOther() {
		if (canvasOther == null) return null;
		return canvasOther.getValue();
	}

	public void setCanvasOther(String canvasOther) {
		this.canvasOther = new Text(canvasOther);
	}

	@CannedReportColumn(priority = 54)
	public boolean getLargeCanvasTown() {
		return largeCanvasTown;
	}

	public void setLargeCanvasTown(boolean largeCanvasTown) {
		this.largeCanvasTown = largeCanvasTown;
	}

	@CannedReportColumn(priority = 55)
	public boolean getLargeCanvasActivity() {
		return largeCanvasActivity;
	}

	public void setLargeCanvasActivity(boolean largeCanvasActivity) {
		this.largeCanvasActivity = largeCanvasActivity;
	}

	@CannedReportColumn(priority = 56)
	public boolean getLargeCanvasCafe() {
		return largeCanvasCafe;
	}

	public void setLargeCanvasCafe(boolean largeCanvasCafe) {
		this.largeCanvasCafe = largeCanvasCafe;
	}

	@CannedReportColumn(priority = 57)
	public String getLargeCanvasOther() {
		if (largeCanvasOther == null) return null;
		return largeCanvasOther.getValue();
	}

	public void setLargeCanvasOther(String largeCanvasOther) {
		this.largeCanvasOther = new Text(largeCanvasOther);
	}

	@CannedReportColumn(priority = 60)
	public String getVillagePartner() {
		return villagePartner;
	}

	public void setVillagePartner(String villagePartner) {
		this.villagePartner = villagePartner;
	}

	@CannedReportColumn(priority = 61)
	public String getDelegation() {
		if (delegation == null) return null;
		return delegation.getValue();
	}

	public void setDelegation(String delegation) {
		this.delegation = new Text(delegation);
	}

	public void setVillageKey(Key villageKey) {
		this.villageKey = villageKey;
	}

	@SkipInCannedReports
	public Key getVillageKey() {
		return villageKey;
	}

	public void setVillageWebKey(String villageWebKey) {
		if (villageWebKey.equals("")) return;
		this.villageKey = KeyFactory.stringToKey(villageWebKey);
	}

	public String getVillageWebKey() {
		if (villageKey == null) return "";
		return KeyFactory.keyToString(villageKey);
	}

	public Village getVillage() {
		if (villageKey == null) return null;
		return CannedQueries.villageByKey(villageKey);
	}
	
}
