package ayyappa.eloksolutions.in.ayyappaap.util;

public class DataObject {
    private String mText1;
    private String mText2;

    private int imgResource;
    public DataObject(String text1, String text2, int imgResource){
        mText1 = text1;
        mText2 = text2;
        this.imgResource=imgResource;
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


    @Override
    public String toString() {
        return "DataObject{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", imgResource=" + imgResource +
                '}';
    }
}