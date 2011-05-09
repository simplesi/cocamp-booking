package uk.org.woodcraft.bookings.accounts;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.TransactionType;

public class LineItem {
	
	private final Date date;
	private final TransactionType type;
	private final String name;
	private final Integer quantity;
	private final double price;
	private final int bookingQuantity;
	

	public LineItem(Date date, TransactionType type, String name, Integer quantity, double price, int bookingQuantity) {
		this.date = date;
		this.type = type;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.bookingQuantity = bookingQuantity;
	}

	public Date getDate()
	{
		return date;
	}
	
	public TransactionType getType() {
		return type;
	}


	public String getName() {
		return name;
	}


	public Integer getQuantity() {
		return quantity;
	}

	public Integer getBookingQuantity() {
		return bookingQuantity;
	}

	public double getPrice() {
		return price;
	}


	public double getLinePrice() {
		if (quantity == null) return price;
		
		return quantity * price;
	}
}
