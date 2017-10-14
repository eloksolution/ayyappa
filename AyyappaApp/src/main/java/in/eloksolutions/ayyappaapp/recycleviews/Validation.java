package in.eloksolutions.ayyappaapp.recycleviews;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Validation {
    // Regular Expression
    // you can change the expression based on your need
   // private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9]{10}";
    private static final String PINCODE_REGEX = "[0-9]{6}";
    private static final String OTP_REGEX = "[0-9]{4}";
    private static final String USERNAME_REGEX="^((?=.*?[0-9])|(?=.*?[#?!@$%^&*-])).{5,10}$";
    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String PHONE_MSG = "Give proper mobile number";
    private static final String PINCODE_MSG = "Give proper pincode number";
    private static final String OTP_MSG = "Give proper otp number";
    private static final String USERNAME_MSG="User Name should be 5 to 10 digits with atleast one numeric or special character";


    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
    //check pincode
    public static boolean isPincode(EditText editText, boolean required) {
        return isValid(editText, PINCODE_REGEX, PINCODE_MSG, required);
    }
    //check otp
    public static boolean isOtp(EditText editText, boolean required) {
        return isValid(editText, OTP_REGEX, OTP_MSG, required);
    }
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.requestFocus();
            editText.setError(errMsg);
            return false;
        };

        return true;
    }
    // check the input field has any text or not
    // return true if it contains text otherwise false
   public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.requestFocus();
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
    public static boolean userName(EditText editText, boolean required) {
        return isValid(editText, USERNAME_REGEX, USERNAME_MSG, required);
    }
    public static boolean hasSelected(Spinner sp, EditText invisible) {
        TextView errorText = (TextView)sp.getSelectedView();
        errorText.setError(null);
        //String text = invisible.getText().toString().trim();
        invisible.setError(null);
        // length 0 means there is no text
        if (sp.getSelectedItemPosition()==0) {
            errorText.requestFocus();
            errorText.setError(REQUIRED_MSG);
            //errorText.setText(REQUIRED_MSG);
            invisible.requestFocus();
            invisible.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

}
