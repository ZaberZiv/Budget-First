package com.budgetfirst.financialapp.presenter;

import android.widget.TextView;

import com.budgetfirst.financialapp.modules.NumberPanel;

import java.text.DecimalFormat;

public class PanelPresenter {

    private NumberPanel mNumberPanel;

    public PanelPresenter() {
        mNumberPanel = new NumberPanel();
    }

    public String addNumbers(String numbers, int tag) {
        return mNumberPanel.addNumbersNP(numbers, tag);
    }

    public String putPeriod(String numbers, TextView text) {
        return mNumberPanel.periodNP(numbers, text);
    }

    public String deleteCharFromPanel(String numbers, TextView text) {
        numbers = mNumberPanel.deleteOneCharNP(numbers);

        checkIfNumIsNull(numbers, text);

        return numbers;
    }

    // Formats the numbers (doubles type) which displayed on the screen
    public String customFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }

    public void checkIfNumIsNull(String numbers, TextView text) {
        if (numbers.length() == 0) {
            text.setText("");
        } else {
            text.setText(customFormat("###,###.##", Double.parseDouble(numbers)));
        }
    }

    public String customStringFormat(String format, double value) {
        return customFormat(format, value);
    }
}
