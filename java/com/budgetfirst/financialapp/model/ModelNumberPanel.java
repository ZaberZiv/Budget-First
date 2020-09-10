package com.budgetfirst.financialapp.model;

public class ModelNumberPanel {

    private StringBuilder inputStr;

    public String addNumbersNP(String numbers, int tag) {
        inputStr = new StringBuilder(numbers);

        if (tag == 0) {
            if (numbers.contains(".")) {
                numbers = checkPeriod(numbers, 0);
            } else if (numbers.isEmpty() || !numbers.equals("0")) {
                numbers = inputStr.append("0").toString();
            }
        } else if (numbers.contains(".")) {
            numbers = checkPeriod(numbers, tag);
        } else {
            numbers = inputStr.append(tag).toString();
        }
        return numbers;
    }

    private String checkPeriod(String numbers, int tag) {
        String[] list = numbers.split("\\.");
        if (list.length == 1) {
            numbers = inputStr.append(tag).toString();
        } else if (list[1].length() < 2) {
            numbers = inputStr.append(tag).toString();
        }
        return numbers;
    }

    public String periodNP(String numbers) {
        inputStr = new StringBuilder(numbers);

        if (!numbers.contains(".") && numbers.length() != 0) {
            numbers = inputStr.append(".").toString();
        } else if (!numbers.contains(".") && numbers.length() == 0) {
            numbers = inputStr.append("0.").toString();
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