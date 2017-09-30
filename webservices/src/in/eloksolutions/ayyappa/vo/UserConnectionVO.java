package in.eloksolutions.ayyappa.vo;

public class UserConnectionVO {
	String userId;
	String firstName;
	String lastName;
	String connectedToId;
	String toFirstName;
	String toLastName;
	String connectDate;
	String status;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getConnectedToId() {
		return connectedToId;
	}
	public void setConnectedToId(String connectedToId) {
		this.connectedToId = connectedToId;
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
	public String getToFirstName() {
		return toFirstName;
	}
	public void setToFirstName(String toFirstName) {
		this.toFirstName = toFirstName;
	}
	public String getToLastName() {
		return toLastName;
	}
	public void setToLastName(String toLastName) {
		this.toLastName = toLastName;
	}
	public String getConnectDate() {
		return connectDate;
	}
	public void setConnectDate(String connectDate) {
		this.connectDate = connectDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "UserConnectionVO [userId=" + userId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", connectedToId="
				+ connectedToId + ", toFirstName=" + toFirstName
				+ ", toLastName=" + toLastName + ", connectDate=" + connectDate
				+ ", status=" + status + "]";
	}
	
	
}
