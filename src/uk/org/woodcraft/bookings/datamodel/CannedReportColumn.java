package uk.org.woodcraft.bookings.datamodel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface CannedReportColumn {
	int priority(); // lower is to the left, higher to the right. Unannotated fields at the end
}
