package uk.org.woodcraft.bookings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import uk.org.woodcraft.bookings.datamodel.CannedReportColumn;
import uk.org.woodcraft.bookings.datamodel.NamedEntity;
import uk.org.woodcraft.bookings.datamodel.SkipInCannedReports;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Text;

public abstract class CannedReport<DataType extends Object> {

	protected List<Method> reportedMethods;

	public void initialize(CannedReportLabel selectedReport) {
		reportedMethods = new ArrayList<Method>();
		
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
	}
	
	protected abstract Class<DataType> getDataType();
	
	protected abstract Collection<DataType> getRows(CannedReportLabel selectedReport);
	
	
	public byte[] getResults(CannedReportLabel selectedReport) 
	{
		initialize(selectedReport);
		
		// Stream containing excel data
		  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		  // Create Excel WorkBook and Sheet
		  WritableWorkbook workBook;
		  try {
			  workBook = Workbook.createWorkbook(outputStream);
		  } catch (IOException e) {
			  throw new RuntimeException(e);
		  }
		  WritableSheet sheet = workBook.createSheet("Results", 0);

		  // Generates Headers Cells
		  WritableFont headerFont = new WritableFont(WritableFont.TAHOMA, 12, WritableFont.BOLD);
		  WritableCellFormat headerCellFormat = new WritableCellFormat(headerFont);
		 // headerCellFormat.setBackground(Colour.PALE_BLUE);
		  
		  int currentRow = 0;
		  int currentColumn = 0;
		  
		  List<String> headers = getHeaders();
		  
		  for(String header : headers)
			try {
				sheet.addCell(new Label(currentColumn++, currentRow, header, headerCellFormat));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		

		  // Generates Data Cells
		  WritableFont dataFont = new WritableFont(WritableFont.TAHOMA, 12);
		  WritableCellFormat dataCellFormat = new WritableCellFormat(dataFont);
		  WritableCellFormat dateCellFormat = new WritableCellFormat(dataFont, DateFormats.FORMAT2);
		  
		  
		  for(DataType row : getRows(selectedReport))
		  {
			  List<Object> rowData = renderRow(row);
			  if(rowData.size() != headers.size())
				  throw new RuntimeException( String.format(
						  				"Wrong number of columns returned when rendering row %s, against headers %s",
						  				row.toString(), headers.toString()));
			  currentRow++;
			  currentColumn = 0;
			  for(Object cellData : rowData)
			  {
				 if (cellData != null ) 
				 {
					 WritableCell cell = null;
				  
					 if (cellData instanceof NamedEntity)
						 cellData = ((NamedEntity)cellData).getName();
					 
					 if (cellData instanceof Text)
						 cellData = ((Text)cellData).getValue();
					 
					 if (cellData instanceof Email)
						 cellData = ((Email)cellData).getEmail();
					 
					 
					 
					 
					 if (cellData instanceof Number)
					 {
						 cell = new jxl.write.Number(currentColumn, currentRow, ((Number)cellData).doubleValue(), dataCellFormat);
					 } else if (cellData instanceof Date)
					 {
						 cell = new DateTime(currentColumn, currentRow, (Date)cellData, dateCellFormat);
					 } else 
					 {
						 if (!(cellData instanceof String) )
							 cellData = cellData.toString();
						
						 cell = new Label(currentColumn, currentRow, (String)cellData, dataCellFormat);
						 
					 }			 
					 
					 
					 try {
						sheet.addCell(cell);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				 }
				 currentColumn++;
			  }
			  
		  }
		 
		  // Write & Close Excel WorkBook
		  try {
			  workBook.write();
			  workBook.close();
		  } catch (Exception e)
		  {
			  throw new RuntimeException(e);
		  }

		  return outputStream.toByteArray();
	}
	
	
	protected List<String> getHeaders() {
		List<String> headers = new ArrayList<String>();
		for(Method m : reportedMethods)
		{
			String methodName = m.getName();
			// Drop the get prefix
			headers.add(methodName.substring(3));
		}
		return headers;
	}

	protected List<Object> renderRow(DataType data) {
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
	
	protected abstract List<CannedReportLabel> getAvailableReports();
	
	class CannedReportLabel
	{
		private String tag;
		private String displayName;
		private String description;
		
		public CannedReportLabel( String tag, String displayName, String description) {
			this.tag = tag;
			this.displayName = displayName;
			this.description = description;
		}

		public String getTag() {
			return tag;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getDescription() {
			return description;
		}
		
		
	}
}
