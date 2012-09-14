package uk.org.woodcraft.bookings;

import uk.org.woodcraft.bookings.datamodel.Accesslevel;
import uk.org.woodcraft.bookings.datamodel.AppSetting;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Organisation;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.CoreData;
import uk.org.woodcraft.bookings.pricing.RegisteredPricingStrategy;
import uk.org.woodcraft.bookings.test.TestConstants;
import uk.org.woodcraft.bookings.utils.DateUtils;

import com.opensymphony.xwork2.ActionSupport;

public class CreateInitialProdDataAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public String execute(){
		
		String setupComplete = AppSetting.get(AppSetting.SETUP_COMPLETE, true);
		
		if (setupComplete != "" )
			throw new SecurityException("Cannot recreate users for an already set up instance");
		
		CoreData.createCoreData();
		
		Event event1 = new Event(TestConstants.EVENT1_NAME, TestConstants.EVENT1_START, TestConstants.EVENT1_END, true, RegisteredPricingStrategy.COCAMP);
		event1.setEarlyBookingDeadline(DateUtils.getDate(2011, 0, 1));
		event1.setBookingDeadline(DateUtils.getDate(2011, 5, 2));
		event1.setBookingSystemLocked(TestConstants.EVENT1_START);
		CannedQueries.save(event1);
		
		AppSetting defaultEventSetting = new AppSetting(AppSetting.DEFAULT_EVENT, event1.getWebKey());
		CannedQueries.save(defaultEventSetting);
		
		
		Organisation orgWcf = new Organisation(TestConstants.ORG1_NAME, true);
		CannedQueries.save(orgWcf);
		
		Unit unit1 = new Unit(TestConstants.UNIT1_NAME, orgWcf, true);
		CannedQueries.save(unit1);
		
		User rootUser = new User("globaladmin@example.com", "Global Admin 1", "password", Accesslevel.GLOBAL_ADMIN);
		rootUser.setOrganisationKey(orgWcf.getKeyCheckNotNull());
		rootUser.setUnitKey(unit1.getKeyCheckNotNull());
		CannedQueries.save(rootUser);

		User orgUser = new User("orgbooking@example.com", "Org Booking Secretary 1", "password", Accesslevel.ORG_ADMIN);
		orgUser.setOrganisationKey(orgWcf.getKeyCheckNotNull());
		orgUser.setUnitKey(unit1.getKeyCheckNotNull());
		CannedQueries.save(orgUser);

		User unitUser = new User("unitbooking@example.com", "Unit Booking Secretary 1", "password", Accesslevel.UNIT_ADMIN);
		unitUser.setOrganisationKey(orgWcf.getKeyCheckNotNull());
		unitUser.setUnitKey(unit1.getKeyCheckNotNull());
		CannedQueries.save(unitUser);
		
		
		// And don't let this be repeated!
		AppSetting setupCompleteSetting = new AppSetting(AppSetting.SETUP_COMPLETE, "true");
		CannedQueries.save(setupCompleteSetting);
		
		
		addActionMessage("Successfully created initial data. You should now be able to log in.");
		return SUCCESS;
	}
}
