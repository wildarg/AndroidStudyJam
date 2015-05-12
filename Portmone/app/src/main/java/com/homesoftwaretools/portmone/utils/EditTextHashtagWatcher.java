package com.homesoftwaretools.portmone.utils;
/*
 * Created by Wild on 06.05.2015.
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;

import java.util.regex.Pattern;

public class EditTextHashtagWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        HashtagUtils.addLinks(s);
    }
}
