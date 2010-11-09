package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(detachable="true")
public class AuditRecord extends KeyBasedData{

	private static final long serialVersionUID = 1L;

	@Persistent
	String creatorUserKey;
	@Persistent
	Date createTime;
	
	@Persistent
	String updatedUserKey;
	@Persistent
	Date updatedTime;
	
	public String getCreatorUserKey() {
		return creatorUserKey;
	}
	public void setCreatorUserKey(String creatorUserKey) {
		this.creatorUserKey = creatorUserKey;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdatedUserKey() {
		return updatedUserKey;
	}
	public void setUpdatedUserKey(String updatedUserKey) {
		this.updatedUserKey = updatedUserKey;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
}