package in.eloksolutions.ayyappaapp.util;

public class DataObjectGroup {
    private String mText1;
    private String mText2;
    private String groupId;
    private String imgResource;
    private  String addTopic;
    private  String owner;
    private String isMember;
    int memberSize;

    public DataObjectGroup(String text1, String text2, String imgResource, String groupId, int memberSize, String owner, String isMember) {
        mText1 = text1;
        mText2 = text2;
        this.groupId = groupId;
        this.imgResource = imgResource;
        this.memberSize=memberSize;
        this.owner=owner;
        this.isMember=isMember;

    }

    public int getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(int memberSize) {
        this.memberSize = memberSize;
    }

    public String getImgResource() {
        return imgResource;
    }

    public void setImgResource(String imgResource) {
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

    public String getAddTopic() {
        return addTopic;
    }

    public void setAddTopic(String addTopic) {
        this.addTopic = addTopic;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    @Override
    public String toString() {
        return "DataObjectGroup{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", groupId='" + groupId + '\'' +
                ", imgResource='" + imgResource + '\'' +
                ", addTopic='" + addTopic + '\'' +
                ", owner='" + owner + '\'' +
                ", isMember='" + isMember + '\'' +
                ", memberSize=" + memberSize +
                '}';
    }
}