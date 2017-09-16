package in.eloksolutions.ayyappa.vo;


public class TopicVO {
	String groupId;
	String topicId;
	String topic;
	String description;
	String createDate;
	String owner;
	String noOfMembers;
	String imgPath;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getNoOfMembers() {
		return noOfMembers;
	}
	public void setNoOfMembers(String noOfMembers) {
		this.noOfMembers = noOfMembers;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	@Override
	public String toString() {
		return "TopicVO [groupId=" + groupId + ", topicId=" + topicId
				+ ", topic=" + topic + ", description=" + description
				+ ", createDate=" + createDate + ", owner=" + owner
				+ ", noOfMembers=" + noOfMembers + "]";
	}
	
}
