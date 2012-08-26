package uk.org.woodcraft.bookings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jxl.read.biff.BiffException;

import org.apache.commons.collections.CollectionUtils;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.SystemClock;

import com.opensymphony.xwork2.validator.ActionValidatorManager;
import com.opensymphony.xwork2.validator.DefaultActionValidatorManager;
import com.opensymphony.xwork2.validator.ValidationException;

public class BulkBookingAction extends SessionBasedAction {

	private static final long serialVersionUID = -3290376628818898762L;

	private File bulkBookingFile;
	private Clock clock = new SystemClock();

	private Collection<Booking> successfulBookings;
	private Collection<Booking> duplicateBookings;
	
	
	@SuppressWarnings("unchecked")
	public String execute()
	{
		if (bulkBookingFile == null)
		{
			addActionError("A file containing bookings must be supplied");
			return INPUT;
		}
		Collection<Booking> bookings;
		try {
			 bookings = parseUploadedFile(bulkBookingFile, clock);
		} catch (BiffException e) {
			e.printStackTrace();
			addActionError("Unable to read supplied excel file.");
			return INPUT;
		} catch (IOException e) {
			e.printStackTrace();
			addActionError("Unable to read supplied excel file.");
			return INPUT;
		} catch (BulkUploadException e) {
			return INPUT;
		}
			
		duplicateBookings = checkBookingDuplicates(bookings);
		setSuccessfulBookings(CollectionUtils.subtract(bookings, duplicateBookings));
		
		return SUCCESS;
	}
	
	private Collection<Booking> checkBookingDuplicates(Collection<Booking> bookings) {
		List<Booking> duplicates = new ArrayList<Booking>();
		
		for(Booking booking : bookings)
		{
			Collection<Booking> candidateDuplicates = CannedQueries.bookingsForName(booking.getName());
			if (candidateDuplicates.size() > 0)
				duplicates.add(booking);
		}
		
		return duplicates;
	}

	public void setBulkBookingFile(File bulkBookingFile) {
		this.bulkBookingFile = bulkBookingFile;
	}

	
	public File setBulkBookingFile() {
		return bulkBookingFile;
	}
	
	private Collection<Booking> parseUploadedFile(File file, Clock clock) throws BiffException, IOException, BulkUploadException
	{
		BulkBookingParser parser = new BulkBookingParser(file, getCurrentUnit(), getCurrentEvent(), clock);
		Collection<Booking> bookings = parser.getBookings();
		
		validateBookings(bookings);
		
		return bookings;
	}
	
	private void validateBookings(Collection<Booking> bookings) throws BulkUploadException {
		ActionValidatorManager validator = new DefaultActionValidatorManager();
		
		boolean errorsFound = false;
		
		for(Booking booking : bookings)
		{
			try {
				validator.validate(booking, "");
			} catch (ValidationException e) {
				addActionError("Booking for '" + booking.getName() + "' was invalid - "+e.getMessage());
				errorsFound = true;
				e.printStackTrace();
			}
		}
		if (errorsFound) 
			throw new BulkUploadException();
	}

	public void setSuccessfulBookings(Collection<Booking> successfulBookings) {
		this.successfulBookings = successfulBookings;
	}

	public Collection<Booking> getSuccessfulBookings() {
		return successfulBookings;
	}
	
}
