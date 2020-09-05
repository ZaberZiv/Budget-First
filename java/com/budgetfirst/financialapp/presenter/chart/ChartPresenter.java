package com.budgetfirst.financialapp.presenter.chart;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.budgetfirst.financialapp.model.filter.ModelFilter;
import com.budgetfirst.financialapp.model.database.ModelDatabase;
import com.budgetfirst.financialapp.utils.UtilConverter;

import java.util.ArrayList;

public class ChartPresenter implements ChartContract.Presenter {

    private static final String TAG = "ChartPresenter";

    private ChartContract.View mView;
    private ModelFilter mModelFilter;
    private ModelDatabase modelDatabase;
    private double income, expense;

    public ChartPresenter(SQLiteDatabase database, ChartContract.View view) {
        modelDatabase = new ModelDatabase(database);
        mModelFilter = new ModelFilter(database);
        mView = view;
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

    @Override
    public void setExpenseTextView() {
        if (expense == 0.0) {
            mView.setTextInExpenseView("0.0");
        } else {
            mView.setTextInExpenseView(customFormat(expense));
        }
    }

    @Override
    public void setIncomeTextView() {
        if (income == 0.0) {
            mView.setTextInIncomeView("0.0");
        } else {
            mView.setTextInIncomeView(customFormat(income));
        }
    }

    @Override
    public void setBalanceTextView() {
        mView.setTextInBalanceView(customFormat((income + expense)));
    }
}