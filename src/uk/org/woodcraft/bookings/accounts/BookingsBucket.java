package uk.org.woodcraft.bookings.accounts;

import uk.org.woodcraft.bookings.datamodel.AgeGroup;

public class BookingsBucket implements Comparable<BookingsBucket>
{
	final AgeGroup ageGroup;
	final boolean isCancelled;
	final int days;
	final double fee;
	 
	public BookingsBucket(AgeGroup ageGroup, boolean isCancelled, int days, double fee) {
		this.ageGroup = ageGroup;
		this.isCancelled = isCancelled;
		this.days = days;
		this.fee = fee;
	}
	 
	@Override
	public int hashCode() {
		return ageGroup.hashCode();
	}
	 
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof BookingsBucket)) return false;
		BookingsBucket other = (BookingsBucket) obj;
		
		return ageGroup.equals(other.ageGroup) && isCancelled == other.isCancelled
			&& days == other.days && fee == other.fee;
	}

	// Cancellation, Agegroup, days (desc)
	@Override
	public int compareTo(BookingsBucket o) {
		if (isCancelled != o.isCancelled)
		{
			if (isCancelled) 
				return 1;
			else
				return -1;
		}
		if (ageGroup != o.ageGroup) return ageGroup.compareTo(o.ageGroup);
		if (days != o.days) return Double.compare(days, o.days);
		if (fee != o.fee) return Double.compare(fee, o.fee); // want people who have booked late to come after those that have booked early
		return 0;
	}
}
