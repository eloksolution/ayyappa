package in.eloksolutions.ayyappaapp.helper;

/**
 * Created by welcome on 7/20/2017.
 */
public class GroupMemberObject {
    private String userId;
    private String firstName;
    private String lastName;
    private int imgResource;
   private String imgPath;

    public GroupMemberObject(String userId, String firstName, int imgResource, String lastName, String imgPath) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imgResource = imgResource;
        this.imgPath=imgPath;
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

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
