package com.budgetfirst.financialapp.presenter.locker;

import android.database.sqlite.SQLiteDatabase;

import com.budgetfirst.financialapp.model.database.ModelDatabase;

public class LockerPresenter {

    private ModelDatabase modelDatabase;

    public LockerPresenter(SQLiteDatabase database) {
        modelDatabase = new ModelDatabase(database);
    }

    public int getCodeForLocker() {
        return modelDatabase.getCodeForLocker();
    }

    public boolean isCodeForLockerInDatabase() {
        return modelDatabase.getCodeForLocker() != 0;
    }

    void saveCodeForLockerInDatabase(int code) {
        modelDatabase.saveCodeForLockerInDatabase(code);
    }

    void deleteCodeForLockerFromDatabase(int code) {
        modelDatabase.deleteCodeForLockerFromDatabase(code);
    }
}