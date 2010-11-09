package uk.org.woodcraft.bookings.datamodel;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class KeyBasedData implements Serializable, Keyed<Key>{
	
	private static final long serialVersionUID = 1L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public Key getKey() {
		return key;
	}
	
	
	/**
	 *  Retrieve the key value, and throw if it is null
	 * @return Key 
	 */
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
	
	public String getWebKey() {
		if (key == null) return null;
		return KeyFactory.keyToString(key);
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
		if(!(obj instanceof KeyBasedData)) return false;
		
		if(!((KeyBasedData)obj).getKey().equals(this.getKey())) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		if(this.key == null) return 0;
		return (this.key.hashCode());
	}
	
	public String toString() {
		return this.key.toString();
	}
}
