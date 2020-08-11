package com.budgetfirst.financialapp.presenter.panel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.budgetfirst.financialapp.utils.UtilCalendar;
import com.budgetfirst.financialapp.utils.UtilConverter;
import com.budgetfirst.financialapp.model.ModelNumberPanel;

public class PanelPresenter implements PanelContract.Presenter {

    private ModelNumberPanel mNumberPanel;

    public PanelPresenter() {
        mNumberPanel = new ModelNumberPanel();
    }

    @Override
    public String addNumbersToThePanel(String numbers, int tag) {
        return mNumberPanel.addNumbersNP(numbers, tag);
    }

    @Override
    public String putPeriod(String numbers, TextView text) {
        numbers = mNumberPanel.periodNP(numbers);
        text.setText(numbers);
        return numbers;
    }

    @Override
    public String deleteCharFromPanel(String numbers, TextView text) {
        numbers = mNumberPanel.deleteOneCharNP(numbers);
        checkIfNumIsNull(numbers, text);
        return numbers;
    }

    public void checkIfNumIsNull(String numbers, TextView text) {
        if (numbers.length() == 0) {
            text.setText("");
        } else {
            text.setText(customFormat(Double.parseDouble(numbers)));
        }
    }

    // Formats the numbers (double type) which displayed on the screen
    @Override
    public String customFormat(double value) {
        return UtilConverter.customStringFormat(value);
    }

    public void getCalendarModule(DatePickerDialog.OnDateSetListener dateSetListener, Context context) {
        UtilCalendar.getCalendarModule(dateSetListener, context);
    }

    public String dateFormatModule(int dayOfMonth, int month, int year) {
        return UtilCalendar.dateFormatModule(dayOfMonth, month, year);
    }
}