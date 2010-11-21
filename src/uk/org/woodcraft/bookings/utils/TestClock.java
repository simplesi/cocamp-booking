package uk.org.woodcraft.bookings.utils;

import java.util.Calendar;
import java.util.Date;

public class TestClock implements Clock {

	Calendar calendar = Calendar.getInstance();
	
	public TestClock( Date initialTime) {
		calendar.setTime(initialTime);
	}
	
	public void add(int field, int amount)
	{
		calendar.add(field, amount);
	}
	
	@Override
	public Date getTime() {
		return calendar.getTime();
	}

}
