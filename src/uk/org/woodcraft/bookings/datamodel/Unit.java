package uk.org.woodcraft.bookings.datamodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.ValidatableModelObject;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

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
	
	@Persistent
	private int estimateWoodchip=0;
	
	@Persistent
	private int estimateElfin=0;
	
	@Persistent
	private int estimatePioneer=0;
	
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
	                                					
	/**
	 * The events that this unit is registered for
	 */
	@Persistent
	private Set<Key> eventsRegistered = new HashSet<Key>();
	
	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "5", trim = true, message = "Name is required for unit")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Key getOrganisationKey() {
		return organisationKey;
	}
	
	public void setOrganisationWebKey(String webKey) {
		organisationKey = KeyFactory.stringToKey(webKey);
	}
	@StringLengthFieldValidator(type = ValidatorType.FIELD, minLength = "1", trim = true, message = "Organisation must be specified")
	public String getOrganisationWebKey() {
		if (organisationKey == null) return null;
		return KeyFactory.keyToString(organisationKey);
	}

	public Organisation getOrganisation() {
		return CannedQueries.orgByKey(getOrganisationKey());
	}
	
	public void setOrganisation(Organisation organisation) {
		this.organisationKey = organisation.getKeyCheckNotNull();
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

	public boolean isApproved() {
		return approved;
	}


	public void setApproved(boolean approved) {
		this.approved = approved;
	}


	public void setDefaultVillageForEvent(Event event, Village defaultVillage) {
		CannedQueries.persistDefaultVillageKeyForUnit(event, this, defaultVillage);
	}

	// TODO: Use a cache for this
	public Village getDefaultVillageForEvent(Event event) {
		Key key= (CannedQueries.defaultVillageKeyForUnit(event, this));
		
		if (key == null) return null;
		
		return (CannedQueries.villageByKey(key));
	}


	public Collection<Booking> getBookings(Event event)
	{
		return (CannedQueries.bookingsForUnit(this, event));
	}
	
	public String toString() {
		return getName();
	}

    // FIXME: Need to ensure this is called so we know who's going to the event
	public void addEventRegistration(Event event) {
		this.eventsRegistered.add(event.getKeyCheckNotNull());
	}

	public Set<Key> getEventsRegistered() {
		return eventsRegistered;
	}

	@Override
	public String getDeleteConditionError() {
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

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateWoodchip() {
		return estimateWoodchip;
	}

	public void setEstimateWoodchip(int estimateWoodchip) {
		this.estimateWoodchip = estimateWoodchip;
	}

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateElfin() {
		return estimateElfin;
	}

	public void setEstimateElfin(int estimateElfin) {
		this.estimateElfin = estimateElfin;
	}

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimatePioneer() {
		return estimatePioneer;
	}

	public void setEstimatePioneer(int estimatePioneer) {
		this.estimatePioneer = estimatePioneer;
	}

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateVenturer() {
		return estimateVenturer;
	}

	public void setEstimateVenturer(int estimateVenturer) {
		this.estimateVenturer = estimateVenturer;
	}

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateDF() {
		return estimateDF;
	}

	public void setEstimateDF(int estimateDF) {
		this.estimateDF = estimateDF;
	}

	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "0", max = "99", message = "Please enter a number between 0 and 99")
	public int getEstimateAdult() {
		return estimateAdult;
	}

	public void setEstimateAdult(int estimateAdult) {
		this.estimateAdult = estimateAdult;
	}

	public boolean getEquipmentKitchen() {
		return equipmentKitchen;
	}

	public void setEquipmentKitchen(boolean equipmentKitchen) {
		this.equipmentKitchen = equipmentKitchen;
	}

	public boolean getEquipmentTables() {
		return equipmentTables;
	}

	public void setEquipmentTables(boolean equipmentTables) {
		this.equipmentTables = equipmentTables;
	}

	public boolean getEquipmentBenches() {
		return equipmentBenches;
	}

	public void setEquipmentBenches(boolean equipmentBenches) {
		this.equipmentBenches = equipmentBenches;
	}

	public boolean getEquipmentLighting() {
		return equipmentLighting;
	}

	public void setEquipmentLighting(boolean equipmentLighting) {
		this.equipmentLighting = equipmentLighting;
	}

	public String getEquipmentOther() {
		if (equipmentOther == null) return null;
		return equipmentOther.getValue();
	}

	public void setEquipmentOther(String equipmentOther) {
		this.equipmentOther = new Text(equipmentOther);
	}

	public boolean getCanvasMarquee() {
		return canvasMarquee;
	}

	public void setCanvasMarquee(boolean canvasMarquee) {
		this.canvasMarquee = canvasMarquee;
	}

	public boolean getCanvasKitchen() {
		return canvasKitchen;
	}

	public void setCanvasKitchen(boolean canvasKitchen) {
		this.canvasKitchen = canvasKitchen;
	}

	public boolean getCanvasStore() {
		return canvasStore;
	}

	public void setCanvasStore(boolean canvasStore) {
		this.canvasStore = canvasStore;
	}

	public String getCanvasOther() {
		if (canvasOther == null) return null;
		return canvasOther.getValue();
	}

	public void setCanvasOther(String canvasOther) {
		this.canvasOther = new Text(canvasOther);
	}

	public boolean getLargeCanvasTown() {
		return largeCanvasTown;
	}

	public void setLargeCanvasTown(boolean largeCanvasTown) {
		this.largeCanvasTown = largeCanvasTown;
	}

	public boolean getLargeCanvasActivity() {
		return largeCanvasActivity;
	}

	public void setLargeCanvasActivity(boolean largeCanvasActivity) {
		this.largeCanvasActivity = largeCanvasActivity;
	}

	public boolean getLargeCanvasCafe() {
		return largeCanvasCafe;
	}

	public void setLargeCanvasCafe(boolean largeCanvasCafe) {
		this.largeCanvasCafe = largeCanvasCafe;
	}

	public String getLargeCanvasOther() {
		if (largeCanvasOther == null) return null;
		return largeCanvasOther.getValue();
	}

	public void setLargeCanvasOther(String largeCanvasOther) {
		this.largeCanvasOther = new Text(largeCanvasOther);
	}

	public String getVillagePartner() {
		return villagePartner;
	}

	public void setVillagePartner(String villagePartner) {
		this.villagePartner = villagePartner;
	}

	public String getDelegation() {
		if (delegation == null) return null;
		return delegation.getValue();
	}

	public void setDelegation(String delegation) {
		this.delegation = new Text(delegation);
	}

	
	public void setEventsRegistered(Set<Key> eventsRegistered) {
		this.eventsRegistered = eventsRegistered;
	}
	
}
