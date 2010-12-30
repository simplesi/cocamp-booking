package uk.org.woodcraft.bookings.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static Date getDate(int year, int month, int day)
	{
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		
		return (c.getTime());
	}
	
	public static int ageOnDay(Date dob, Date day)
	{
		GregorianCalendar dobCal = new GregorianCalendar();
		GregorianCalendar dayCal = new GregorianCalendar();
	    
	    int age = 0;
	    int factor = 0; //to correctly calculate age when birthday has not been yet celebrated

	    dobCal.setTime(dob);
	    dayCal.setTime(day);
	    
	    if(dayCal.before(dobCal)) 
	    	throw new IllegalArgumentException(String.format("Date of birth %tF cannot be after comparison day %tF", dob, day ));
	    
	    // check if birthday has been celebrated this year
	    if(dayOfYearProxy(dayCal)< dayOfYearProxy(dobCal)) {
	            factor = -1; //birthday not celebrated
	    }
	    
	    age = dayCal.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR) + factor;
		
	    return age;
	}
	
	/**
	 * Get a proxy for the day of the year so that day1 < day2 if d1 < d2, even if one is a leap year and the other isn't 
	 * @param date
	 * @return
	 */
	private static int dayOfYearProxy(GregorianCalendar date)
	{
		return (date.get(Calendar.MONTH) * 50 + date.get(Calendar.DAY_OF_MONTH));
	}
	
	public static Date cleanupTime(Date input)
	{ 
		if (input == null) return input;
		
		Calendar c = Calendar.getInstance();
		c.setTime(input);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static int daysBetween(Date date1, Date date2)
	{
		int days =  (int) (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000);
		
		// invert if wrong way round
		if (days < 0) days = -days;
		return days;
	}
}
