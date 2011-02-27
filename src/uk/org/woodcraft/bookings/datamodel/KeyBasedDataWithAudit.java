package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.ObjectState;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.DeleteCallback;
import javax.jdo.listener.StoreCallback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.org.woodcraft.bookings.utils.SessionUtils;

/***
 * Track lifecycle data around this object
 *
 */
@PersistenceCapable(detachable="true")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class KeyBasedDataWithAudit extends KeyBasedData implements StoreCallback, DeleteCallback{

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
		String userKey = SessionUtils.getAuditUser().getKey();
		setCreatorUserKey(userKey);
		
		log.info(String.format("%s created %s", userKey, getObjectAuditDescription()));
	}
	
	/***
	 * Triggered when an object is updated and about to be persisted
	 */
	private void auditUpdate() {
		
		setUpdatedTime(new Date());
		String userKey = SessionUtils.getAuditUser().getKey();
		setUpdatedUserKey(userKey);
		
		log.info(String.format("%s updated %s", userKey, getObjectAuditDescription()));
	}

	/***
	 * Triggered when an object is delete and about to be persisted
	 */
	private void auditDelete() {
		log.info(String.format("%s deleted %s", SessionUtils.getAuditUser().getKey(), getObjectAuditDescription()));
	}
	
	@SkipInCannedReports
	private String getObjectAuditDescription() {
		if (this instanceof NamedEntity)
			return String.format("{type:%s, key:%s, name:%s}", this.getClass().getName(), getKey(), ((NamedEntity)this).getName());
		
		return String.format("{type:%s, key:%s}", this.getClass().getName(), getKey()); 
	}
	
	public void setAuditRecord(AuditRecord auditRecord) {
		this.auditRecord = auditRecord;
	}
	
	@SkipInCannedReports
	public AuditRecord getAuditRecord() {
		if (auditRecord == null) auditRecord = new AuditRecord();
		return auditRecord;
	}
	
	// FIXME: make the audit record detach correctly, and then remove this skip
	@SkipInCannedReports
	public String getCreatorUserKey() {
		return getAuditRecord().getCreatorUserKey();
	}
	
	protected void setCreatorUserKey(String creatorUserKey) {
		this.getAuditRecord().setCreatorUserKey(creatorUserKey);
	}
	
	@SkipInCannedReports
	public Date getCreateTime() {
		return getAuditRecord().getCreateTime();
	}
	
	protected void setCreateTime(Date createTime) {
		this.getAuditRecord().setCreateTime(createTime);
	}
	
	@SkipInCannedReports
	public String getUpdatedUserKey() {
		return getAuditRecord().getUpdatedUserKey();
	}
	
	protected void setUpdatedUserKey(String updatedUserKey) {
		this.getAuditRecord().setUpdatedUserKey(updatedUserKey);
	}
	
	@SkipInCannedReports
	public Date getUpdatedTime() {
		return getAuditRecord().getUpdatedTime();
	}
	
	protected void setUpdatedTime(Date updatedTime) {
		this.getAuditRecord().setUpdatedTime(updatedTime);
	}

	@Override
	public void jdoPreDelete() {
		auditDelete();
	}
	

}