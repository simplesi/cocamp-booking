package uk.org.woodcraft.bookings.utils;

import java.util.Date;

public class SystemClock implements Clock{

	@Override
	public Date getTime() {
		return new Date();
	}

}
