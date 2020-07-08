package com.budgetfirst.financialapp.presenter.calculation;

import android.database.Cursor;
import android.widget.TextView;

import java.util.ArrayList;

public interface CalculationContract {

    interface View {
        void setViewsByBinding();
        void showDatePickerDialog();
    }

    interface Presenter {
        void getDataToSetTextViewsPresenter(Cursor cursor);
        Cursor getAllDataFromPresenter();
        void setExpenceTextView(TextView expenceTextView);
        void setIncomeTextView(TextView incomeTextView);
        void setBalanceTextView(TextView balanceTextView);
        void setTotalExpenceTextView(TextView totalExpenceTextView);
        void setTotalIncomeTextView(TextView totalIncomeTextView);
        ArrayList<String> fillArrayPresenter(ArrayList<String> dateInStringList, int num);
        String customFormat(String pattern, double value);
    }
}
