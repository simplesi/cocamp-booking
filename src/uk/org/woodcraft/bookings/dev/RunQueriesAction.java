package uk.org.woodcraft.bookings.dev;

import java.util.Collection;

import uk.org.woodcraft.bookings.auth.SecurityModel;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.Village;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.test.TestConstants;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This action is intended to exercise all queries on the datastore, so that we know which indexes to use
 * @author simon
 *
 */
public class RunQueriesAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	public String execute()
	{
		SecurityModel.checkIsDevMode();
		
		// Event queries
		CannedQueries.allEvents(false);
		CannedQueries.allEvents(true);
		Event event1 = CannedQueries.eventByName(TestConstants.EVENT1_NAME);
		@SuppressWarnings("unused")
		Event duplicateEvent = CannedQueries.eventByName(TestConstants.EVENT1_NAME, event1.getKeyCheckNotNull());
		CannedQueries.eventByKey(event1.getKeyCheckNotNull());
		
		// Village queries
		CannedQueries.villagesForEvent(event1);
		Village village1 = CannedQueries.villageByName(TestConstants.VILLAGE1_NAME, event1);
		@SuppressWarnings("unused")
		Village duplicateVillage = CannedQueries.villageByName(TestConstants.VILLAGE1_NAME, event1.getKeyCheckNotNull(), village1.getKeyCheckNotNull());
		CannedQueries.villageByKey(village1.getKeyCheckNotNull());
		
		// Org queries
		CannedQueries.allOrgs(false);
		CannedQueries.allOrgs(true);
		CannedQueries.allUnapprovedOrgs();
		Organisation org1 = CannedQueries.orgByName(TestConstants.ORG1_NAME);
		@SuppressWarnings("unused")
		Organisation duplicateOrg = CannedQueries.orgByName(TestConstants.ORG1_NAME, org1.getKeyCheckNotNull());
		CannedQueries.orgByKey(org1.getKeyCheckNotNull());
		
		// Unit queries
		CannedQueries.allUnits(false);
		CannedQueries.allUnits(true);
		CannedQueries.allUnapprovedUnits();
		Unit unit1 = CannedQueries.unitByName(TestConstants.UNIT1_NAME, org1);
		@SuppressWarnings("unused")
		Unit duplicateUnit = CannedQueries.unitByName(TestConstants.UNIT1_NAME, org1.getKeyCheckNotNull(), unit1.getKey());
		CannedQueries.unitByKey(unit1.getKeyCheckNotNull());
		CannedQueries.unitsForOrg(org1.getKeyCheckNotNull(), false);
		CannedQueries.unitsForOrg(org1.getKeyCheckNotNull(), true);
		CannedQueries.unitsHomeless(event1);
		CannedQueries.unitsForVillage(village1);
		
		// Booking queries
		Collection<Booking> bookings = CannedQueries.bookingsForOrg(org1, event1);
		CannedQueries.bookingsForEvent(event1);
		CannedQueries.bookingsForUnitAllEvents(unit1);
		CannedQueries.bookingsForUnit(unit1, event1);
		CannedQueries.bookingsForVillage(village1);
		CannedQueries.bookingsHomeless(event1);
		CannedQueries.bookingByKey(bookings.iterator().next().getKeyCheckNotNull());
		CannedQueries.bookingsForName("Test person 2");
		 
		// User queries
		CannedQueries.allUsers();
		CannedQueries.allUnapprovedUsers();
		CannedQueries.allUsersForOrg(org1);
		CannedQueries.allUsersForUnit(unit1);
		CannedQueries.getUserByEmail(TestConstants.USER_ADMIN_EMAIL);
		
		// Others
		CannedQueries.defaultVillageKeyForUnit(event1, unit1);

		return SUCCESS;
	}
}
