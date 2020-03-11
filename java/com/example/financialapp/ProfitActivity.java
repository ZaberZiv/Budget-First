package com.example.financialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfitActivity extends AppCompatActivity {

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;
    private ArrayAdapter adapterForListView;
    private RecyclerView recyclerView;

    TextView mDisplayDate;
    TextView incomeTextView;
    TextView expenceTextView;
    TextView balanceTextView;
    TextView totalIncomeTextView;
    TextView totalExpenceTextView;
    LinearLayout linearLayout;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    RelativeLayout relativeLayout;
    ListView listView;
    Button buttonDay;

    private String currentDate;
    static int checkNumber;
    static long longMonth;
    static long longYear;
    static long dateLong;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private SimpleDateFormat monthDateFormat;
    private SimpleDateFormat yearDateFormat;

    static private ArrayList<String> amountCheckDB = new ArrayList<>();
    static private ArrayList<String> incomeCheckDB = new ArrayList<>();
    static private ArrayList<String> mMonthInStringList = new ArrayList<>();
    static private ArrayList<String> mYearInStringList = new ArrayList<>();
    static private ArrayList<String> monthList = new ArrayList<>();
    static private ArrayList<String> yearList = new ArrayList<>();
    static private ArrayList<String> yearForYearList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        incomeTextView = (TextView) findViewById(R.id.incomeTextView);
        expenceTextView = (TextView) findViewById(R.id.expenceTextView);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        totalIncomeTextView = (TextView) findViewById(R.id.totalIncomeTextView);
        totalExpenceTextView = (TextView) findViewById(R.id.totalExpenceTextView);
        mDisplayDate = (TextView) findViewById(R.id.showDateTextView);

        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout4);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        buttonDay = (Button) findViewById(R.id.btnDay);

        //Show the Date
        showDatePickerDialog();

        //Database Helper
        FinancialDBHelper dbHelper = new FinancialDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        showTotalFromDB();

        //RecycleView
        doRecycleView();

        //Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    // RecycleView
    public void doRecycleView() {
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpenceAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
    }

    // Removing items from RecycleView and Database
    private void removeItem(long id) {
        mDatabase.delete(FinancialContract.FinancialEntry.TABLE_NAME,
                FinancialContract.FinancialEntry._ID + "=" + id, null);

        mAdapter.swapCursor(getAllItems());
    }

    // Adding Cursor to the RecycleView (doRecycleView() method)
    public Cursor getAllItems() {
        String selectionDay = FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " = " + dateLong;
        String selectionMonthAndYear = FinancialContract.FinancialEntry.COLUMN_MONTH + " = " + longMonth + " AND " + FinancialContract.FinancialEntry.COLUMN_YEAR + " = " + longYear;
        String selectionYear = FinancialContract.FinancialEntry.COLUMN_YEAR + " = " + longYear;

        if (checkNumber == 1) {
            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionDay,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 2) {
            Log.e("getAllItems", "=- 2 -=");

            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionMonthAndYear,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 3) {
            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selectionYear,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else {
            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        }
    }

    // Buttons Year, Month, Day, Show All
    public void filterClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMonth:
                checkNumber = 2;
                Log.e("filterClicked", "=- 1 -=");
                cursorDataBase(getAllItems());
                showListView(mMonthInStringList, yearList);
                break;

            case R.id.btnYear:
                checkNumber = 3;
                cursorDataBase(getAllItems());
                showListView(mYearInStringList, yearForYearList);
                break;

            default:
                checkNumber = 0;
                mDisplayDate.setText("ALL");
                doRecycleView();
                break;
        }
    }

    // ListView + Adapter для
    public void showListView(final ArrayList<String> listForListView, final ArrayList<String> listForLongYear) {
        linearLayout = findViewById(R.id.showMonth);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);
        Log.e("showListView", "=- 6 -=");

        listView = (ListView) findViewById(R.id.listView);
        // Сюда должен передаваться список с датой (Месяц + Год или просто Год в зависимости от нажатой кнопки)
        adapterForListView = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listForListView);
        listView.setAdapter(adapterForListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                longMonth = Long.parseLong(monthList.get(position));
                longYear = Long.parseLong(listForLongYear.get(position));

                mDisplayDate.setText(listForListView.get(position));
                Log.e("onItemClick", "=- 7 -=");

                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                doRecycleView();
            }
        });
    }

    // Calendar
    public void showDatePickerDialog() {
        //Showing the date that Uses have set in MainActivity
        currentDate = MainActivity.formatedDate;
        mDisplayDate.setText(currentDate);

        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProfitActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                currentDate = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(currentDate);

                dateLong = convertStringToLongDate(currentDate);
//                cursorDataBase(dateLong);
                checkNumber = 1;
                cursorDataBase(getAllItems());

                doRecycleView();
            }
        };
    }

    static public long convertStringToLongDate(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }
        return longDate;
    }

    // Показывает суммы на экране по выбранному дню
    public void cursorDataBase(Cursor cursor) {
        Log.e("cursorDataBase", "=- 3 -=");

        int expenceIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
        int incomeIndex = cursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);

        if (cursor.moveToFirst()) {
            amountCheckDB.clear();
            incomeCheckDB.clear();

            do {
                amountCheckDB.add(cursor.getString(expenceIndex));
                incomeCheckDB.add(cursor.getString(incomeIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Показывает на экране суммы за определенный день
        double income = 0.0;
        double expence = 0.0;

        for (int i = 0; i < amountCheckDB.size(); i++) {
            expence += Double.parseDouble(amountCheckDB.get(i));
            Log.e("amountCheckDB", "=- 4 -= " + amountCheckDB);
        }

        for (int i = 0; i < incomeCheckDB.size(); i++) {
            income += Double.parseDouble(incomeCheckDB.get(i));
            Log.e("incomeCheckDB", "=- 5 -= " + incomeCheckDB);
        }

        if (expence == 0.0) {
            expenceTextView.setText("0.0");
        } else {
            expenceTextView.setText(String.valueOf(expence));
        }

        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(String.valueOf(income));
        }
    }

//    public void cursorDataBase(long userDate) {
//        Cursor c = mDatabase.rawQuery("SELECT * FROM " + FinancialContract.FinancialEntry.TABLE_NAME +
//                " WHERE " + FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " = " + userDate, null);
//
//        int expenceIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
//        int incomeIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);
//
//        if (c.moveToFirst()) {
//            amountCheckDB.clear();
//            incomeCheckDB.clear();
//
//            do {
//                amountCheckDB.add(c.getString(expenceIndex));
//                incomeCheckDB.add(c.getString(incomeIndex));
//            } while (c.moveToNext());
//        }
//        c.close();
//
//        // Показывает на экране суммы за определенный день
//        double income = 0.0;
//        double expence = 0.0;
//
//        for (int i = 0; i < amountCheckDB.size(); i++) {
//            expence += Double.parseDouble(amountCheckDB.get(i));
//        }
//
//        for (int i = 0; i < incomeCheckDB.size(); i++) {
//            income += Double.parseDouble(incomeCheckDB.get(i));
//        }
//
//        incomeTextView.setText(String.valueOf(income));
//        expenceTextView.setText(String.valueOf(expence));
//    }


    public void showTotalFromDB() {
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + FinancialContract.FinancialEntry.TABLE_NAME, null);

        int incomeIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);
        int expenceIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
        int monthIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_MONTH);
        int yearIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_YEAR);

        if (c.moveToFirst()) {
            amountCheckDB.clear();
            incomeCheckDB.clear();
            monthList.clear();
            yearList.clear();
            yearForYearList.clear();

            do {
                amountCheckDB.add(c.getString(expenceIndex));
                incomeCheckDB.add(c.getString(incomeIndex));
                monthList.add(c.getString(monthIndex));
                yearList.add(c.getString(yearIndex));
                yearForYearList.add(c.getString(yearIndex));
            } while (c.moveToNext());
        }
        c.close();

        // Показывает на экране итоговые значения Total и Баланс
        double income = 0.0;
        double expence = 0.0;

        for (int i = 0; i < amountCheckDB.size(); i++) {
            expence += Double.parseDouble(amountCheckDB.get(i));
        }

        for (int i = 0; i < incomeCheckDB.size(); i++) {
            income += Double.parseDouble(incomeCheckDB.get(i));
        }

        totalIncomeTextView.setText(String.valueOf(income));
        totalExpenceTextView.setText(String.valueOf(expence));
        balanceTextView.setText(String.valueOf(income + expence));

        //Information (Month and Year) for ListView in method filterClicked()
        mMonthInStringList.clear();
        mYearInStringList.clear();

        for (int i = 0; i < monthList.size(); i++) {
            monthDateFormat = new SimpleDateFormat("MMMM");
            yearDateFormat = new SimpleDateFormat("YYYY");

            String pickMonth = monthDateFormat.format(Long.parseLong(monthList.get(i)));
            String pickYear = yearDateFormat.format(Long.parseLong(yearList.get(i)));

            //This lists goes to ListView which shows when press Month/Year button
            mYearInStringList.add(pickYear);
            mMonthInStringList.add(pickMonth + " " + pickYear);
        }

        for (int i = 0; i < mMonthInStringList.size() - 1; i++) {
            for (int k = mMonthInStringList.size() - 1; k > i; k--) {
                if (mMonthInStringList.get(i).equals(mMonthInStringList.get(k))) {
                    mMonthInStringList.remove(k);
                    monthList.remove(k);
                    yearList.remove(k);
                }
            }
        }

        for (int i = 0; i < mYearInStringList.size() - 1; i++) {
            for (int k = mYearInStringList.size() - 1; k > i; k--) {
                if (mYearInStringList.get(i).equals(mYearInStringList.get(k))) {
                    mYearInStringList.remove(k);
                    yearForYearList.remove(k);
                }
            }
        }
    }
}
