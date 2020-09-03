package com.budgetfirst.financialapp.presenter.history;

import android.database.Cursor;

public interface HistoryContract {

    interface View {
        void setViewsByBinding();
        void setTextInExpenseView(String text);
        void setTextInIncomeView(String text);
        void setTextInBalanceView(String text);
        void setTextInTotalExpenseView(String text);
        void setTextInTotalIncomeView(String text);
    }

    interface Presenter {
        void getDataToSetTextViewsPresenter(Cursor cursor);
        Cursor getAllDataFromPresenter();
        void setExpenseTextView();
        void setIncomeTextView();
        void setBalanceTextView();
        void setTotalExpenseTextView();
        void setTotalIncomeTextView();
        String customFormat(double value);
    }
}
