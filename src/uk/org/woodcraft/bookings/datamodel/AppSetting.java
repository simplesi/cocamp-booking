package uk.org.woodcraft.bookings.datamodel;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import uk.org.woodcraft.bookings.persistence.CannedQueries;

import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(detachable="true")
public class AppSetting {

	public static final String DEFAULT_EVENT = "Default.Event";
	
	@PrimaryKey
	@Persistent
	private	String key;
	
	@Persistent
	private	String value;

	public AppSetting(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
	
	public static String get(String key)
	{
		AppSetting setting = (CannedQueries.getByKey(AppSetting.class, key));
		
		if(setting == null) throw new IllegalArgumentException("Application Setting "+key+" does not exist in the datastore");
		return setting.getValue();
	}
	
	public static Event getDefaultEvent()
	{
		String defaultEventKey = get(DEFAULT_EVENT);
		return (CannedQueries.getByKey(Event.class, KeyFactory.stringToKey(defaultEventKey)));
	}
}
