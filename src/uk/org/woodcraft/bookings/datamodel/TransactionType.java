package uk.org.woodcraft.bookings.datamodel;

import java.util.ArrayList;
import java.util.List;

public enum TransactionType {

	Payment,
	Fees(false, true), 
	Discount,
	Adjustment,
	Bill(true, true);
	
	private boolean userUpdatable = true;
	private boolean isCost = false;
	
	private TransactionType( boolean userUpdatable, boolean isCost) {
		this.userUpdatable = userUpdatable;
		this.isCost = isCost;
	}
	private TransactionType() {
	}
	
	public boolean getUserUpdatable()
	{
		return userUpdatable;
	}
	
	public boolean getIsCost()
	{
		return isCost;
	}
	
	public static List<TransactionType> userUpdatableValues()
	{
		List<TransactionType> result = new ArrayList<TransactionType>();
		for (TransactionType transactionType : values()) {
			if (transactionType.userUpdatable) result.add(transactionType);
		}
		return result;
	}
}
