package com.edgehill.mad.mymedicare;

/**
 * Convenience class used to format three strings returned by the date picker into a string object
 * that is suitable for entering into the database.
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
