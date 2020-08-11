package com.budgetfirst.financialapp.presenter.calculation;

import android.database.Cursor;
import android.widget.TextView;

import java.util.ArrayList;

public interface CalculationContract {

    interface View {
        void setViewsByBinding();
    }

    interface Presenter {
        void getDataToSetTextViewsPresenter(Cursor cursor);
        Cursor getAllDataFromPresenter();
        void setExpenseTextView(TextView expenseTextView);
        void setIncomeTextView(TextView incomeTextView);
        void setBalanceTextView(TextView balanceTextView);
        void setTotalExpenseTextView(TextView totalExpenseTextView);
        void setTotalIncomeTextView(TextView totalIncomeTextView);
        String customFormat(double value);
    }
}
