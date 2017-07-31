package in.eloksolutions.ayyappa.model;

import in.eloksolutions.ayyappa.vo.PadiMember;

import java.util.List;

public class Padipooja {
	String padipoojaId;
	String eventName;
	String location;
	String description;
	String date;
	String time;
	String memId;
	String name;
	List<User> padiMembers;
	
	public Padipooja(){
		
	}
	public Padipooja(String padipoojaId, String eventName, String location, String description, String date, String time, String memId
			,String name){
		super();
		this.padipoojaId=padipoojaId;
		this.eventName=eventName;
		this.location=location;
		this.description=description;
		this.date=date;
		this.time=time;
		this.memId=memId;
		this.name=name;
	}
	
	public String getPadipoojaId() {
		return padipoojaId;
	}
	public void setPadipoojaId(String padipoojaId) {
		this.padipoojaId = padipoojaId;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<User> getPadiMembers() {
		return padiMembers;
	}


	public void setPadiMembers(List<User> padiMembers) {
		this.padiMembers = padiMembers;
	}


	@Override
	public String toString() {
		return "Padipooja [padipoojaId=" + padipoojaId + ", eventName="
				+ eventName + ", location=" + location + ", description="
				+ description + ", date=" + date + ", time=" + time
				+ ", memId=" + memId + ", name=" + name + "]";
	}
	

}
