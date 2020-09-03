package com.budgetfirst.financialapp.model;

public class Data {

    private String formatedDate;
    private String itemName;
    private double expense;
    private double income;
    private String year;

    public Data() {
    }

    public Data(String itemName, double expense) {
        this.itemName = itemName;
        this.expense = expense;
    }

    public Data(String itemName, double expense, String year) {
        this.itemName = itemName;
        this.expense = expense;
        this.year = year;
    }

    public Data(String itemName, double income, double expense, String year) {
        this.itemName = itemName;
        this.income = income;
        this.expense = expense;
        this.year = year;
    }

    public Data(double income, String itemName) {
        this.expense = income;
        this.itemName = itemName;
    }

    public Data(String itemName, double expense, double income) {
        this.itemName = itemName;
        this.expense = expense;
        this.income = income;
    }

    public Data(String formatedDate, String itemName, double expense, double income) {
        this.formatedDate = formatedDate;
        this.itemName = itemName;
        this.expense = expense;
        this.income = income;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
