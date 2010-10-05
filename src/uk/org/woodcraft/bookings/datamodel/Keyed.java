package uk.org.woodcraft.bookings.datamodel;

public interface Keyed<T> {
	public void setKey(T key);
	public T getKey();
}
