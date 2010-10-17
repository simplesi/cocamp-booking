package uk.org.woodcraft.bookings.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.persistence.CoreData;
import uk.org.woodcraft.bookings.test.BaseAppEngineTestCase;
import uk.org.woodcraft.bookings.test.TestConstants;

public class KeyBasedDataWithAuditTest extends BaseAppEngineTestCase {

	@Test
	public void testAuditRecord() {
		Event event = new Event("name", TestConstants.EVENT1_START, TestConstants.EVENT1_END, false);
		
		assertNull(event.getCreateTime());
		assertNull(event.getCreatorUserKey());
		
		event = CannedQueries.save(event);
	//	System.out.println(JDOHelper.getObjectState(event));
		
		assertNotNull(event.getCreateTime());
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getCreatorUserKey());
		assertNull(event.getUpdatedTime());
		assertNull(event.getUpdatedUserKey());
		
		Date creationTime = event.getCreateTime();
		
	//	System.out.println(JDOHelper.getObjectState(event));
		
		/*
		 * In theory the below block should work, but datanucleus transitions the object from detached-clean -> persistent-dirty for some reason
		 * 
		// Nothing changed, so no audit change
		event = CannedQueries.save(event);
		assertEquals(creationTime, event.getCreateTime());
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getCreatorUserKey());
		assertNull(event.getUpdatedTime());
		assertNull(event.getUpdatedUserKey());
		*/
		
		
		// Now change something, and check
		event.setName("renamed event");
		event = CannedQueries.save(event);
		assertEquals(creationTime, event.getCreateTime());
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getCreatorUserKey());
		assertNotNull(event.getUpdatedTime());
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getUpdatedUserKey());
		
		Date firstUpdateTime = event.getUpdatedTime();
		
		
		// Now change something again, and check
		event.setName("renamed event 2");
		
		event = CannedQueries.save(event);
		assertEquals(creationTime, event.getCreateTime());
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getCreatorUserKey());
		assert(!firstUpdateTime.equals(event.getUpdatedTime()));
		assertEquals(CoreData.SYSTEM_USER_EMAIL, event.getUpdatedUserKey());
		
	}
	
}
