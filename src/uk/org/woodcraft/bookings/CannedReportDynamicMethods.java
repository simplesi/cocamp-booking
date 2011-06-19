package uk.org.woodcraft.bookings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.org.woodcraft.bookings.datamodel.CannedReportColumn;
import uk.org.woodcraft.bookings.datamodel.SkipInCannedReports;


public abstract class CannedReportDynamicMethods extends CannedReport {


	public List<Method> getReportedMethods(CannedReportLabel selectedReport) {
		ArrayList<Method> reportedMethods = new ArrayList<Method>();
		
		for(Method candidateMethod : getDataType().getMethods())
		{
			int modifiers = candidateMethod.getModifiers();
			if (!Modifier.isStatic(modifiers)
					&& Modifier.isPublic(modifiers)
					&& candidateMethod.getParameterTypes().length == 0)
			{
				if (candidateMethod.getName().startsWith("get")
					&& !candidateMethod.getReturnType().equals(Void.class))
					{
						if (!candidateMethod.getDeclaringClass().getPackage().getName().contains("uk.org.woodcraft.bookings"))
							continue;
					
						if (candidateMethod.isAnnotationPresent(SkipInCannedReports.class))
							continue;
						
						candidateMethod.setAccessible(true);
						reportedMethods.add(candidateMethod);
						
					}
			}
		}
		
		// TODO: fix order of some names
		// TODO: filter to recognised return types?
		
		// By priority, then alphabetical by name
		Collections.sort(reportedMethods, new Comparator<Method>() {
			@Override
			public int compare(Method m1, Method m2) {
				
				CannedReportColumn m1Column = m1.getAnnotation(CannedReportColumn.class);
				CannedReportColumn m2Column = m2.getAnnotation(CannedReportColumn.class);
				
				if (m1Column == null) m1Column = new CannedReportColumn() {
					public Class<? extends Annotation> annotationType() {return null;}
					public int priority() {	return Integer.MAX_VALUE; }
				};
				
				if (m2Column == null) m2Column = new CannedReportColumn() {
					public Class<? extends Annotation> annotationType() {return null;}
					public int priority() {	return Integer.MAX_VALUE; }
				};
				
				// Resort to names if equal
				if (m1Column.priority() == m2Column.priority())
					return m1.getName().compareTo(m2.getName());	
				
				return (m1Column.priority()-m2Column.priority());
			}
		});
		
		return reportedMethods;
	}
	
	@SuppressWarnings("rawtypes")
	protected abstract Class getDataType();
	
	protected List<String> getHeaders(List<Method> reportedMethods) {
		List<String> headers = new ArrayList<String>();
		for(Method m : reportedMethods)
		{
			String methodName = m.getName();
			// Drop the get prefix
			headers.add(methodName.substring(3));
		}
		return headers;
	}

	protected List<Object> renderRow(List<Method> reportedMethods, Object data) {
		List<Object> cells = new ArrayList<Object>();
		for(Method m : reportedMethods)
		{
			try {
				cells.add(m.invoke(data));
			} catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		return cells;
	}
}
