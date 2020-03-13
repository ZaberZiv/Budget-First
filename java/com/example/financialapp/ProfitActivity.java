package com.example.financialapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import static com.example.financialapp.MainActivity.customFormat;

public class ProfitActivity extends AppCompatActivity {

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;
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
    LinearLayout linearLayoutDate;
    LinearLayout linearLayoutButtons;
    LinearLayout relativeLayout;
    ListView listView;
    Button buttonDay;

    private String currentDate;
    static int checkNumber;
    static long longMonth;
    static long longYear;
    static long dateLong;
    static double incomeTotalAllBtn;
    static double expenceTotalAllBtn;
    static int textColorExpence;
    static int textColorIncome;
    static int bgColorForSwipe;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    static private ArrayList<String> mMonthInStringList = new ArrayList<>();
    static private ArrayList<String> mYearInStringList = new ArrayList<>();
    static private ArrayList<String> monthList = new ArrayList<>();
    static private ArrayList<String> yearList = new ArrayList<>();
    static private ArrayList<String> yearForYearList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        // TextColor is selected in ExpenceAdapter class
        textColorExpence = ContextCompat.getColor(this, R.color.colorExpence);
        textColorIncome = ContextCompat.getColor(this, R.color.colorIncome);
//        bgColorForSwipe = ContextCompat.getColor(this, R.color.colorAccent);

        incomeTextView = (TextView) findViewById(R.id.incomeTextView);
        expenceTextView = (TextView) findViewById(R.id.expenceTextView);
        balanceTextView = (TextView) findViewById(R.id.balanceTextView);
        totalIncomeTextView = (TextView) findViewById(R.id.totalIncomeTextView);
        totalExpenceTextView = (TextView) findViewById(R.id.totalExpenceTextView);
        mDisplayDate = (TextView) findViewById(R.id.showDateTextView);

        linearLayoutButtons = (LinearLayout) findViewById(R.id.linearLayoutButtons);
        linearLayoutDate = (LinearLayout) findViewById(R.id.linearLayoutDate);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout3);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout4);
        relativeLayout = (LinearLayout) findViewById(R.id.recycleLayout);

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
                ItemTouchHelper.LEFT) {
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
        cursorDataBase(getAllItems());
        showTotalFromDB();
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

    // Buttons Year, Month, Show All. The Day btn in showDatePickerDialog() method;
    public void filterClicked(View view) {
        switch (view.getId()) {
            case R.id.btnMonth:
                checkNumber = 2;
                showListView(mMonthInStringList, yearList);
                break;

            case R.id.btnYear:
                checkNumber = 3;
                showListView(mYearInStringList, yearForYearList);
                break;

                // Доработать кнопку ALL
            default:
                checkNumber = 0;
                mDisplayDate.setText("ALL");
                incomeTextView.setText(customFormat("###,###.##", incomeTotalAllBtn));
                expenceTextView.setText(customFormat("###,###.##", expenceTotalAllBtn));
                doRecycleView();
                break;
        }
    }

    // ListView + Adapter для
    public void showListView(final ArrayList<String> listForListView, final ArrayList<String> listForLongYear) {
        linearLayout = findViewById(R.id.showMonth);

        linearLayout.setVisibility(View.VISIBLE);
        linearLayoutDate.setVisibility(View.INVISIBLE);
        linearLayoutButtons.setVisibility(View.INVISIBLE);
        linearLayout1.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.INVISIBLE);

        listView = (ListView) findViewById(R.id.listView);
        // Сюда должен передаваться список с датой (Месяц + Год или просто Год в зависимости от нажатой кнопки)
        ArrayAdapter adapterForListView = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, listForListView);
        listView.setAdapter(adapterForListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                longMonth = Long.parseLong(monthList.get(position));
                longYear = Long.parseLong(listForLongYear.get(position));

                mDisplayDate.setText(listForListView.get(position));

                linearLayout.setVisibility(View.INVISIBLE);
                linearLayoutButtons.setVisibility(View.VISIBLE);
                linearLayoutDate.setVisibility(View.VISIBLE);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                cursorDataBase(getAllItems());
                doRecycleView();
            }
        });
    }

    // Calendar
    public void showDatePickerDialog() {
        //Showing the date that Uses have set in MainActivity
        currentDate = MainActivity.mFormatedDate;
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
                if (String.valueOf(month).length() == 1 && String.valueOf(dayOfMonth).length() == 1) {
                    currentDate = "0" + dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(month).length() == 1) {
                    currentDate = dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(dayOfMonth).length() == 1) {
                    currentDate = "0" + dayOfMonth + "/" + month + "/" + year;
                } else {
                    currentDate = dayOfMonth + "/" + month + "/" + year;
                }
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
        ArrayList<String> amountCheckDB = new ArrayList<>();
        ArrayList<String> incomeCheckDB = new ArrayList<>();

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

        // Показывает на экране суммы за определенный период
        double income = 0.0;
        double expence = 0.0;

        for (int i = 0; i < amountCheckDB.size(); i++) {
            expence += Double.parseDouble(amountCheckDB.get(i));
        }

        for (int i = 0; i < incomeCheckDB.size(); i++) {
            income += Double.parseDouble(incomeCheckDB.get(i));
        }

        if (expence == 0.0) {
            expenceTextView.setText("0.0");
        } else {
            expenceTextView.setText(customFormat("###,###.##", expence));
        }

        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(customFormat("###,###.##", income));
        }
    }

    public void showTotalFromDB() {
        ArrayList<String> amountCheckDB = new ArrayList<>();
        ArrayList<String> incomeCheckDB = new ArrayList<>();

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
                yearList.add(c.getString(yearIndex)); // contains date (Year column) in long type
                yearForYearList.add(c.getString(yearIndex)); // contains date (Year column) in long type
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

        incomeTotalAllBtn = income;
        expenceTotalAllBtn = expence;

        totalIncomeTextView.setText(customFormat("###,###.##", income));
        totalExpenceTextView.setText(customFormat("###,###.##", expence));
        balanceTextView.setText(customFormat("###,###.##", (income + expence)));

        //Information (Month and Year) for ListView in method filterClicked()
        mMonthInStringList.clear();
        mYearInStringList.clear();

        for (int i = 0; i < monthList.size(); i++) {
            SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");
            SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

            String pickMonth = monthDateFormat.format(Long.parseLong(monthList.get(i)));
            String pickYear = yearDateFormat.format(Long.parseLong(yearList.get(i)));

            //This lists goes to ListView which shows when press Month/Year button
            mYearInStringList.add(pickYear);
            mMonthInStringList.add(pickMonth + " " + pickYear);
        }

        // removing all equals dates (Month)
        for (int i = 0; i < mMonthInStringList.size() - 1; i++) {
            for (int k = mMonthInStringList.size() - 1; k > i; k--) {
                if (mMonthInStringList.get(i).equals(mMonthInStringList.get(k))) {
                    mMonthInStringList.remove(k);
                    monthList.remove(k);
                    yearList.remove(k);
                }
            }
        }

        // removing all equals dates (Year)
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