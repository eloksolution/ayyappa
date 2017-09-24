package ayyappa.eloksolutions.in.ayyappaap.beans;

import java.util.ArrayList;
import java.util.List;

public class EventDTO {
	
	//for event
	private String padipoojaId;
	private String eventName;
	private String date;
	private String time;
	private String location;
	private String description;
	private String eventPic;
	private String pincode;
	private String owner;
	private Integer memberCount;
	private String ownerName;
	private int status;
	private boolean member;
	private boolean past;
	private String areaName;
	private String city;
	private String state;
	private String country;
	private String imgPath;
	private String memId;


	//list of members

	//list of members
	List<RegisterDTO> padiMembers=new ArrayList<RegisterDTO>();

	public String getMemId() {
		return memId;
	}

	public void setMemId(String memId) {
		this.memId = memId;
	}

	public void setPadipoojaId(String padipoojaId) {
		this.padipoojaId = padipoojaId;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public boolean isMember() {
		return member;
	}

	public boolean isPast() {
		return past;
	}

	public void setPast(boolean past) {
		this.past = past;
	}

	public List<RegisterDTO> getPadiMembers() {
		return padiMembers;
	}

	public void setPadiMembers(List<RegisterDTO> padiMembers) {
		this.padiMembers = padiMembers;
	}

	public String getPadipoojaId() {
		return padipoojaId;
	}
	public void setPadipoojaId(Integer id) {
		this.padipoojaId = padipoojaId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getdate() {
		return date;
	}

	public void setdate(String date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEventPic() {
		return eventPic;
	}
	public void setEventPic(String eventPic) {
		this.eventPic = eventPic;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String gettime() {
		return time;
	}
	public void setTime(String ftTime) {
		this.time = ftTime;
	}

	public String getDate() {
		return date;
	}


	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}
	public boolean getMember() {
		return member;
	}
	public void setMember(boolean isMember) {
		this.member = isMember;
	}
	public boolean getPast() {
		return past;
	}

	public void setIsPast(boolean isPast) {
		this.past = isPast;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getImagePath() {
		return imgPath;
	}

	public void setImagePath(String imagePath) {
		this.imgPath = imagePath;
	}

	@Override
	public String toString() {
		return "EventDTO{" +
				"padipoojaId=" + padipoojaId +
				", eventName='" + eventName + '\'' +
				", time='" + time + '\'' +
				", date='" + date + '\'' +
				", location='" + location + '\'' +
				", description='" + description + '\'' +
				", eventPic='" + eventPic + '\'' +
				", pincode=" + pincode +
				", owner=" + owner +
				", memberCount=" + memberCount +
				", ownerName='" + ownerName + '\'' +
				", isMember=" + member +
				", isPast=" + past +

				'}';
	}
}
