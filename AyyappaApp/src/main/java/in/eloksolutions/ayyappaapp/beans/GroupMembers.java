package in.eloksolutions.ayyappaapp.beans;

/**
 * Created by welcome on 7/18/2017.
 */

public class GroupMembers {
    String groupId;
    String userId;
    String firstname;
    String lastName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "GroupMembers{" +
                "groupId='" + groupId + '\'' +
                ", userId='" + userId + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
