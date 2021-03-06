package in.eloksolutions.ayyappa.vo;

public class PadiMember {
	private String padiId;
	private String userId;
	private String firstName;
	private String lastName;
	private String padiName;
	private String imgPath;
	
	public PadiMember(){
		
	}

	public PadiMember(String padiId, String userId, String firstName,
			String lastName,String imgPath) {
		super();
		this.padiId = padiId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.imgPath=imgPath;
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

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return "PadiMember [padiId=" + padiId + ", userId=" + userId
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", padiName=" + padiName + ", imgPath=" + imgPath + "]";
	}

}
