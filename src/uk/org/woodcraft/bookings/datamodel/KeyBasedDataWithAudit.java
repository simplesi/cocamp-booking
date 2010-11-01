package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.StoreCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.woodcraft.bookings.persistence.CoreData;
import uk.org.woodcraft.bookings.utils.SessionUtils;

/***
 * Track lifecycle data around this object
 *
 */
@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class KeyBasedDataWithAudit extends KeyBasedData implements StoreCallback{

	private static final Log log = LogFactory.getLog (KeyBasedDataWithAudit.class);
	
	private static final long serialVersionUID = 1L;
	
	@Persistent(dependent = "true")
	private AuditRecord auditRecord = new AuditRecord();
	
	@Override
	public void jdoPreStore() {
		ObjectState state = JDOHelper.getObjectState(this);
		if (state.equals(ObjectState.TRANSIENT)
				|| state.equals(ObjectState.PERSISTENT_NEW)) 
		{
			auditCreate();
			
		} else if (state.equals(ObjectState.PERSISTENT_DIRTY) 
				|| state.equals(ObjectState.PERSISTENT_NONTRANSACTIONAL_DIRTY) 
				|| state.equals(ObjectState.DETACHED_DIRTY)) {
			auditUpdate();
		}
		
	}	
	
	/***
	 * Triggered when an object is created and about to be persisted
	 */
	private void auditCreate() {
		
		setCreateTime(new Date());
		String userKey = getAuditUser().getKey();
		setCreatorUserKey(userKey);
		
	//	if (log.isDebugEnabled()) 
			log.info(String.format("%s created object of type %s with key %s", userKey, this.getClass().getName(), getKey()));
	}
	
	/***
	 * Triggered when an object is updated and about to be persisted
	 */
	private void auditUpdate() {
		
		setUpdatedTime(new Date());
		String userKey = getAuditUser().getKey();
		setUpdatedUserKey(userKey);
		
		//if (log.isDebugEnabled()) 
			log.info(String.format("%s updated object of type %s with key %s", userKey, this.getClass().getName(), getKey()));
	}
	
	public void setAuditRecord(AuditRecord auditRecord) {
		this.auditRecord = auditRecord;
	}

	public AuditRecord getAuditRecord() {
		return auditRecord;
	}
	
	public String getCreatorUserKey() {
		return getAuditRecord().getCreatorUserKey();
	}
	
	protected void setCreatorUserKey(String creatorUserKey) {
		this.getAuditRecord().setCreatorUserKey(creatorUserKey);
	}
	
	public Date getCreateTime() {
		return getAuditRecord().getCreateTime();
	}
	
	protected void setCreateTime(Date createTime) {
		this.getAuditRecord().setCreateTime(createTime);
	}
	
	public String getUpdatedUserKey() {
		return getAuditRecord().getUpdatedUserKey();
	}
	
	protected void setUpdatedUserKey(String updatedUserKey) {
		this.getAuditRecord().setUpdatedUserKey(updatedUserKey);
	}
	
	public Date getUpdatedTime() {
		return getAuditRecord().getUpdatedTime();
	}
	
	protected void setUpdatedTime(Date updatedTime) {
		this.getAuditRecord().setUpdatedTime(updatedTime);
	}
	
	private User getAuditUser() {
		User user = SessionUtils.currentUser(false);
		if (user == null) user = CoreData.SYSTEM_USER;
		
		return user;
	}
}