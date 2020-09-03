package com.budgetfirst.financialapp.presenter.data;

import android.database.Cursor;

public interface DatabaseContract {

    interface View {

    }

    interface Presenter {
        void saveToDatabase(double income, double expense, String date, String name);
        void deleteFromDatabase(long id);
        boolean clearDatabase();
        Cursor getCursorForSelectedDate(
                int checkNumber, long dateLong, long monthLong, long yearLong);
    }
}
