package com.budgetfirst.financialapp.presenter.panel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.budgetfirst.financialapp.utils.UtilCalendar;
import com.budgetfirst.financialapp.utils.UtilConverter;
import com.budgetfirst.financialapp.model.ModelNumberPanel;

public class PanelPresenter implements PanelContract.Presenter {

    private ModelNumberPanel mNumberPanel;
    private PanelContract.View mView;

    public PanelPresenter() {
    }

    public PanelPresenter(PanelContract.View view) {
        mNumberPanel = new ModelNumberPanel();
        mView = view;
    }

    @Override
    public String addNumbersToThePanel(String numbers, int tag) {
        return mNumberPanel.addNumbersNP(numbers, tag);
    }

    @Override
    public String putPeriod(String numbers) {
        numbers = mNumberPanel.periodNP(numbers);
        mView.setTextInView(numbers);
        return numbers;
    }

    @Override
    public String deleteCharFromPanel(String numbers) {
        numbers = mNumberPanel.deleteOneCharNP(numbers);
        checkIfNumIsNull(numbers);
        return numbers;
    }

    public void checkIfNumIsNull(String numbers) {
        if (numbers.length() == 0) {
            mView.setTextInView("");
        } else {
            mView.setTextInView(customFormat(Double.parseDouble(numbers)));
        }
    }

    // Formats the numbers (double type) which displayed on the screen
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