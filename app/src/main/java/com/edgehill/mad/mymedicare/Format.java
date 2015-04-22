package com.edgehill.mad.mymedicare;

/**
 * Created by scottwhite on 21/04/15.
 */
public final class Format {
    private Format(){};

    public static String date(int day, int month, int year){
        String strMonth, strDay, strYear;
        if (month < 10) {
            strMonth = Integer.toString(month);
            strMonth = "0" + strMonth;
        } else {
            strMonth = Integer.toString(month);
        }
        if (day < 10) {
            strDay = Integer.toString(day);
            strDay = "0" + strDay;
        } else {
            strDay = Integer.toString(day);
        }
        strYear = Integer.toString(year);

        return strYear + "" + strMonth + "" + strDay;
    }

}
