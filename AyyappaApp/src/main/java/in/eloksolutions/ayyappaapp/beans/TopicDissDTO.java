package in.eloksolutions.ayyappaapp.beans;

/**
 * Created by welcome on 7/21/2017.
 */

public class TopicDissDTO {
    String dissId;
    String comment;
    String sPostDate;
    String userName;
    String userId;

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

    public String getsPostDate() {
        return sPostDate;
    }

    public void setsPostDate(String sPostDate) {
        this.sPostDate = sPostDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "TopicDissDTO{" +
                "dissId='" + dissId + '\'' +
                ", comment='" + comment + '\'' +
                ", sPostDate='" + sPostDate + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
