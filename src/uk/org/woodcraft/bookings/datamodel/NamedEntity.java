package uk.org.woodcraft.bookings.datamodel;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;


public interface NamedEntity {

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Name is required")
	public String getName();
}
