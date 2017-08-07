package in.eloksolutions.ayyappa.model;

import java.util.Date;

public class Discussion {
	
	String dissId;
	String comment;
	Date postDate;
	String sPostDate;
	String userId;
	String userName;
	
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
	public Discussion(String comment, String userId,String userName) {
		super();
		this.comment = comment;
		this.userId = userId;
		this.userName=userName;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "Discussion [dissId=" + dissId + ", comment=" + comment
				+ ", postDate=" + postDate + ", sPostDate=" + sPostDate
				+ ", userId=" + userId + "]";
	}
	
	
}
