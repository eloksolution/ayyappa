package ayyappa.eloksolutions.in.ayyappaap.util;

public class DataObjectGroup {
    private String mText1;
    private String mText2;
    private String groupId;
    private int imgResource;

    public DataObjectGroup(String text1, String text2, int imgResource, String groupId) {
        mText1 = text1;
        mText2 = text2;
        this.groupId = groupId;
        this.imgResource = imgResource;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", groupId='" + groupId + '\'' +
                ", imgResource=" + imgResource +
                '}';
    }
}