package in.eloksolutions.ayyappa.vo;

public class GroupMember {
	private String groupId;
	private String userId;
	private String userName;
	
	public GroupMember(){
		
	}
	
	
	public GroupMember(String groupId, String userId, String userName) {
		super();
		this.groupId = groupId;
		this.userId = userId;
		this.userName = userName;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Override
	public String toString() {
		return "GroupMember [groupId=" + groupId + ", userId=" + userId
				+ ", userName=" + userName + "]";
	}
	
}
