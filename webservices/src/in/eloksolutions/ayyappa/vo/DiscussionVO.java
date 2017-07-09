package in.eloksolutions.ayyappa.vo;

public class DiscussionVO {
	String topicId;	
	String comment;
	String ownerId;
	
	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "DiscussionVO [topicId=" + topicId + ", comment=" + comment
				+ ", ownerId=" + ownerId + "]";
	}
	
}
