package uk.org.woodcraft.bookings.persistence;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheSupport {
	
	private static final Log log = LogFactory.getLog (CacheSupport.class);
	
	private static final Map<Object,Serializable> localcache = new HashMap<Object,Serializable>();
	private static final MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
	
	/*
	private static MemcacheService cacheInit(){
		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
		return memcache;
	}
	*/
	public static Object cacheGet(Object id){
		Object r = localcache.get(id);
		if (r!= null) return r;
		
		try {
			r = memcache.get(id);
			/*if (r!=null) 
				log.debug("Retrieved result from cache for "+id);
			else
				log.debug("No cache result for "+id);
	*/
		}
		catch (MemcacheServiceException e) {
			// nothing can be done.
		}
		return r;
	}
	
	public static void cacheDelete(Object id){
		//MemcacheService memcache = cacheInit();
		localcache.remove(id);
		memcache.delete(id);
		log.debug("Deleted item "+id);
	}
	public static void cachePutExp(Object id, Serializable o, int exp) {
	//	MemcacheService memcache = cacheInit();
		try {
			if (exp>0) {
				memcache.put(id, o, Expiration.byDeltaSeconds(exp));
			}
			else {
				localcache.put(id, o);
				memcache.put(id, o);
			}
		//	log.debug("Added to cache "+id);
		}
		catch (MemcacheServiceException e) {
			// nothing can be done.
		}
	}
	public static void cachePut(Object id, Serializable o){
		cachePutExp(id, o, 0);
	}
	
	/**
	 *  Flush the cache of everything - for use with testing
	 */
	public static void flush(){
		localcache.clear();
		log.debug("Cache flushed");
	}
}
