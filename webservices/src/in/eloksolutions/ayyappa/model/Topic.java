package in.eloksolutions.ayyappa.model;

import java.util.Date;
import java.util.List;

public class Topic {
	String groupId;
	String topicId;
	String topic;
	String description;
	Date createDate;
	String sCreateDate;
	String owner;
	String ownerName;
	String imgPath;
	
	List<Discussion> discussions;
	public Topic(String id, String topic, String description, String groupId,
			String owner, long timestamp,String ownerName,String imgPath) {
		topicId=id;
		this.topic=topic;
		this.description=description;
		this.owner=owner;
		this.groupId=groupId;
		createDate=new Date(timestamp);
		this.ownerName=ownerName;
		this.imgPath=imgPath;
	}
	public Topic(String id, String topic, String description, String groupId,
			String owner, String createDate,String ownerName,String imgPath) {
		topicId=id;
		this.topic=topic;
		this.description=description;
		this.owner=owner;
		this.groupId=groupId;
		this.sCreateDate=createDate;
		this.ownerName=ownerName;
		this.imgPath=imgPath;
	}
	public Topic(String topic,String description,String owner,
			String groupId,String ownerName,String imgPath) {
		this.topic=topic;
		this.description=description;
		this.owner=owner;
		this.groupId=groupId;
		this.ownerName=ownerName;
		this.imgPath=imgPath;
	}
	
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<Discussion> getDiscussions() {
		return discussions;
	}
	public void setDiscussions(List<Discussion> discussions) {
		this.discussions = discussions;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getsCreateDate() {
		return sCreateDate;
	}
	public void setsCreateDate(String sCreateDate) {
		this.sCreateDate = sCreateDate;
	}
	@Override
	public String toString() {
		return "Topic [groupId=" + groupId + ", topicId=" + topicId
				+ ", topic=" + topic + ", description=" + description
				+ ", createDate=" + createDate + ", sCreateDate=" + sCreateDate
				+ ", owner=" + owner + ", ownerName=" + ownerName
				+ ", imgPath=" + imgPath + ", discussions=" + discussions + "]";
	}
	
		
	
}
