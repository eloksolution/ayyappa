package ayyappa.eloksolutions.in.ayyappaap;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateAndTimePicker {
    public static void datePickerDialog(Activity activity,final TextView txt) {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        final int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txt.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public static TimePickerDialog getTimePickerDialog(int hour, int minute, Activity activity, final TextView txt) {

        TimePickerDialog  mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String format;
                if (selectedHour == 0) {
                    selectedHour += 12;
                    format = "AM";
                } else if (selectedHour == 12) {
                    format = "PM";
                } else if (selectedHour > 12) {
                    selectedHour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }
                txt.setText(selectedHour + ":" + selectedMinute +" "+ format);
            }
        }, hour, minute, true);//Yes 24 hour time
        return mTimePicker;
    }

}
