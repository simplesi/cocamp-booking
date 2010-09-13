package uk.org.woodcraft.bookings;

import com.opensymphony.xwork2.ActionSupport;

public class WcfBookingsAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	public String execute(){
		addActionMessage("wcfbookings: Hello, world");
		return SUCCESS;
	}
}