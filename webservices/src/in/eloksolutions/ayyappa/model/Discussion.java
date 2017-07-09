package in.eloksolutions.ayyappa.model;

import java.util.Date;

public class Discussion {
	
	String dissId;
	String comment;
	Date postDate;
	String sPostDate;
	String userId;
	
	public Discussion() {
		super();
	}
	public Discussion(String dissId, String comment, Date postDate,
			String userId) {
		super();
		this.dissId = dissId;
		this.comment = comment;
		this.postDate = postDate;
		this.userId = userId;
	}
	public Discussion(String comment, String userId) {
		super();
		this.comment = comment;
		this.userId = userId;
	}
	public String getDissId() {
		return dissId;
	}
	public void setDissId(String dissId) {
		this.dissId = dissId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getPostDate() {
		return postDate;
	}
	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getsPostDate() {
		return sPostDate;
	}
	public void setsPostDate(String sPostDate) {
		this.sPostDate = sPostDate;
	}
	@Override
	public String toString() {
		return "Discussion [dissId=" + dissId + ", comment=" + comment
				+ ", postDate=" + postDate + ", sPostDate=" + sPostDate
				+ ", userId=" + userId + "]";
	}
	
	
}
