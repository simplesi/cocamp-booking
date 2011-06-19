package uk.org.woodcraft.bookings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
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
import uk.org.woodcraft.bookings.datamodel.NamedEntity;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("rawtypes")
public abstract class CannedReport {

	public CannedReport() {
		super();
	}

	protected abstract Collection getRows(CannedReportLabel selectedReport);

	public byte[] getResults(CannedReportLabel selectedReport) {
		List<Method> reportedMethods = getReportedMethods(selectedReport);
		
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
		  
		  List<String> headers = getHeaders(reportedMethods);
		  
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
		  
		  
		  for(Object row : getRows(selectedReport))
		  {
			  List<Object> rowData = renderRow(reportedMethods, row);
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

	protected abstract List<CannedReportLabel> getAvailableReports();

	protected abstract List<Method> getReportedMethods(CannedReportLabel selectedReport);
}