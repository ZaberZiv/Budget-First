package com.budgetfirst.financialapp.presenter.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.budgetfirst.financialapp.model.ModelFilter;
import com.budgetfirst.financialapp.model.database.ModelDatabase;
import com.budgetfirst.financialapp.utils.UtilConverter;

import java.util.ArrayList;

public class ChartPresenter {
    private static final String TAG = "ChartPresenter";

    private ModelFilter mModelFilter;
    private ModelDatabase modelDatabase;
    private double income, expense;

    public ChartPresenter(SQLiteDatabase database) {
        modelDatabase = new ModelDatabase(database);
        mModelFilter = new ModelFilter(database);
    }

    public ArrayList<String> fillArrayMonth() {
        return mModelFilter.fillArrayMonth();
    }

    public ArrayList<String> fillArrayYear() {
        return mModelFilter.fillArrayYear();
    }

    public ArrayList<String> fillArrayDay() {
        return mModelFilter.fillArrayDay();
    }

    public ArrayList<String> getmMonthList() {
        return mModelFilter.getmMonthList();
    }

    public ArrayList<String> getmYearList() {
        return mModelFilter.getmYearList();
    }

    public ArrayList<String> getmYearForYearList() {
        return mModelFilter.getmYearForYearList();
    }

    public ArrayList<String> getmDateList() {
        return mModelFilter.getmDateList();
    }

    public String customFormat(double value) {
        return UtilConverter.customStringFormat(value);
    }

    public void getDataToSetTextViewsPresenter(Cursor cursor) {
        modelDatabase.getDataFromCursor(cursor);

        income = modelDatabase.getIncome();
        expense = modelDatabase.getExpence();
    }

    public void setExpenseTextView(TextView expenseTextView) {
        if (expense == 0.0) {
            expenseTextView.setText("0.0");
        } else {
            expenseTextView.setText(customFormat(expense));
        }
    }

    public void setIncomeTextView(TextView incomeTextView) {
        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(customFormat(income));
        }
    }

    public void setBalanceTextView(TextView balanceTextView) {
        balanceTextView.setText(customFormat((income + expense)));
    }
}
