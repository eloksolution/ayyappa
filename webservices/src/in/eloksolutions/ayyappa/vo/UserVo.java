package in.eloksolutions.ayyappa.vo;

public class UserVo {
	private String userId;
	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String area;
	private String city;
	private String state;
	private String password;
	private String createDate;
	private String lat;
	private String lon;
	private String imgPath;
	private String status;
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getLat() {
		return lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserVo [userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", mobile=" + mobile + ", email="
				+ email + ", area=" + area + ", city=" + city + ", state="
				+ state + ", password=" + password + ", createDate="
				+ createDate + ", lat=" + lat + ", lon=" + lon + ", imgPath="
				+ imgPath + "]";
	}
	
	

}
