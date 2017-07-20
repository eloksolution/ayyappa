package ayyappa.eloksolutions.in.ayyappaap.util;

/**
 * Created by welcome on 7/20/2017.
 */

public class DisObject {

    private String userId;
    private String sPostDate;
    private String dissId;
    private String comment;
    private int imgResource;

    public DisObject(String userId, String sPostDate,String dissId, String comment, int imgResource) {
        this.userId = userId;
        this.sPostDate = sPostDate;
        this.dissId = dissId;
        this.comment=comment;
        this.imgResource = imgResource;
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

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}
