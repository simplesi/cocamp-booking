package uk.org.woodcraft.bookings.datamodel;

import java.io.Serializable;

public interface Keyed<T> extends Serializable{
	public void setKey(T key);
	public T getKey();
}
