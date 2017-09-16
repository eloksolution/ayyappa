package in.eloksolutions.ayyappa.vo;

public class DeekshaVO {
	String userId;
	String startDate;
	String endDate;
	String description;
	
	
	public DeekshaVO(String userId, String startDate, String endDate,
			String description) {
		super();
		this.userId = userId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}
	public String getUserId() {
		return userId;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getDescription() {
		return description;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "DeekshaVO [userId=" + userId + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", description=" + description + "]";
	}
	
}
