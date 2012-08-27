package uk.org.woodcraft.bookings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Diet;
import uk.org.woodcraft.bookings.datamodel.Event;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.Clock;


public class BulkBookingParser {

	private static final int FIRST_ROW = 8;
	private static final int LAST_ROW = 8;
	
	private static final int COL_NAME 			= 0;
	private static final int COL_DOB 			= 1;
	private static final int COL_EMAIL 			= 2;
	private static final int COL_PHONE 			= 3;
	private static final int COL_WCF_MEMBERSHIP = 4;
	private static final int COL_DIET 			= 5;
	private static final int COL_DIET_NOTES 	= 6;
	private static final int COL_OTHER_NEEDS 	= 7;
	private static final int COL_COCAMP_MEMBER 	= 8;
	private static final int COL_DATE_ARRIVAL 	= 9;
	private static final int COL_DATE_DEPARTURE	= 10;
	
	
	private Workbook workbook;
	private Unit unit;
	private Event event;
	private Clock clock;
	
	public BulkBookingParser(File file, Unit unit, Event event, Clock clock) throws BiffException, IOException {
		workbook = Workbook.getWorkbook(file);
		this.unit = unit;
		this.event = event;
		this.clock = clock;
	}
	
	public Collection<Booking> getBookings()
	{
		Collection<Booking> bookings = new ArrayList<Booking>();
		
		Sheet sheet = workbook.getSheet(0);
		
		for(int row = FIRST_ROW; row <= LAST_ROW; row++)
		{
			Booking booking = bookingForRow(sheet, row);
			if (booking != null) bookings.add(booking);
		}
		
		return bookings;	
	}
	
	private Booking bookingForRow(Sheet sheet, int row)
	{
		String name = getString(sheet, row, COL_NAME);
		
		if (name != null && name.length() > 0)
		{
			Date dob 			= getDate(sheet, row, COL_DOB);
			String email 		= getString(sheet, row, COL_EMAIL);
			String phone 		= getString(sheet, row, COL_PHONE);
			int wcfMembership 	= (int)getNumber(sheet, row, COL_WCF_MEMBERSHIP);
			String dietString 	= getString(sheet, row, COL_DIET);
			Diet diet 			= Diet.valueOf(dietString);
			String dietNotes 	= getString(sheet, row, COL_DIET_NOTES);
			String otherNeeds 	= getString(sheet, row, COL_OTHER_NEEDS);
			String cocampMember = getString(sheet, row, COL_COCAMP_MEMBER);
			Date dateArrival 	= getDate(sheet, row, COL_DATE_ARRIVAL);
			Date dateDeparture 	= getDate(sheet, row, COL_DATE_DEPARTURE);
			
			Booking booking = Booking.create(name, unit, event, clock);
			booking.setDob(dob);
			booking.setEmail(email);
			booking.setPhoneNumber(phone);
			//booking.setMembershipNumber(wcf_membership)
			booking.setDiet(diet);
			booking.setDietNotes(dietNotes);
			booking.setOtherNeeds(otherNeeds);
			//booking.setBecomeMember(becomeMember)
			booking.setArrivalDate(dateArrival);
			booking.setDepartureDate(dateDeparture);
			
			return booking;
		}
		
		return null;
	}
	

	private String getString(Sheet sheet, int row, int col)
	{
		Cell cell = sheet.getCell(row,col); 
		if (cell.getType() != CellType.LABEL) 
			throw new RuntimeException("Expected cell type to be string at "+row+","+col);
		
		return  ((LabelCell) cell).getString(); 
	}
	
	private Date getDate(Sheet sheet, int row, int col)
	{
		Cell cell = sheet.getCell(row,col); 
		if (cell.getType() != CellType.DATE) 
			throw new RuntimeException("Expected cell type to be date at "+row+","+col);
		
		return  ((DateCell) cell).getDate();
	}
	
	private double getNumber(Sheet sheet, int row, int col)
	{
		Cell cell = sheet.getCell(row,col); 
		if (cell.getType() != CellType.NUMBER) 
			throw new RuntimeException("Expected cell type to be date at "+row+","+col);
		
		return  ((NumberCell) cell).getValue();
	}
}
