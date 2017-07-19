package ayyappa.eloksolutions.in.ayyappaap.beans;

import java.util.Date;

public class MemberDTO {
	private String memId;
	private String memberName;
	private Date updatedTs;
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memberId) {
		this.memId = memId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public Date getUpdatedTs() {
		return updatedTs;
	}
	public void setUpdatedTs(Date updatedTs) {
		this.updatedTs = updatedTs;
	}
	
	

}
