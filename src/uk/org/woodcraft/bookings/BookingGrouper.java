package uk.org.woodcraft.bookings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.org.woodcraft.bookings.datamodel.Booking;
import uk.org.woodcraft.bookings.datamodel.Unit;
import uk.org.woodcraft.bookings.utils.DateUtils;


public class BookingGrouper {

	private List<BookingTransformer> transformers;
	
	// By Village
	// By Age
	// By Diet
	// By Unit
	// By Org
	
	public BookingGrouper(List<BookingTransformer> transformers) {
		this.transformers = transformers;
	}
	
	public Map<List<Object>, List<Booking>> group(Collection<Booking> bookings)
	{
		Map<List<Object>, List<Booking>> results = new LinkedHashMap<List<Object>, List<Booking>>();
		
		for(Booking booking : bookings)
		{
			List<List<Object>> groups = groupsFor(booking);
			
			for(List<Object> group : groups)
			{
				List<Booking> resultForGroup = results.get(group);
				if (resultForGroup == null) 
				{
					resultForGroup = new ArrayList<Booking>(1);
					results.put(group, resultForGroup);
				}
				
				resultForGroup.add(booking);
			}
		}
		return results;
	}
	
	private List<List<Object>> groupsFor(Booking booking)
	{
		List<List<Object>> groups = new ArrayList<List<Object>>();
		
		Object[] valuesPerDimension = new Object[transformers.size()];
		int dimNum = 0;
		for(BookingTransformer transformer : transformers)
		{
			 Object[] values = transformer.transform(booking);
			 
			 // Ensure there's always something
			 if (values.length == 0) values = new Object[]{null};
			 valuesPerDimension[dimNum++] = values;
		
		}
		
		int[] indexPerDimension = new int[transformers.size()];
		Arrays.fill(indexPerDimension, 0);

		boolean complete = false;
		
		do {
			// Work out the group for this dimension combination
			List<Object> group = new ArrayList<Object>(indexPerDimension.length);
			for (int i = 0; i < indexPerDimension.length; i++) {
				Object[] valuesForDim = (Object[])valuesPerDimension[i];
				group.add(valuesForDim[indexPerDimension[i]]);
			}
			groups.add(group);
			
			boolean haveIncremented = false;
			int iteratingDim = indexPerDimension.length - 1;
			
			do {
				if (indexPerDimension[iteratingDim] >= ((Object[])(valuesPerDimension[iteratingDim])).length -1)
				{
					// Hit the maximimum for this dimension, reset, and increment preceding dimension
					
					if (iteratingDim > 0) 
					{
						indexPerDimension[iteratingDim] = 0;
						iteratingDim--;
					} else {
						// All dimensions now at max, so quit
						haveIncremented = true;
						complete = true;
					}
					
				} else {
					indexPerDimension[iteratingDim]++;
					haveIncremented = true;
				}
				
			} while(!haveIncremented);	
			
		} while(!complete);
		
		
		return groups;
	}
	
	
	public interface BookingTransformer
	{
		Object[] transform(Booking b);
		String columnName();
	}
	
	public static class VillageTransformer implements BookingTransformer
	{
		@Override
		public Object[] transform(Booking b) {
			return new Object[]{b.getVillage()};
		}

		@Override
		public String columnName() {
			return "Village";
		}
	}

	public static class AgeTransformer implements BookingTransformer
	{
		@Override
		public Object[] transform(Booking b) {
			return new Object[]{b.getAgeGroup()};
		}
		@Override
		public String columnName() {
			return "Age Group";
		}
	}

	public static class DietTransformer implements BookingTransformer
	{
		@Override
		public Object[] transform(Booking b) {
			return new Object[]{b.getDiet()};
		}
		
		@Override
		public String columnName() {
			return "Diet";
		}
	}
	
	public static class UnitTransformer implements BookingTransformer
	{
		@Override
		public Object[] transform(Booking b) {
			return new Object[]{b.getUnit()};
		}
		
		@Override
		public String columnName() {
			return "Unit";
		}
	}
	public static class OrgTransformer implements BookingTransformer
	{
		@Override
		public Object[] transform(Booking b) {
			Unit unit = b.getUnit();
			if (unit == null) return new Object[]{null};
			return new Object[]{unit.getOrganisation()};
		}
		
		@Override
		public String columnName() {
			return "Organisation";
		}
	}
	
	public static class AllDatesAtEventTransformer implements BookingTransformer
	{

		@Override
		public Object[] transform(Booking b) {
			List<Date> dates = new ArrayList<Date>();
			
			Date arrivalDate = DateUtils.cleanupTime(b.getArrivalDate());
			Date departureDate = DateUtils.cleanupTime(b.getDepartureDate());
			if (arrivalDate == null || departureDate == null) 
				return dates.toArray();
			
			Date date = arrivalDate;
			
			do
			{
				dates.add(date);
				date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
			}
			while(date.before(departureDate));
			
			dates.add(departureDate);
			return dates.toArray();
		}
		
		@Override
		public String columnName() {
			return "Date";
		}
		
	}
}