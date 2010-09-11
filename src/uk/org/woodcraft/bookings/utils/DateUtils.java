package uk.org.woodcraft.bookings.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static Date getDate(int year, int month, int day)
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		
		return (c.getTime());
	}
}
