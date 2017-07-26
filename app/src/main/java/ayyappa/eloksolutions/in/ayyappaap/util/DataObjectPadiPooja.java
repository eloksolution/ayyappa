package ayyappa.eloksolutions.in.ayyappaap.util;

public class DataObjectPadiPooja {
    private String mText1;
    private String mText2;
    private String padipoojaId;
    private int imgResource;

    public DataObjectPadiPooja(String text1, String text2, int imgResource, String padipoojaId) {
        mText1 = text1;
        mText2 = text2;
        this.padipoojaId = padipoojaId;
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

    public String getPadipoojaId() {
        return padipoojaId;
    }

    public void setPadipoojaId(String groupId) {
        this.padipoojaId = padipoojaId;
    }



    @Override
    public String toString() {
        return "DataObject{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", padipoojaId='" + padipoojaId + '\'' +
                ", imgResource=" + imgResource +
                '}';
    }
}