package uk.org.woodcraft.bookings.persistence;

import java.util.Map;

public interface ValidatableModelObject {
	public Map<String,String> getValidationErrors();
}
