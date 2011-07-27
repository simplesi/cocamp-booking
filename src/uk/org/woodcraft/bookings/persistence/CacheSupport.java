package uk.org.woodcraft.bookings.persistence;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.woodcraft.bookings.datamodel.KeyBasedDataWithAudit;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheSupport {
	
	private static final Log log = LogFactory.getLog (CacheSupport.class);
	
	private static MemcacheService cacheInit(){
		MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
		return memcache;
	}
	public static Object cacheGet(Object id){
		Object r = null;
		MemcacheService memcache = cacheInit();
		try {
			r = memcache.get(id);
			if (r!=null) 
				log.debug("Retrieved result from cache for "+id);
			else
				log.debug("No cache result for "+id);
	
		}
		catch (MemcacheServiceException e) {
			// nothing can be done.
		}
		return r;
	}
	
	public static void cacheDelete(Object id){
		MemcacheService memcache = cacheInit();
		memcache.delete(id);
		log.debug("Deleted item "+id);
	}
	public static void cachePutExp(Object id, Serializable o, int exp) {
		MemcacheService memcache = cacheInit();
		try {
			if (exp>0) {
				memcache.put(id, o, Expiration.byDeltaSeconds(exp));
			}
			else {
				memcache.put(id, o);
			}
			log.debug("Added to cache "+id);
		}
		catch (MemcacheServiceException e) {
			// nothing can be done.
		}
	}
	public static void cachePut(Object id, Serializable o){
		cachePutExp(id, o, 0);
	}
}
