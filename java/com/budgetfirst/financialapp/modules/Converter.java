package com.budgetfirst.financialapp.modules;

import android.util.Log;

import java.text.SimpleDateFormat;

public class Converter {

    private static final String TAG = "Converter";

    public static long convertStringToLongDate(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInString).getTime();
            Log.i(TAG, "1: DD/MM/YYYY: " + longDate);
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
            Log.i(TAG, "2: Month: " + longDate);
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
            Log.i(TAG, "3: YYYY: " + longDate);
        } catch (Exception e) {
            Log.e(TAG, "Check the conversion of Date in convertStringToLongYear()");
            e.printStackTrace();
        }
        return longDate;
    }
}
