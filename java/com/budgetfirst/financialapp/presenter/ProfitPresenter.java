package com.budgetfirst.financialapp.presenter;

import com.budgetfirst.financialapp.modules.Converter;

public class ProfitPresenter {

    public long getLongDate(String date) {
        return Converter.convertStringToLongDate(date);
    }
}
