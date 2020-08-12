package com.budgetfirst.financialapp.presenter.floatingbutton;

import android.widget.Button;

public interface FloatingButtonContract {
    interface View {
        Button getmYearBtn();
        Button getmMonthBtn();
        Button getmDayBtn();
        Button getmShowAllBtn();
    }
}
