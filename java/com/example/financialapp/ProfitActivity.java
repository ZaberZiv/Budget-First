package com.example.financialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfitActivity extends AppCompatActivity {

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;

    static TextView incomeTextView;
    static TextView expenceTextView;
    TextView balanceTextView;
    TextView totalIncomeTextView;
    TextView totalExpenceTextView;

    private TextView mDisplayDate;
    private String currentDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    static public ArrayList<String> amountCheckDB = new ArrayList<>();
    static public ArrayList<String> incomeCheckDB = new ArrayList<>();

    static long dateLong;
    RecyclerView recyclerView;

    Button buttonDay;
    static int checkNumber;
    static long longMonth;
    static long longYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        incomeTextView = (TextView) findViewById(R.id.incomeTextView);
        expenceTextView = (TextView) findViewById(R.id.expenceTextView);

        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        totalIncomeTextView = (TextView) findViewById(R.id.totalIncomeTextView);
        totalExpenceTextView = (TextView) findViewById(R.id.totalExpenceTextView);

        buttonDay = (Button) findViewById(R.id.btnDay);

        //Show the Date
        mDisplayDate = (TextView) findViewById(R.id.showDateTextView);
        showDatePickerDialog();

        //DB Helper
        FinancialDBHelper dbHelper = new FinancialDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        showTotlaFromDB();

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

    public void doRecycleView() {
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpenceAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
        Log.i("RecycleView", "After the RecycleView");
    }

    private void removeItem(long id) {
        mDatabase.delete(FinancialContract.FinancialEntry.TABLE_NAME,
                FinancialContract.FinancialEntry._ID + "=" + id, null);
        Log.i("removeItem", "DELETING THE ITEM!");

        mAdapter.swapCursor(getAllItems());
    }

    public Cursor getAllItems() {
        String selection = FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " = " + dateLong;
        String month = FinancialContract.FinancialEntry.COLUMN_MONTH + " = " + longMonth + " AND " + FinancialContract.FinancialEntry.COLUMN_YEAR + " = " + longYear;
        String year = FinancialContract.FinancialEntry.COLUMN_YEAR + " = " + longYear;

        Log.i("getAllItems", "DAY LONG: " + selection);
        Log.i("getAllItems", "MONTH LONG: " + month);
        Log.i("getAllItems", "YEAR LONG: " + year);

        if (checkNumber == 1) {
            Log.i("getAllItems", "DAY: checkNumber is: " + checkNumber);
            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    selection,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 2) {
            Log.i("getAllItems", "MONTH: checkNumber is: " + checkNumber);



            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    month,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else if (checkNumber == 3) {
            Log.i("getAllItems", "YEAR: checkNumber is: " + checkNumber);
            return mDatabase.query(
                    FinancialContract.FinancialEntry.TABLE_NAME,
                    null,
                    year,
                    null,
                    null,
                    null,
                    FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " DESC"
            );
        } else {
            Log.i("getAllItems", "DEFAULT: checkNumber is: " + checkNumber);
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

    public void filterClicked(View view) {
        Log.i("filterClicked", "filterClicked is worked!");

        switch (view.getId()) {
            case R.id.btnMonth:
                checkNumber = 2;


                // FINISH HERE!!!!!!!!!!!!!!!!!!!
                LinearLayout linearLayout = findViewById(R.id.showMonth);
                linearLayout.setVisibility(View.VISIBLE);



                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
                String pickMonth = simpleDateFormat.format(dateLong);
                try {
                    longMonth = new SimpleDateFormat("MMMM").parse(pickMonth).getTime();
                    Log.i("MONTH", "LONG MONTH: " + longMonth);

                } catch (Exception e) {
                    Log.e("Data Error", "Check the convertion of Date from String to long");
                    e.printStackTrace();
                }
                doRecycleView();
                Log.i("btnMonth", "Month is CLICKED! " + checkNumber);
                break;

            case R.id.btnYear:
                checkNumber = 3;
                SimpleDateFormat DateFormat = new SimpleDateFormat("YYYY");
                String pickYear = DateFormat.format(dateLong);
                try {
                    longYear = new SimpleDateFormat("YYYY").parse(pickYear).getTime();
                    Log.i("YEAR", "LONG YEAR: " + longYear);

                } catch (Exception e) {
                    Log.e("Data Error", "Check the convertion of Date from String to long");
                    e.printStackTrace();
                }
                doRecycleView();
                Log.i("btnYear", "Year is CLICKED! " + checkNumber);
                break;

            default:
                checkNumber = 0;
                doRecycleView();
                Log.i("default", "default is working! " + checkNumber);
                break;
        }
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
                cursorDataBase(dateLong);

                checkNumber = 1;
                Log.i("Date", "Date is PICKED! " + checkNumber);

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

    public void cursorDataBase(long userDate) {
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + FinancialContract.FinancialEntry.TABLE_NAME +
                " WHERE " + FinancialContract.FinancialEntry.COLUMN_TIMESTAMP + " = " + userDate, null);

        int expenceIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
        int incomeIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);

        if (c.moveToFirst()) {
            amountCheckDB.clear();
            incomeCheckDB.clear();

            do {
                amountCheckDB.add(c.getString(expenceIndex));
                incomeCheckDB.add(c.getString(incomeIndex));
            } while (c.moveToNext());
        }
        c.close();

        double income = 0.0;
        double expence = 0.0;

        for (int i = 0; i < amountCheckDB.size(); i++) {
            expence += Double.parseDouble(amountCheckDB.get(i));
        }

        for (int i = 0; i < incomeCheckDB.size(); i++) {
            income += Double.parseDouble(incomeCheckDB.get(i));
        }

        incomeTextView.setText(String.valueOf(income));
        expenceTextView.setText(String.valueOf(expence));
    }

    public void showTotlaFromDB() {
        Cursor c = mDatabase.rawQuery("SELECT * FROM " + FinancialContract.FinancialEntry.TABLE_NAME, null);

        int incomeIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME);
        int expenceIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE);
        int monthIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_MONTH);
        int yearIndex = c.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_YEAR);

        ArrayList<String> monthList = new ArrayList<>();
        ArrayList<String> yearList = new ArrayList<>();

        if (c.moveToFirst()) {
            amountCheckDB.clear();
            incomeCheckDB.clear();
            monthList.clear();
            yearList.clear();

            do {
                amountCheckDB.add(c.getString(expenceIndex));
                incomeCheckDB.add(c.getString(incomeIndex));
                monthList.add(c.getString(monthIndex));
                yearList.add(c.getString(yearIndex));
            } while (c.moveToNext());
        }
        c.close();

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


        // FINISH HERE!!!!!!!!!!!!!!!!!!!

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, monthList);
        listView.setAdapter(adapter);
    }
}
