package com.homesoftwaretools.portmone.utils;/*
 * Created by Wild on 07.05.2015.
 */

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatUtils {

    public static final SimpleDateFormat JOURNAL_ITEM_EDITOR_FORMAT = new SimpleDateFormat("EEEE, dd MMM, yyyy", Locale.getDefault());
    public static final SimpleDateFormat DRAWER_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

}
