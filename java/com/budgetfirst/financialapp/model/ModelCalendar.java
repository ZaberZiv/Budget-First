package com.budgetfirst.financialapp.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import java.util.Calendar;

public class ModelCalendar {

    public static void getCalendarModule(DatePickerDialog.OnDateSetListener dateSetListener, Context context) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static String dateFormatModule(int dayOfMonth, int month, int year) {
        String formatedDate;
        if (String.valueOf(month).length() == 1
                && String.valueOf(dayOfMonth).length() == 1) {
            formatedDate = "0" + dayOfMonth + "/" + "0" + month + "/" + year;
        } else if (String.valueOf(month).length() == 1) {
            formatedDate = dayOfMonth + "/" + "0" + month + "/" + year;
        } else if (String.valueOf(dayOfMonth).length() == 1) {
            formatedDate = "0" + dayOfMonth + "/" + month + "/" + year;
        } else {
            formatedDate = dayOfMonth + "/" + month + "/" + year;
        }
        return formatedDate;
    }
}
