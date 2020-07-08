package com.budgetfirst.financialapp.presenter.panel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.budgetfirst.financialapp.model.ModelCalendar;
import com.budgetfirst.financialapp.model.ModelConverter;
import com.budgetfirst.financialapp.model.ModelNumberPanel;

import java.text.DecimalFormat;

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
            text.setText(customFormat("###,###.##", Double.parseDouble(numbers)));
        }
    }

    // Formats the numbers (double type) which displayed on the screen
    @Override
    public String customFormat(String pattern, double value) {
        return ModelConverter.customStringFormat(pattern, value);
    }

    public void getCalendarModule(DatePickerDialog.OnDateSetListener dateSetListener, Context context) {
        ModelCalendar.getCalendarModule(dateSetListener, context);
    }

    public String dateFormatModule(int dayOfMonth, int month, int year) {
        return ModelCalendar.dateFormatModule(dayOfMonth, month, year);
    }
}
