package in.eloksolutions.ayyappaapp.util;

/**
 * Created by welcome on 7/20/2017.
 */

public class DisObject {

    private String userId;
    private String userName;
    private String sPostDate;
    private String dissId;
    private String comment;
    private int imgPath;

    public DisObject(String userId, String userName, String sPostDate, String dissId, String comment, int imgPath) {
        this.userId = userId;
        this.userName=userName;
        this.sPostDate = sPostDate;
        this.dissId = dissId;
        this.comment=comment;
        this.imgPath = imgPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getsPostDate() {
        return sPostDate;
    }

    public void setsPostDate(String sPostDate) {
        this.sPostDate = sPostDate;
    }

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

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "DisObject{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", sPostDate='" + sPostDate + '\'' +
                ", dissId='" + dissId + '\'' +
                ", comment='" + comment + '\'' +
                ", imgPath=" + imgPath +
                '}';
    }
}
