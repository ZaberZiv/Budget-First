package com.budgetfirst.financialapp.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.budgetfirst.financialapp.model.database.ModelDatabase;
import com.budgetfirst.financialapp.utils.UtilConverter;

import java.util.ArrayList;

public class ModelFilter {

    public static final String TAG = "ModelFilter";

    private ArrayList<String> mMonthList, mYearList, mYearForYearList, mDateList;
    private ModelDatabase modelDatabase;

    public ModelFilter(SQLiteDatabase database) {
        modelDatabase = new ModelDatabase(database);
    }

    public ArrayList<String> fillArrayMonth() {
        ArrayList<String> dateInStringList = new ArrayList<>();

        modelDatabase.getDataFromCursor(modelDatabase.getCursorOfAllData());

        mMonthList = modelDatabase.getmMonthList();
        mYearList = modelDatabase.getmYearList();

        for (int i = 0; i < mMonthList.size(); i++) {
            String pickMonth = "";
            String pickYear = "";

            try {
                pickMonth = UtilConverter.dateFormatMonth(Long.parseLong(mMonthList.get(i)));
                pickYear = UtilConverter.dateFormatYear(Long.parseLong(mYearList.get(i)));

            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "yearList is empty, maybe after cleaning DB." + mYearList);
            }

            dateInStringList.add(pickMonth + " " + pickYear);
        }

        removeDuplicatesForMonth(dateInStringList, mMonthList, mYearList);

        return dateInStringList;
    }

    public ArrayList<String> fillArrayYear() {
        ArrayList<String> dateInStringList = new ArrayList<>();

        modelDatabase.getDataFromCursor(modelDatabase.getCursorOfAllData());
        mYearForYearList = modelDatabase.getmYearForYearList();

        for (int i = 0; i < mYearForYearList.size(); i++) {
            String pickYear = "";

            try {
                pickYear = UtilConverter.dateFormatYear(Long.parseLong(mYearForYearList.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "yearList is empty, maybe after cleaning DB." + mYearForYearList);
            }

            dateInStringList.add(pickYear);
        }

        removeDuplicatesInArray(dateInStringList);
        removeDuplicatesInArray(mYearForYearList);

        return dateInStringList;
    }

    public ArrayList<String> fillArrayDay() {
        ArrayList<String> dateInStringList = new ArrayList<>();

        modelDatabase.getDataFromCursor(modelDatabase.getCursorOfAllData());
        mDateList = modelDatabase.getmDateList();

        for (int i = 0; i < mDateList.size(); i++) {
            String pickDate = "";

            try {
                pickDate = UtilConverter.dateFormatDayMonthYear(Long.parseLong(mDateList.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "yearList is empty, maybe after cleaning DB." + mDateList);
            }

            dateInStringList.add(pickDate);
        }

        removeDuplicatesInArray(dateInStringList);
        removeDuplicatesInArray(mDateList);

        return dateInStringList;
    }

    private void removeDuplicatesInArray(ArrayList<String> list) {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int k = list.size() - 1; k > i; k--) {
                if (list.get(i).equals(list.get(k))) {
                    list.remove(k);
                }
            }
        }
    }

    private void removeDuplicatesForMonth(ArrayList<String> arrayList,
                                          ArrayList<String> arrayListTwo,
                                          ArrayList<String> arrayListThree) {

        for (int i = 0; i < arrayList.size() - 1; i++) {
            for (int k = arrayList.size() - 1; k > i; k--) {
                if (arrayList.get(i).equals(arrayList.get(k))) {
                    arrayList.remove(k);
                    arrayListTwo.remove(k);
                    arrayListThree.remove(k);
                }
            }
        }
    }

    public ArrayList<String> getmMonthList() {
        fillArrayMonth();
        return mMonthList;
    }

    public ArrayList<String> getmYearList() {
        fillArrayMonth();
        return mYearList;
    }

    public ArrayList<String> getmYearForYearList() {
        fillArrayYear();
        return mYearForYearList;
    }

    public ArrayList<String> getmDateList() {
        fillArrayDay();
        return mDateList;
    }
}
