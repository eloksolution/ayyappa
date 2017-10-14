package in.eloksolutions.ayyappaapp.util;

public class DataObjectPadiPooja {
    private String mText1;
    private String mText2;
    private String padipoojaId;
    private String imgResource;
    private int memberSize;
    private String date;
    private String location;
    private  String day;
    private String month;
    private  String week;
    private  String year;
    public DataObjectPadiPooja(String text1, String text2, String imgResource, String padipoojaId, int memberSize, String date, String location,String month,String day,String week) {
        mText1 = text1;
        mText2 = text2;
        this.padipoojaId = padipoojaId;
        this.imgResource = imgResource;
        this.memberSize= memberSize;
        this.date=date;
        this.location=location;
        this.month=month;
        this.day=day;
        this.week=week;


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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "DataObjectPadiPooja{" +
                "mText1='" + mText1 + '\'' +
                ", mText2='" + mText2 + '\'' +
                ", padipoojaId='" + padipoojaId + '\'' +
                ", imgResource='" + imgResource + '\'' +
                ", memberSize=" + memberSize +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", week='" + week + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}