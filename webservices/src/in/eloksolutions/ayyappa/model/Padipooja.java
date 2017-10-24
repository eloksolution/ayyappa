package in.eloksolutions.ayyappa.model;

import java.util.List;

public class Padipooja {
	String padipoojaId;
	String eventName;
	String location;
	String description;
	String date;
	String day;
	String month;
	String week;
	String year;
	String time;
	String memId;
	String name;
	String isMember;
	String imgPath;
	String noOfMembers;

	List<User> padiMembers;
	
	public Padipooja(){
		
	}
	public Padipooja(String padipoojaId, String eventName, String location, String description, String date, String time, String memId
			,String name,String imgPath){
		super();
		this.padipoojaId=padipoojaId;
		this.eventName=eventName;
		this.location=location;
		this.description=description;
		this.date=date;
		this.time=time;
		this.memId=memId;
		this.name=name;
		this.imgPath=imgPath;
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
	

	public String getIsMember() {
		return isMember;
	}
	public void setIsMember(String isMember) {
		this.isMember = isMember;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getDay() {
		return day;
	}
	public String getMonth() {
		return month;
	}
	public String getWeek() {
		return week;
	}
	public String getYear() {
		return year;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getNoOfMembers() {
		return noOfMembers;
	}
	public void setNoOfMembers(String noOfMembers) {
		this.noOfMembers = noOfMembers;
	}
	@Override
	public String toString() {
		return "Padipooja [padipoojaId=" + padipoojaId + ", eventName="
				+ eventName + ", location=" + location + ", description="
				+ description + ", date=" + date + ", time=" + time
				+ ", memId=" + memId + ", name=" + name + "]";
	}
	

}
