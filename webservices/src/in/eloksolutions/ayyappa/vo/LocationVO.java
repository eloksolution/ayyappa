package in.eloksolutions.ayyappa.vo;

public class LocationVO {
	private String userId;
	private Double lat;
	private Double lon;
	
	
	public LocationVO(String userId, Double lat, Double lon) {
		super();
		this.userId = userId;
		this.lat = lat;
		this.lon = lon;
	}
	public String getUserId() {
		return userId;
	}
	public Double getLat() {
		return lat;
	}
	public Double getLon() {
		return lon;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	@Override
	public String toString() {
		return "LocationVO [userId=" + userId + ", lat=" + lat + ", lon=" + lon
				+ "]";
	}
	
	
}
