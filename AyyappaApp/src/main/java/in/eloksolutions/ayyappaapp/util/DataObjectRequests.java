package in.eloksolutions.ayyappaapp.util;

/**
 * Created by welcome on 10/18/2017.
 */

public class DataObjectRequests {
    private String userId;
    private String imgPath;
    private String firstName;
    private String lastName;
    private int yes;
    private int no;

    public DataObjectRequests(String userId, String imgPath, String firstName, String lastName, int yes, int no) {
        this.userId=userId;
        this.imgPath=imgPath;
        this.firstName=firstName;
        this.lastName=lastName;
        this.yes=yes;
        this.no=no;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "DataObjectRequests{" +
                "userId='" + userId + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", yes=" + yes +
                ", no=" + no +
                '}';
    }
}
