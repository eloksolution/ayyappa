package ayyappa.eloksolutions.in.ayyappaap.config;

public class  Config {

   public  static String SERVER_URL="http://13.59.69.182/AS/";
   String YOUR_SERVER_URL =  "http://13.59.69.182/AS/";
    String YOUR_SERVER_URL_LOCAL =  "http://192.168.0.2:8080/AyappaServices/";
   String DISPLAY_MESSAGE_ACTION ="info.activities.services.myarea.DISPLAY_MESSAGE";
    String EXTRA_MESSAGE = "message";
    public  static String APP_PREFERENCES="ayyappa";

    public  static String USER_KEY="User_ID";
    public  static String userId;
    public  static String firstName;
    public  static String lastName;
    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        userId = userId;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
      firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        lastName = lastName;
    }
}