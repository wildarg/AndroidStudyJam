package com.wild.androidstudyjam.weatherforecast2.utils;/*
 * Created by Wild on 21.04.2015.
 */

public class WeatherIconManager {
        public static String getIconChar(String icon) {
            char c;
            if (icon.equals("01d"))
                c = 0xf00d;
            else if (icon.equals("02d"))
                c = 0xf002;
            else if (icon.equals("03d"))
                c = 0xf041;
            else if (icon.equals("04d"))
                c = 0xf013;
            else if (icon.equals("09d"))
                c = 0xf019;
            else if (icon.equals("10d"))
                c = 0xf008;
            else if (icon.equals("11d"))
                c = 0xf005;
            else if (icon.equals("13d"))
                c = 0xf00a;
            else if (icon.equals("50d"))
                c = 0xf014;
            else if (icon.equals("01n"))
                c = 0xf02e;
            else if (icon.equals("02n"))
                c = 0xf031;
            else if (icon.equals("03n"))
                c = 0xf041;
            else if (icon.equals("04n"))
                c = 0xf013;
            else if (icon.equals("09n"))
                c = 0xf019;
            else if (icon.equals("10n"))
                c = 0xf036;
            else if (icon.equals("11n"))
                c = 0xf033;
            else if (icon.equals("13n"))
                c = 0xf038;
            else if (icon.equals("50n"))
                c = 0xf014;
            else
                return "?";
            return String.valueOf(c);
        }
}
