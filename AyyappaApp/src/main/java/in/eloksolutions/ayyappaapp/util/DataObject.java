package in.eloksolutions.ayyappaapp.util;

public class DataObject {
    private String mText1;
    private String mText2;

    private String moviesImages;
    public DataObject(String text1, String text2, String moviesImages){
        mText1 = text1;
        mText2 = text2;
        this.moviesImages=moviesImages;
    }



    public String getMoviesImages() {
        return moviesImages;
    }

    public void setMoviesImages(String moviesImages) {
        this.moviesImages = moviesImages;
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
                ", moviesImages=" + moviesImages +
                '}';
    }
}