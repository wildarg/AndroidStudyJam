package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 06.05.2015.
 */

import android.text.Spannable;
import android.text.util.Linkify;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashtagUtils {
    private static final Pattern hashtagPattern = Pattern.compile("#\\w+", Pattern.UNICODE_CASE);
    private static final String hashtagScheme = "hashtag://";

    public static void addLinks(TextView textView) {
        Linkify.addLinks(textView, hashtagPattern, hashtagScheme);
    }

    public static void addLinks(Spannable s) {
        Linkify.addLinks(s, hashtagPattern, hashtagScheme);
    }

    public static List<String> getTags(String text) {
        List<String> tags = new ArrayList<String>();
        final Matcher matcher = hashtagPattern.matcher(text);
        while (matcher.find()) {
            String tag = matcher.group();
            tags.add(tag);
        }
        return tags;
    }

}
