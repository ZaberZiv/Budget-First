package com.budgetfirst.financialapp.presenter.database;

import android.database.Cursor;

public interface DatabaseContract {

    interface View {

    }

    interface Presenter {
        void saveToDatabase(double income, double expence, String date, String name);
        void deleteFromDatabase(long id);
        boolean clearDatabase();
        Cursor getCursorPresenter(
                int checkNumber, long dateLong, long monthLong, long yearLong);
    }
}
