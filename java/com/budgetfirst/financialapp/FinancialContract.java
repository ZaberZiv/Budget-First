package com.budgetfirst.financialapp;

import android.provider.BaseColumns;

public class FinancialContract {

    private FinancialContract() {}

    public static final class FinancialEntry implements BaseColumns {
        public static final String TABLE_NAME = "financialList";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_EXPENCE = "expence";
        public static final String COLUMN_INCOME = "income";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_YEAR = "year";
    }
}