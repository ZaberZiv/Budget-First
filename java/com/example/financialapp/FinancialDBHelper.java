package com.example.financialapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class FinancialDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "financials.db";
    public static final int DATABASE_VERSION = 1;

    public FinancialDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FINANCIALLIST_TABLE = "CREATE TABLE " +
                FinancialContract.FinancialEntry.TABLE_NAME + " (" +
                FinancialContract.FinancialEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FinancialContract.FinancialEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FinancialContract.FinancialEntry.COLUMN_EXPENCE + " INTEGER NOT NULL, " +
                FinancialContract.FinancialEntry.COLUMN_INCOME + " INTEGER NOT NULL, " +
                FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL," +
                FinancialContract.FinancialEntry.COLUMN_MONTH + " INTEGER NOT NULL," +
                FinancialContract.FinancialEntry.COLUMN_YEAR + " INTEGER NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_FINANCIALLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FinancialContract.FinancialEntry.TABLE_NAME);
        onCreate(db);
    }
}
