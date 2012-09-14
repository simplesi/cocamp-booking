package uk.org.woodcraft.bookings.test;

import java.util.Date;

import uk.org.woodcraft.bookings.utils.DateUtils;

public class TestConstants {

	public static final String EVENT1_NAME = "Test event 1";
	
	public static final Date EVENT1_START = DateUtils.getDate(2011, 6, 30);
	public static final Date EVENT1_END = DateUtils.getDate(2011, 7, 9);
	
	public static final String VILLAGE1_NAME = "Village 1";
	
	public static final String ORG1_NAME = "Woodcraft Folk";
	
	public static final String UNIT1_NAME = "Unit 1";
	public static final String UNIT2_NAME = "Unit 2";
	
	public static final String USER_ADMIN_EMAIL = "globaladmin@example.com";
	
	public static final Date DATE_BEFORE_EARLY_DEADLINE = DateUtils.getDate(2010, 11, 10);
	public static final Date DATE_BEFORE_DEADLINE = DateUtils.getDate(2011, 1, 10);
	public static final Date DATE_AFTER_DEADLINE_BEFORE_AMMENDMENT = DateUtils.getDate(2011, 5, 10);
	public static final Date DATE_AFTER_AMMENDMENT_DEADLINE = DateUtils.getDate(2011, 6, 10);
	
}
