package uk.org.woodcraft.bookings;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uk.org.woodcraft.bookings.test.BaseFixtureTestCase;
import uk.org.woodcraft.bookings.test.TestFixture;


public class BookedUnitsReportTest extends BaseFixtureTestCase{

	public BookedUnitsReportTest() {
		super(TestFixture.BASIC_DATA);
	}
	
	@Test
	public void testGetHeaders()
	{
		BookedUnitsReport report = new BookedUnitsReport();
		List<String> headers = report.getHeaders();
		/*
		for(String header : headers)
			System.out.print(header + "\",\"");
		*/
		String[] expectedHeaders = {"Name","Organisation","Email","Phone","Address","NotesString",
				"EstimateWoodchip","EstimateElfin","EstimatePioneer","EstimateVenturer","EstimateDF",
				"EstimateAdult","EquipmentKitchen","EquipmentTables","EquipmentBenches","EquipmentLighting",
				"EquipmentOther","CanvasMarquee","CanvasKitchen","CanvasStore","CanvasOther","LargeCanvasTown",
				"LargeCanvasActivity","LargeCanvasCafe","LargeCanvasOther","VillagePartner","Delegation","Key"};
		
		
		assertEquals(Arrays.asList(expectedHeaders), headers);
	}
	
	@Test
	public void testGenerateSimpleReport()
	{
		BookedUnitsReport report = new BookedUnitsReport();
		
		report.getResults(null);
	}
	
	

}
