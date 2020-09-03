package com.budgetfirst.financialapp.presenter.panel;

public interface PanelContract {

    interface View {
        void setTextInView(String text);
    }

    interface Presenter {
        String addNumbersToThePanel(String numbers, int tag);
        String putPeriod(String numbers);
        String deleteCharFromPanel(String numbers);
    }
}
