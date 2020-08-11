package com.budgetfirst.financialapp.presenter.panel;

import android.widget.TextView;

public interface PanelContract {

    interface View {
        void setViewsByBinding();
        double checkOfEnteredNameAndNumbers(int plusOrMinus);
        void showDatePickerDialog();
    }

    interface Presenter {
        String addNumbersToThePanel(String numbers, int tag);
        String putPeriod(String numbers, TextView text);
        String deleteCharFromPanel(String numbers, TextView text);
        String customFormat(double value);
    }
}
