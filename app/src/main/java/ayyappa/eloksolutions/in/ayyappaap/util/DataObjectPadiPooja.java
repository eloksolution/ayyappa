package ayyappa.eloksolutions.in.ayyappaap.util;

public class DataObjectPadiPooja {
    private String mText1;
    private String mText2;
    private String padipoojaId;
    private int imgResource;
    private int padiMembers;
    private String date;
    private String location;
    public DataObjectPadiPooja(String text1, String text2, int imgResource, String padipoojaId, int padiMembers, String date, String location) {
        mText1 = text1;
        mText2 = text2;
        this.padipoojaId = padipoojaId;
        this.imgResource = imgResource;
        this.padiMembers= padiMembers;
        this.date=date;
        this.location=location;


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

    public String getPadipoojaId() {
        return padipoojaId;
    }

    public void setPadipoojaId(String groupId) {
        this.padipoojaId = padipoojaId;
    }

    public int getPadiMembers() {
        return padiMembers;
    }

    public void setPadiMembers(int padiMembers) {
        this.padiMembers = padiMembers;
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
                ", padiMembers=" + padiMembers +
                ",location=" +location+
                '}';
    }
}