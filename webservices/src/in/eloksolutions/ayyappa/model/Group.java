package in.eloksolutions.ayyappa.model;

public class Group {
	 String groupId;
	String name;
    String description;
	String owner;
	String createDate;
	int numberOfMembers;
	String type;
	String imagePath;
	String catgory;
	public Group(){
		
	}
	public Group(  String groupId,String name, String description, String owner
			) {
		super();
		this.groupId = groupId;
		this.name = name;
		this.description = description;
		this.owner = owner;
		
		//this.createDate = createDate;
		//this.numberOfMembers = numberOfMembers;
		//this.type = type;
		//this.imagePath = imagePath;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getNumberOfMembers() {
		return numberOfMembers;
	}
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	public String getCatgory() {
		return catgory;
	}
	public void setCatgory(String catgory) {
		this.catgory = catgory;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	@Override
	public String toString() {
		return "Group [ name=" + name + ", groupId=" + groupId
				+ ", description=" + description + ", owner=" + owner
				+ ", createDate=" + createDate + ", numberOfMembers="
				+ numberOfMembers + ", type=" + type + ", imagePath="
				+ imagePath + ", catgory=" + catgory + "]";
	}
}
