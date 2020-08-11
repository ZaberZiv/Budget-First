package com.budgetfirst.financialapp.presenter.calculation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.budgetfirst.financialapp.model.ModelFilter;
import com.budgetfirst.financialapp.utils.UtilConverter;
import com.budgetfirst.financialapp.model.database.ModelDatabase;

import java.util.ArrayList;

public class CalculationPresenter implements CalculationContract.Presenter {

    private static final String TAG = "CalculationPresenter";

    private ModelDatabase modelDatabase;
    private double income, expense;

    private ModelFilter modelFilter;

    public CalculationPresenter(SQLiteDatabase database) {
        modelDatabase = new ModelDatabase(database);
        modelFilter = new ModelFilter(database);
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
    public void setExpenseTextView(TextView expenseTextView) {
        if (expense == 0.0) {
            expenseTextView.setText("0.0");
        } else {
            expenseTextView.setText(customFormat(expense));
        }
    }

    @Override
    public void setIncomeTextView(TextView incomeTextView) {
        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(customFormat(income));
        }
    }

    @Override
    public void setBalanceTextView(TextView balanceTextView) {
        balanceTextView.setText(customFormat((income + expense)));
    }

    @Override
    public void setTotalExpenseTextView(TextView totalExpenseTextView) {
        totalExpenseTextView.setText(customFormat(expense));
    }

    @Override
    public void setTotalIncomeTextView(TextView totalIncomeTextView) {
        totalIncomeTextView.setText(customFormat(income));
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
