package uk.org.woodcraft.bookings.accounts;

import java.util.Date;

import uk.org.woodcraft.bookings.datamodel.TransactionType;

public class LineItem {
	
	private final Date date;
	private final TransactionType type;
	private final String name;
	private final Integer quantity;
	private final double price;
	

	public LineItem(Date date, TransactionType type, String name, Integer quantity, double price) {
		this.date = date;
		this.type = type;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
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


	public double getPrice() {
		return price;
	}


	public double getLinePrice() {
		if (quantity == null) return price;
		
		return quantity * price;
	}
}
