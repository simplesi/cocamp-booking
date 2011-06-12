package uk.org.woodcraft.bookings.soap;

public class MyVillageResponse {
	private String villageName;
	private String villageKey;
	private String userName;
	private String errorMessage;
	private boolean success;
	
	public MyVillageResponse() {
		// TODO Auto-generated constructor stub
	}
	
	public MyVillageResponse(String errorMessage) {
		this.errorMessage = errorMessage;
		success = false;
	}
	
	public MyVillageResponse(String userName, String villageName, String villageKey) {
		this.userName = userName;
		this.villageName = villageName;
		this.villageKey = villageKey;
		this.success = true;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getVillageKey() {
		return villageKey;
	}

	public void setVillageKey(String villageKey) {
		this.villageKey = villageKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
