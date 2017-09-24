package ayyappa.eloksolutions.in.ayyappaap.util;

public class DataObjectPadiPooja {
    private String mText1;
    private String mText2;
    private String padipoojaId;
    private String imgResource;
    private int memberSize;
    private String date;
    private String location;
    public DataObjectPadiPooja(String text1, String text2, String imgResource, String padipoojaId, int memberSize, String date, String location) {
        mText1 = text1;
        mText2 = text2;
        this.padipoojaId = padipoojaId;
        this.imgResource = imgResource;
        this.memberSize= memberSize;
        this.date=date;
        this.location=location;


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

    public String getPadipoojaId() {
        return padipoojaId;
    }

    public void setPadipoojaId(String groupId) {
        this.padipoojaId = padipoojaId;
    }

    public int getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(int memberSize) {
        this.memberSize = memberSize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", padipoojaId='" + padipoojaId + '\'' +
                ", imgResource=" + imgResource +
                ", memberSize=" + memberSize +
                ",location=" +location+
                '}';
    }
}