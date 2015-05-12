package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 06.05.2015.
 */

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class FormValidator {

    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    public FormValidator notEmpty(EditText editText, String message) {
        if (isValid()) {
            String text = editText.getText().toString();
            valid = !TextUtils.isEmpty(text);
            if (!isValid()) {
                editText.setError(message);
                editText.requestFocus();
            }
        }
        return this;
    }

    public FormValidator positiveNumber(EditText editText, String message) {
        if (isValid()) {
            String text = editText.getText().toString();
            try {
                final Float number = Float.parseFloat(text);
                valid = number > 0;
            } catch (Exception e) {
                valid = false;
            }
            if (!isValid()) {
                editText.setError(message);
                editText.requestFocus();
            }
        }
        return this;
    }

    public FormValidator validDate(EditText editText, String message, SimpleDateFormat sdf) {
        if (isValid()) {
            String text = editText.getText().toString();
            try {
                final Date date = sdf.parse(text);
            } catch (ParseException e) {
                valid = false;
            }
            if (!isValid()) {
                editText.setError(message);
                editText.requestFocus();
            }
        }
        return this;
    }

    public FormValidator equals(EditText editText1, EditText editText2, String message) {
        if (isValid()) {
            String text1 = editText1.getText().toString();
            String text2 = editText2.getText().toString();

            valid = text1.equals(text2);

            if (!isValid()) {
                editText2.setError(message);
                editText2.requestFocus();
            }
        }
        return this;
    }

    public FormValidator isValidEmail(EditText editText, String message) {
        if (isValid()) {
            String text = editText.getText().toString();
            valid = Patterns.EMAIL_ADDRESS.matcher(text).matches();
            if (!isValid()) {
                editText.setError(message);
                editText.requestFocus();
            }
        }
        return this;
    }

}
