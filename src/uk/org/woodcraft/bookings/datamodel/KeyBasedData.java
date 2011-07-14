package uk.org.woodcraft.bookings.datamodel;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringEscapeUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;

@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class KeyBasedData implements Serializable, Keyed<Key>{
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@SkipInCannedReports
	public Key getKey() {
		return key;
	}
	
	
	/**
	 *  Retrieve the key value, and throw if it is null
	 * @return Key 
	 */
	@SkipInCannedReports
	public Key getKeyCheckNotNull(){
		if(key == null)
			throw( new IllegalStateException(
					String.format("Key for object %s was null. Ensure the key is non-null before calling this method", 
							this.getClass().toString())));
		return key;
	}
	
	public void setKey(Key key) {
		this.key = key;
	}
	
	@SkipInCannedReports
	public String getWebKey() {
		if (key == null) return null;
		return KeyFactory.keyToString(key);
	}
	
	public String getBase64EncodedWebKey() {
		return Base64.encode(getWebKey().getBytes());
	}
	
	public void setWebKey(String webKey) {
		if( webKey == null || webKey.length() == 0) return;
		key = KeyFactory.stringToKey(webKey);
	}
	
	public boolean isNew() {
		return key == null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if(!(obj instanceof KeyBasedData)) return false;
		
		Key thatKey = ((KeyBasedData)obj).getKey();
		
		if (thatKey == null && this.getKey() != null) return false;
		if (thatKey == null ) return true;
		
		return thatKey.equals(this.getKey());
	}
	
	@Override
	public int hashCode() {
		if(this.key == null) return 0;
		return (this.key.hashCode());
	}
	
	public String toString() {
		if (key == null) return "{unknown key}";
		return this.key.toString();
	}
	
	public static class ToKey implements Transformer
	{
		@Override
		public Object transform(Object in) {
			return ((KeyBasedData) in).getKey();
		}
		
	}
}
