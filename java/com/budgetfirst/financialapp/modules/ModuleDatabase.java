package com.budgetfirst.financialapp.modules;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.database.FinancialContract;

import java.util.ArrayList;

public class ModuleDatabase {

    private static final String TAG = "ModuleDatabase";
    private SQLiteDatabase database;
    private Data data;
    private Context context;

    private boolean flag;
    private double income;
    private double expence;
    private ArrayList<String> mMonthList = new ArrayList<>();
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<String> mYearForYearList = new ArrayList<>();

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpence() {
        return expence;
    }

    public void setExpence(double expence) {
        this.expence = expence;
    }

    public ModuleDatabase(SQLiteDatabase database, Data data) {
        this.database = database;
        this.data = data;
    }

    public ModuleDatabase(SQLiteDatabase database, Context context) {
        this.database = database;
        this.context = context;
    }

    public void addToDataBaseModule() {
        long longDateDB = Converter.convertStringToLongDate(data.getFormatedDate());
        long longMonthDB = Converter.convertStringToLongMonth(longDateDB);
        long longYearDB = Converter.convertStringToLongYear(longDateDB);

        ContentValues cv = new ContentValues();
        cv.put(FinancialContract.FinancialEntry.COLUMN_TITLE, data.getItemName());
        cv.put(FinancialContract.FinancialEntry.COLUMN_EXPENCE, data.getExpence());
        cv.put(FinancialContract.FinancialEntry.COLUMN_INCOME, data.getIncome());
        cv.put(FinancialContract.FinancialEntry.COLUMN_TIMESTAMP, longDateDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_MONTH, longMonthDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_YEAR, longYearDB);
        database.insert(FinancialContract.FinancialEntry.TABLE_NAME, null, cv);
    }

    public void deleteDatabaseModule(long id) {
        database.delete(FinancialContract.FinancialEntry.TABLE_NAME,
                FinancialContract.FinancialEntry._ID + "=" + id, null);
    }

    public boolean clearDatabaseModule() {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .setTitle(R.string.title_delete_db)
                .setMessage(R.string.title_delete_message)
                .setPositiveButton(R.string.title_delete_positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.delete(FinancialContract.FinancialEntry.TABLE_NAME, null, null);
                        flag = true;
                        Toast.makeText(context, R.string.toast_delete_confirmation, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.toast_delete_rejection, null)
                .show();
        return flag;
    }

    public Cursor getCursorModuleDatabase(
            int checkNumber, long dateLong, long monthLong, long yearLong) {
        String selectionDay = FinancialContract.FinancialEntry.COLUMN_TIMESTAMP
                + " = " + dateLong;

        String selectionMonthAndYear = FinancialContract.FinancialEntry.COLUMN_MONTH
                + " = "
                + monthLong
                + " AND " + FinancialContract.FinancialEntry.COLUMN_YEAR
                + " = " + yearLong;

        String selectionYear = FinancialContract.FinancialEntry.COLUMN_YEAR
                + " = " + yearLong;

        if (checkNumber == 1) {
            return database.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionDay,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 2) {
            return database.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionMonthAndYear,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 3) {
            return database.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionYear,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else {
            return database.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        }
    }

    public Cursor getCursorOfAllData() {
        return database.rawQuery("SELECT * FROM " + FinancialContract.FinancialEntry.TABLE_NAME, null);
    }

    public void getDataFromCursor(Cursor cursor) {
        ArrayList<String> amountCheckDB = new ArrayList<>();
        ArrayList<String> incomeCheckDB = new ArrayList<>();

        int expenceIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
        int incomeIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);
        int monthIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_MONTH);
        int yearIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_YEAR);

        if (cursor.moveToFirst()) {
            amountCheckDB.clear();
            incomeCheckDB.clear();
            mMonthList.clear();
            mYearList.clear();
            mYearForYearList.clear();
            do {
                amountCheckDB.add(cursor.getString(expenceIndex));
                incomeCheckDB.add(cursor.getString(incomeIndex));
                mMonthList.add(cursor.getString(monthIndex));
                mYearList.add(cursor.getString(yearIndex)); // contains date (Year column) in long type
                mYearForYearList.add(cursor.getString(yearIndex)); // contains date (Year column) in long type
            } while (cursor.moveToNext());
        }
        cursor.close();

        expence = 0.0;
        income = 0.0;

        for (int i = 0; i < amountCheckDB.size(); i++) {
            expence += Double.parseDouble(amountCheckDB.get(i));
        }

        for (int i = 0; i < incomeCheckDB.size(); i++) {
            income += Double.parseDouble(incomeCheckDB.get(i));
        }
    }

    public ArrayList<String> getmMonthList() {
        return mMonthList;
    }

    public void setmMonthList(ArrayList<String> mMonthList) {
        this.mMonthList = mMonthList;
    }

    public ArrayList<String> getmYearList() {
        return mYearList;
    }

    public void setmYearList(ArrayList<String> mYearList) {
        this.mYearList = mYearList;
    }

    public ArrayList<String> getmYearForYearList() {
        return mYearForYearList;
    }

    public void setmYearForYearList(ArrayList<String> mYearForYearList) {
        this.mYearForYearList = mYearForYearList;
    }
}