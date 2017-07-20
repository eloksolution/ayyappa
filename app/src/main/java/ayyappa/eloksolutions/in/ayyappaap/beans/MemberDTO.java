package ayyappa.eloksolutions.in.ayyappaap.beans;

import java.util.Date;

public class MemberDTO {
	private String userId;
	private String firstName;
	private Date lastName;


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

	public Date getLastName() {
		return lastName;
	}

	public void setLastName(Date lastName) {
		this.lastName = lastName;
	}
}
