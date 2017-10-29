package in.eloksolutions.ayyappa.model;

import in.eloksolutions.ayyappa.util.Util;
import in.eloksolutions.ayyappa.vo.GroupMember;
import in.eloksolutions.ayyappa.vo.UserConnectionVO;
import in.eloksolutions.ayyappa.vo.UserPadis;
import in.eloksolutions.ayyappa.vo.UserTopics;

import java.util.List;

public class User {
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
	private String imgPath;
	private LatLong loc;
	private String lat;
	private String lon;
	private String tokenFCM;
	private List<GroupMember> groups;
	private List<UserTopics> userTopics;
	private List<UserConnectionVO> userConnections;
	private List<UserPadis> userPadis;
	private boolean isRequestSent;
	private boolean isConnected;
	
	public User(){
		
	}
	public User(String userId, String firstName, String lastName,
			String mobile, String email, String area, String city, String state, String imgPath) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.area = area;
		this.city = city;
		this.state = state;
		this.imgPath=imgPath;
	}
	public User(String userId, String firstName, String lastName,
			String mobile, String email, String area, String city, String state,String lat,String lon,String imgPath) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.area = area;
		this.city = city;
		this.state = state;
		this.imgPath=imgPath;
		if(!Util.isEmpty(lat) && !Util.isEmpty(lon))
			this.loc=new LatLong(lat, lon);
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public List<GroupMember> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupMember> groups) {
		this.groups = groups;
	}
	public List<UserTopics> getUserTopics() {
		return userTopics;
	}
	public void setUserTopics(List<UserTopics> userTopics) {
		this.userTopics = userTopics;
	}
	public List<UserConnectionVO> getUserConnections() {
		return userConnections;
	}
	public void setUserConnections(List<UserConnectionVO> userConnections) {
		this.userConnections = userConnections;
	}
	public List<UserPadis> getUserPadis() {
		return userPadis;
	}
	public void setUserPadis(List<UserPadis> userPadis) {
		this.userPadis = userPadis;
	}
	public LatLong getLoc() {
		return loc;
	}
	public void setLoc(LatLong loc) {
		this.loc = loc;
	}
	
	public void setLoc(String lat,String lon) {
		this.loc = new LatLong(lat,lon);
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getTokenFCM() {
		return tokenFCM;
	}
	public void setTokenFCM(String tokenFCM) {
		this.tokenFCM = tokenFCM;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	public class LatLong{
		String lat;
		String lon;
		public LatLong(){
			
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

		public LatLong(String lat, String lon) {
			super();
			this.lat = lat;
			this.lon = lon;
		}
		
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public boolean isRequestSent() {
		return isRequestSent;
	}
	public void setRequestSent(boolean isRequestSent) {
		this.isRequestSent = isRequestSent;
	}
	public boolean isConnected() {
		return isConnected;
	}
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", mobile=" + mobile + ", email="
				+ email + ", area=" + area + ", city=" + city + ", state="
				+ state + ", password=" + password + ", createDate="
				+ createDate + ", imgPath=" + imgPath + ", loc=" + loc
				+ ", groups=" + groups + ", userTopics=" + userTopics
				+ ", userConnections=" + userConnections + ", userPadis="
				+ userPadis + "]";
	}
	
}
