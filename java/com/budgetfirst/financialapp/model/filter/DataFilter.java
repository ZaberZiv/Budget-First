package com.budgetfirst.financialapp.model.filter;

public class DataFilter {

    private int checkNumber;
    private long dateLong, monthLong, yearLong;

    public DataFilter() {
    }

    public DataFilter(int checkNumber, long dateLong, long monthLong, long yearLong) {
        this.checkNumber = checkNumber;
        this.dateLong = dateLong;
        this.monthLong = monthLong;
        this.yearLong = yearLong;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public long getMonthLong() {
        return monthLong;
    }

    public void setMonthLong(long monthLong) {
        this.monthLong = monthLong;
    }

    public long getYearLong() {
        return yearLong;
    }

    public void setYearLong(long yearLong) {
        this.yearLong = yearLong;
    }
}
