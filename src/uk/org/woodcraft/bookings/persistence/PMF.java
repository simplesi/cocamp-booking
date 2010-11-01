package uk.org.woodcraft.bookings.persistence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
    
    /*
    static
    {
    	// urrrrggh - use this if you need to track lifecycle events
    	pmfInstance =  JDOHelper.getPersistenceManagerFactory("transactions-optional");
    	//pmfInstance.addInstanceLifecycleListener(new DirtyLifecycleLogger(), null);
    }
    */
    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}