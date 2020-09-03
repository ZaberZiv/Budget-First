package com.budgetfirst.financialapp.presenter.history;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.budgetfirst.financialapp.model.ModelFilter;
import com.budgetfirst.financialapp.utils.UtilConverter;
import com.budgetfirst.financialapp.model.database.ModelDatabase;

import java.util.ArrayList;

public class HistoryPresenter implements HistoryContract.Presenter {

    private static final String TAG = "CalculationPresenter";

    private ModelDatabase modelDatabase;
    private double income, expense;

    private ModelFilter modelFilter;
    private HistoryContract.View mView;

    public HistoryPresenter(SQLiteDatabase database, HistoryContract.View view) {
        modelDatabase = new ModelDatabase(database);
        modelFilter = new ModelFilter(database);
        mView = view;
    }

    @Override
    public void getDataToSetTextViewsPresenter(Cursor cursor) {
        modelDatabase.getDataFromCursor(cursor);

        income = modelDatabase.getIncome();
        expense = modelDatabase.getExpence();
    }

    @Override
    public Cursor getAllDataFromPresenter() {
        return modelDatabase.getCursorOfAllData();
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

    @Override
    public void setTotalExpenseTextView() {
        mView.setTextInTotalExpenseView(customFormat(expense));
    }

    @Override
    public void setTotalIncomeTextView() {
        mView.setTextInTotalIncomeView(customFormat(income));
    }

    public ArrayList<String> fillArrayMonth() {
        return modelFilter.fillArrayMonth();
    }

    public ArrayList<String> fillArrayYear() {
        return modelFilter.fillArrayYear();
    }

    public ArrayList<String> fillArrayDay() {
        return modelFilter.fillArrayDay();
    }

    @Override
    public String customFormat(double value) {
        return UtilConverter.customStringFormat(value);
    }

    public double getIncome() {
        return income;
    }

    public double getExpence() {
        return expense;
    }

    public ArrayList<String> getmMonthList() {
        return modelFilter.getmMonthList();
    }

    public ArrayList<String> getmYearList() {
        return modelFilter.getmYearList();
    }

    public ArrayList<String> getmYearForYearList() {
        return modelFilter.getmYearForYearList();
    }

    public ArrayList<String> getmDateList() {
        return modelFilter.getmDateList();
    }
}
