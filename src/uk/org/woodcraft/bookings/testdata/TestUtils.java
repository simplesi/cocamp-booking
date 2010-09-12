package uk.org.woodcraft.bookings.testdata;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.org.woodcraft.bookings.datamodel.NamedEntity;

public class TestUtils {

	public static void assertNames( List<? extends NamedEntity> entities, String... expectedNames  )
	{
		List<String> foundNames = new ArrayList<String>(entities.size());
		for(NamedEntity entity : entities)
			foundNames.add(entity.getName());
		
		String[] actualNames = (String[])foundNames.toArray(new String[] {});
		Arrays.sort(actualNames);
		Arrays.sort(expectedNames);
		
		assertArrayEquals(expectedNames, actualNames);
	}
	
}
