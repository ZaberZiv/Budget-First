package com.budgetfirst.financialapp.model;

public class Data {

    private String formatedDate;
    private String itemName;
    private double expence;
    private double income;
    private String year;

    public Data() {
    }

    public Data(String itemName, double expence) {
        this.itemName = itemName;
        this.expence = expence;
    }

    public Data(String itemName, double expence, String year) {
        this.itemName = itemName;
        this.expence = expence;
        this.year = year;
    }

    public Data(String itemName, double income, double expence, String year) {
        this.itemName = itemName;
        this.income = income;
        this.expence = expence;
        this.year = year;
    }

    public Data(double income, String itemName) {
        this.expence = income;
        this.itemName = itemName;
    }

    public Data(String itemName, double expence, double income) {
        this.itemName = itemName;
        this.expence = expence;
        this.income = income;
    }

    public Data(String formatedDate, String itemName, double expence, double income) {
        this.formatedDate = formatedDate;
        this.itemName = itemName;
        this.expence = expence;
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

    public double getExpence() {
        return expence;
    }

    public void setExpence(double expence) {
        this.expence = expence;
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
