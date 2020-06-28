package com.budgetfirst.financialapp.presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.budgetfirst.financialapp.activities.ProfitActivity;
import com.budgetfirst.financialapp.modules.Data;
import com.budgetfirst.financialapp.modules.ModuleDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabasePresenter {

    private static final String TAG = "DatabasePresenter";

    private ModuleDatabase moduleDatabase;
    private PanelPresenter mPanelPresenter;
    private double income;
    private double expence;
    private ArrayList<String> mMonthList = new ArrayList<>();
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<String> mYearForYearList = new ArrayList<>();

    public DatabasePresenter(SQLiteDatabase database, Data data) {
        moduleDatabase = new ModuleDatabase(database, data);
    }

    public DatabasePresenter(SQLiteDatabase database, Context context) {
        moduleDatabase = new ModuleDatabase(database, context);
    }

    public void saveToDatabase() {
        moduleDatabase.addToDataBaseModule();
    }

    public void deleteFromDatabase(long id) {
        moduleDatabase.deleteDatabaseModule(id);
    }

    public boolean clearDatabase() {
        return moduleDatabase.clearDatabaseModule();
    }

    public Cursor getCursorPresenter(
            int checkNumber, long dateLong, long monthLong, long yearLong) {
        return moduleDatabase.getCursorModuleDatabase(checkNumber, dateLong, monthLong, yearLong);
    }

    public void getDataToSetTextViewsPresenter(Cursor cursor) {
        mPanelPresenter = new PanelPresenter();
        moduleDatabase.getDataFromCursor(cursor);

        income = moduleDatabase.getIncome();
        expence = moduleDatabase.getExpence();
        mMonthList = moduleDatabase.getmMonthList();
        mYearList = moduleDatabase.getmYearList();
        mYearForYearList = moduleDatabase.getmYearForYearList();
    }

    public Cursor getAllDataFromPresenter() {
       return moduleDatabase.getCursorOfAllData();
    }

    public void setExpenceTextView(TextView expenceTextView) {
        if (expence == 0.0) {
            expenceTextView.setText("0.0");
        } else {
            expenceTextView.setText(mPanelPresenter.customStringFormat("###,###.##", expence));
        }
    }

    public void setIncomeTextView(TextView incomeTextView) {
        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(mPanelPresenter.customStringFormat("###,###.##", income));
        }
    }

    public void setBalanceTextView(TextView balanceTextView) {
        balanceTextView.setText(mPanelPresenter.customStringFormat("###,###.##", (income + expence)));
    }

    public void setTotalExpenceTextView(TextView totalExpenceTextView) {
        totalExpenceTextView.setText(mPanelPresenter.customStringFormat("###,###.##", expence));
    }

    public void setTotalIncomeTextView(TextView totalIncomeTextView) {
        totalIncomeTextView.setText(mPanelPresenter.customStringFormat("###,###.##", income));
    }

    public ArrayList<String> fillArrayPresenter(ArrayList<String> dateInStringList, int num) {
        dateInStringList.clear();

        moduleDatabase.getDataFromCursor(moduleDatabase.getCursorOfAllData());
        ArrayList<String> monthList = moduleDatabase.getmMonthList();
        ArrayList<String> yearList = moduleDatabase.getmYearList();
        ArrayList<String> yearForYearList = moduleDatabase.getmYearForYearList();

        for (int i = 0; i < monthList.size(); i++) {
            SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");
            SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

            String pickMonth = "";
            String pickYear = "";
            try {
                pickMonth = monthDateFormat.format(Long.parseLong(monthList.get(i)));
                pickYear = yearDateFormat.format(Long.parseLong(yearList.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG,"yearList is empty, maybe after cleaning DB." + yearList);
            }

            //This lists goes to ListView which shows when press Month/Year button
            if (num == 0) dateInStringList.add(pickYear);
            if (num == 1) dateInStringList.add(pickMonth + " " + pickYear);
        }

        if (num == 0) {
            // removing all equals dates (Year)
            for (int i = 0; i < dateInStringList.size() - 1; i++) {
                for (int k = dateInStringList.size() - 1; k > i; k--) {
                    if (dateInStringList.get(i).equals(dateInStringList.get(k))) {
                        dateInStringList.remove(k);
                        yearForYearList.remove(k);
                    }
                }
            }
        }

        if (num == 1) {
            for (int i = 0; i < dateInStringList.size() - 1; i++) {
                for (int k = dateInStringList.size() - 1; k > i; k--) {
                    if (dateInStringList.get(i).equals(dateInStringList.get(k))) {
                        dateInStringList.remove(k);
                        monthList.remove(k);
                        yearList.remove(k);
                    }
                }
            }
        }
        return dateInStringList;
    }

    public double getIncome() {
        return income;
    }

    public double getExpence() {
        return expence;
    }

    public ArrayList<String> getmMonthList() {
        return mMonthList;
    }

    public ArrayList<String> getmYearList() {
        return mYearList;
    }

    public ArrayList<String> getmYearForYearList() {
        return mYearForYearList;
    }
}
