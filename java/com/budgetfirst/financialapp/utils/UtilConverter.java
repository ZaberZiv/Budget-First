package com.budgetfirst.financialapp.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class UtilConverter {

    private static final String TAG = "UtilConverter";

    public static long convertStringToLongDate(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e(TAG, "Check the convertion of Date in convertStringToLongDate()");
            e.printStackTrace();
        }
        return longDate;
    }

    public static long convertStringToLongMonth(long longDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        String pickMonth = simpleDateFormat.format(longDate);
        try {
            longDate = new SimpleDateFormat("MMMM").parse(pickMonth).getTime();
        } catch (Exception e) {
            Log.e(TAG, "Check the convertion of Date in convertStringToLongMonth()");
            e.printStackTrace();
        }
        return longDate;
    }

    public static long convertStringToLongYear(long longDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String pickYear = simpleDateFormat.format(longDate);
        try {
            longDate = new SimpleDateFormat("yyyy").parse(pickYear).getTime();
        } catch (Exception e) {
            Log.e(TAG, "Check the conversion of Date in convertStringToLongYear()");
            e.printStackTrace();
        }
        return longDate;
    }

    public static long convertStringToLongMMYY(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("MMMM yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e(TAG, "Check the convertion of Date in convertStringToLongDate()");
            e.printStackTrace();
        }
        return longDate;
    }

    public static String convertStringToStringMMYY(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("MMMM yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e(TAG, "Check the convertion of Date in convertStringToLongMMYY()");
            e.printStackTrace();
        }

        return String.valueOf(longDate);
    }

    public static String customStringFormat(double value) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.##");
        return myFormatter.format(value);
    }

    public static String dateFormatYear(long l) {
        return new SimpleDateFormat("yyyy").format(l);
    }

    public static String dateFormatMonth(long l) {
        return new SimpleDateFormat("MMMM").format(l);
    }

    public static String dateFormatMonthAndYear(long l) {
        return new SimpleDateFormat("MMMM yyyy").format(l);
    }

    public static String dateFormatDayMonthYear(long l) {
        return new SimpleDateFormat("dd/MM/yyyy").format(l);
    }
}
