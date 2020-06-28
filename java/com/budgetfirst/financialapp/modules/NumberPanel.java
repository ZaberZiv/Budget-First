package com.budgetfirst.financialapp.modules;

import android.util.Log;
import android.widget.TextView;

public class NumberPanel {

    private StringBuilder inputStr;

    // Нужно сделать проверку строки inputStr на то что, если она содержит точку,
    // то после нее можно будет добавить только два знака
    public String addNumbersNP(String numbers, int tag) {
        inputStr = new StringBuilder(numbers);

        String[] array = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        if (!numbers.contains("0.") || numbers.contains("0.") && numbers.length() < 4) {
            for (String s : array) {
                if (tag == Integer.parseInt(s)) {
                    if (tag == 0) {
                        if (numbers.isEmpty()) {
                            numbers = inputStr.append("0").toString();
                        } else if (numbers.contains("0.") && !numbers.equals("0.0")) {
                            numbers = inputStr.append("0").toString();
                        } else if (!numbers.equals("0")) {
                            numbers = inputStr.append("0").toString();
                        }
                    } else {
                        numbers = inputStr.append(s).toString();
                    }
                }
            }
        }
        return numbers;
    }

    public String periodNP(String numbers, TextView text) {
        inputStr = new StringBuilder(numbers);

        if (!numbers.contains(".") && numbers.length() != 0) {
            numbers = inputStr.append(".").toString();
            text.setText(numbers);
        } else if (!numbers.contains(".") && numbers.length() == 0) {
            numbers = inputStr.append("0.").toString();
            text.setText(numbers);
        }

        return numbers;
    }

    public String deleteOneCharNP(String numbers) {
        if (numbers.length() > 0) {
            numbers = numbers.substring(0, numbers.length() - 1);
        }
        return numbers;
    }
}