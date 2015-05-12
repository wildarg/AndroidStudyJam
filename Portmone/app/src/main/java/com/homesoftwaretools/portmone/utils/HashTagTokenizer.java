package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 07.05.2015.
 */

import android.widget.MultiAutoCompleteTextView;

public class HashTagTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    @Override
    public int findTokenStart(CharSequence text, int cursor) {
        int i = cursor;

        while (i > 0 && text.charAt(i-1) != '#') {
            i--;
        }
        if (i>0)
            return i-1;
        else
            return i;
    }

    @Override
    public int findTokenEnd(CharSequence text, int cursor) {
        int i = cursor;
        int len = text.length();

        while (i < len) {
            if (text.charAt(i) == ' ') {
                return i;
            } else {
                i++;
            }
        }

        return len;

    }

    @Override
    public CharSequence terminateToken(CharSequence text) {
        String s = text.toString();
//        if (s.startsWith("@")) {
//            return s.substring(0, s.indexOf(',')) + " ";
//        }
//        return text + " ";
        return s;
    }}
