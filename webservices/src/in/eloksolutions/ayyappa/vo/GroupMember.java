package in.eloksolutions.ayyappa.vo;

public class GroupMember {
	private String groupId;
	private String userId;
	private String firstName;
	private String lastName;
	private String groupName;
	private String imgPath;
	
	public GroupMember(){
		
	}
	
	public GroupMember(String groupId, String userId, String groupName) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.groupName = groupName;
	}

	public GroupMember(String groupId, String userId, String firstName,
			String lastName) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return "GroupMember [groupId=" + groupId + ", userId=" + userId
				+ ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

	
	
}
