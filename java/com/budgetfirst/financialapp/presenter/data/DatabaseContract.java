package com.budgetfirst.financialapp.presenter.data;

import android.database.Cursor;

import com.budgetfirst.financialapp.model.filter.DataFilter;

public interface DatabaseContract {

    interface View {

    }

    interface Presenter {
        void saveToDatabase(double income, double expense, String date, String name);
        void deleteFromDatabase(long id);
        boolean clearDatabase();
        Cursor getCursorForSelectedDate(DataFilter dataFilter);
    }
}
