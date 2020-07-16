package com.budgetfirst.financialapp.model;

public class ModelNumberPanel {

    private StringBuilder inputStr;

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