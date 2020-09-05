package com.budgetfirst.financialapp.presenter.chart;

public interface ChartContract {

    interface View {
        void hideViews();
        void showViews();
        void setTextInExpenseView(String text);
        void setTextInIncomeView(String text);
        void setTextInBalanceView(String text);
    }

    interface Presenter {
        void setExpenseTextView();
        void setIncomeTextView();
        void setBalanceTextView();
    }
}
