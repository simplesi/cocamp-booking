package uk.org.woodcraft.bookings.datamodel;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable="true")
public class Transaction extends KeyBasedDataWithAudit implements NamedEntity{

	private static final long serialVersionUID = -8661368205753174806L;
	
	@Persistent
	private Key unitKey;
	
	@Persistent
	private Key eventKey;

	@Persistent
	private Date timestamp;
	
	@Persistent
	private String name;
	
	@Persistent
	private String comments;
	
	@Persistent
	private TransactionType type;
	
	@Persistent
	double amount;

	public Transaction(Key unitKey, Key eventKey, Date timestamp, TransactionType type, String name, String comments, Double amount) {
		this.unitKey = unitKey;
		this.eventKey = eventKey;
		this.timestamp = timestamp;
		this.type = type;
		this.name = name;
		this.comments = comments;
		this.amount = amount;
	}
	
	public Key getUnitKey() {
		return unitKey;
	}

	public void setUnitKey(Key unitKey) {
		this.unitKey = unitKey;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void setEventKey(Key eventKey) {
		this.eventKey = eventKey;
	}

	public Key getEventKey() {
		return eventKey;
	}
	
}
