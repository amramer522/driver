package com.amoor.driver.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class HelperMethod {
    public static void replace(Fragment fragment, FragmentManager supportFragmentManager, int id) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static String getTextFromTil(TextInputLayout textInputLayout) {
        String text = textInputLayout.getEditText().getText().toString().trim();
        return text;
    }

    public static String getTextFromEt(EditText editText) {
        String text = editText.getText().toString().trim();
        return text;
    }

    public static void setTextToTil(TextInputLayout textInputLayout, String text) {
        textInputLayout.getEditText().setText(text);
    }

    public static String getTextFromSpinner(Spinner spinner) {
        String text = spinner.getSelectedItem().toString().trim();
        return text;
    }

    public static void setAppLanguage(Context context, String localeCode) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void setTilEmpty(TextInputLayout registerFragmentTilEmailAddress) {
        registerFragmentTilEmailAddress.getEditText().setText("");
    }


    public static void showToastMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean tilsVaild(String name, String email, String phone, String gender, String password, String confirm_password) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)) {

            return true;
        } else {
            return false;
        }

    }


    public static boolean isValidate(Context context, String... values) {
        String[] msgs = {"Student Id", "User name", "password", "Confirm password", "Mobile phone", "Email", "Home address", "Bus Line", "University", "Faculty", "Level"};
        for (int i = 0; i < values.length; i++) {
            boolean empty = TextUtils.isEmpty(values[i]);
            if (empty) {
                showToastMsg(context, msgs[i] + " is empty");
                return false;
            }
        }

        if (TextUtils.equals(values[2], values[3])) {
            if (values[2].length() > 7) {
                return true;
            } else {
                showToastMsg(context, "password must be at least 7 chars");
                return false;
            }
        } else {
            showToastMsg(context, "passwords not matched");
            return false;
        }
    }

    public static void setSpinnerAdapter(Activity activity, String[] list, Spinner spinner) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    public static void showTimeDialog(Activity activity, final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                if (selectedHour>12)
//                {
//                    textView.setText( selectedHour-12 + ":" + selectedMinute + " PM");
//
//                }
//                else
//                {
//                    textView.setText( selectedHour + ":" + selectedMinute + " AM");
//                }
                String time =  ((selectedHour > 12) ? selectedHour % 12 : selectedHour) + ":" + (selectedMinute < 10 ? ("0" + selectedMinute) : selectedMinute) + " " + ((selectedHour >= 12) ? "PM" : "AM");
                textView.setText(time);

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

}
