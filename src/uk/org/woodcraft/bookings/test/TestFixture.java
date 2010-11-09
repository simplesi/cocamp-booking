package uk.org.woodcraft.bookings.test;

public abstract class TestFixture {

	public static final TestFixture BASIC_DATA = new BasicTestDataFixture();
	
	public void setUp()
	{
		createStorageData();
	}
	
	public abstract void createStorageData();
	
	
	public void tearDown()
	{
		// do nothing
	}
}
