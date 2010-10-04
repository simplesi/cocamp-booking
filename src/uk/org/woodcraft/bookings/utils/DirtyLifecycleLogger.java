package uk.org.woodcraft.bookings.utils;

import javax.jdo.listener.DirtyLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;

public class DirtyLifecycleLogger implements DirtyLifecycleListener{

	@Override
	public void postDirty(InstanceLifecycleEvent arg0) {
		System.out.println("pre dirty" + arg0);
		
	}

	@Override
	public void preDirty(InstanceLifecycleEvent arg0) {
		System.out.println("post dirty" + arg0);
		
	}

}
