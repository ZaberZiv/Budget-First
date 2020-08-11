package com.budgetfirst.financialapp.presenter.locker;

import android.database.sqlite.SQLiteDatabase;

import com.budgetfirst.financialapp.model.database.ModelDatabase;

public class LockerPresenter implements LockerContract.Presenter {

    ModelDatabase modelDatabase;

    public LockerPresenter(SQLiteDatabase database) {
        modelDatabase = new ModelDatabase(database);
    }

    @Override
    public int getCodeForLocker() {
        return modelDatabase.getCodeForLocker();
    }

    @Override
    public boolean isCodeForLockerInDatabase() {
        return modelDatabase.getCodeForLocker() != 0;
    }

    @Override
    public void saveCodeForLockerInDatabase(int code) {
        modelDatabase.saveCodeForLockerInDatabase(code);
    }

    @Override
    public void deleteCodeForLockerFromDatabase(int code) {
        modelDatabase.deleteCodeForLockerFromDatabase(code);
    }

}
