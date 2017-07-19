package ayyappa.eloksolutions.in.ayyappaap.helper;

import java.util.Date;


public class EventMembers {
	private String id;
	private String eventId;
	private String eventName;
	private String memId;
	private String memberName;
	private String owner;
	private String pincode;
	private Date updatedTs;

	public EventMembers(){}

	public EventMembers(String id, String eventId, String eventName, String memId, String memberName, String owner, String pincode, Date updatedTs) {
		this.id = id;
		this.eventId = eventId;
		this.eventName = eventName;
		this.memId = memId;
		this.memberName = memberName;
		this.owner = owner;
		this.pincode = pincode;
		this.updatedTs = updatedTs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getMemId() {
		return memId;
	}

	public void setmemId(String memId) {
		this.memId = memId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public Date getUpdatedTs() {
		return updatedTs;
	}

	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}
}
