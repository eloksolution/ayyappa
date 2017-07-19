package in.eloksolutions.ayyappa.vo;

public class PadiMember {
	private String padiId;
	private String userId;
	private String firstName;
	private String lastName;
	private String padiName;
	
	public PadiMember(){
		
	}

	public PadiMember(String padiId, String userId, String firstName,
			String lastName) {
		super();
		this.padiId = padiId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getPadiId() {
		return padiId;
	}

	public void setPadiId(String padiId) {
		this.padiId = padiId;
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

	public String getPadiName() {
		return padiName;
	}

	public void setPadiName(String padiName) {
		this.padiName = padiName;
	}

	@Override
	public String toString() {
		return "PadiMember [padiId=" + padiId + ", userId=" + userId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", padiName=" + padiName + "]";
	}
}
