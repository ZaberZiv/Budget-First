package com.budgetfirst.financialapp.presenter.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.budgetfirst.financialapp.model.Data;
import com.budgetfirst.financialapp.model.database.ModelDatabase;

public class DatabasePresenter implements DatabaseContract.Presenter{

    private static final String TAG = "DatabasePresenter";

    private ModelDatabase moduleDatabase;
    SQLiteDatabase database;

    public DatabasePresenter(SQLiteDatabase database) {
        this.database = database;
    }

    public DatabasePresenter(SQLiteDatabase database, Context context) {
        moduleDatabase = new ModelDatabase(database, context);
    }

    @Override
    public void saveToDatabase(double income, double expence, String date, String name) {
        Data data = new Data();
        data.setIncome(income);
        data.setExpence(expence);
        data.setFormatedDate(date);
        data.setItemName(name);

        new ModelDatabase(database, data).addToDatabaseModule();
    }

    @Override
    public void deleteFromDatabase(long id) {
        moduleDatabase.deleteDatabaseModule(id);
    }

    @Override
    public boolean clearDatabase() {
        return moduleDatabase.clearDatabaseModule();
    }

    @Override
    public Cursor getCursorPresenter(
            int checkNumber, long dateLong, long monthLong, long yearLong) {
        return moduleDatabase.getCursorModuleDatabase(checkNumber, dateLong, monthLong, yearLong);
    }

}
