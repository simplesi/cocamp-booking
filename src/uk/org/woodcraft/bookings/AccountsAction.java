package uk.org.woodcraft.bookings;

import java.util.Date;
import java.util.List;

import uk.org.woodcraft.bookings.accounts.AccountManager;
import uk.org.woodcraft.bookings.accounts.LineItem;
import uk.org.woodcraft.bookings.datamodel.Transaction;
import uk.org.woodcraft.bookings.persistence.SessionBasedAction;
import uk.org.woodcraft.bookings.utils.Clock;
import uk.org.woodcraft.bookings.utils.SystemClock;

public class AccountsAction extends SessionBasedAction{

	private static final long serialVersionUID = 5349573159349600392L;

	private String viewLevel = "unit";
	private AccountManager accountManager;
	
	private Clock clock = new SystemClock();
	
	public String accountsForUnit()
	{
		accountManager = new AccountManager(getCurrentEvent(), getCurrentUnit());
		return SUCCESS;
	}

	public String accountsForOrg()
	{
		accountManager = new AccountManager(getCurrentEvent(), getCurrentOrganisation());
		viewLevel = "organisation";
		return SUCCESS;
	}
	
	public String accountsForAll()
	{	
		accountManager = new AccountManager(getCurrentEvent());
		
		viewLevel = "event";
		return SUCCESS;
	}
	

	
	public List<LineItem> getCosts()
	{
		return accountManager.getCosts();
	}
	
	public double getTotalCost()
	{
		return accountManager.getTotalCost();
	}
	
	public int getTotalBookingsCount()
	{
		return accountManager.getTotalBookingsCount();
	}
	
	public List<Transaction> getPayments()
	{
		return accountManager.getPayments();
	}
	
	public double getPaymentTotal()
	{
		return accountManager.getPaymentTotal();
	}
	
	public double getBalance()
	{
		return getTotalCost() - getPaymentTotal();
	}
	
	public boolean getIsBeforeEarlyBookingDeadline()
	{
		return getCurrentEvent().getEarlyBookingDeadline().after(clock.getTime());
	}
	
	public Date getEarlyBookingsDate()
	{
		return getCurrentEvent().getEarlyBookingDeadline();
	}
	
	public double getEarlyBookingsAmount()
	{
		return getTotalCost() / 2;
	}
	
	public String getViewLevel()
	{
		return viewLevel;
	}
	

}
