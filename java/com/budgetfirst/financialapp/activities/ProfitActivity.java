package com.budgetfirst.financialapp.activities;

import androidx.appcompat.app.ActionBar;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.budgetfirst.financialapp.adapter.ExpenceAdapter;
import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.DatabasePresenter;
import com.budgetfirst.financialapp.presenter.PanelPresenter;
import com.budgetfirst.financialapp.presenter.ProfitPresenter;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfitActivity extends AppCompatActivity {

    private static final String TAG = "ProfitActivity";

    private ProfitPresenter mProfitPresenter;
    private PanelPresenter mPanelPresenter;
    private DatabasePresenter mDatabasePresenter;

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;
    private RecyclerView recyclerView;

    private TextView displayDateTextView;
    private TextView incomeTextView;
    private TextView expenceTextView;
    private TextView balanceTextView;
    private TextView totalIncomeTextView;
    private TextView totalExpenceTextView;
    private LinearLayout linearLayout;
    private ListView listView;
    private Button buttonDay;

    private Animation slideDown;
    private Animation slideUp;
    private Animation animateNumbers;

    static int sCheckNumber;
    static long sLongMonth;
    static long sLongYear;
    static long sLongDate;
    static double sIncomeTotalAllBtn;
    static double sExpenceTotalAllBtn;
    private boolean flagMonth;
    private boolean flagYear;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static ArrayList<String> sMonthInStringList = new ArrayList<>();
    private static ArrayList<String> sYearInStringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDatabase = new FinancialDBHelper(this).getWritableDatabase();
        mDatabasePresenter = new DatabasePresenter(mDatabase, this);
        mPanelPresenter = new PanelPresenter();
        mProfitPresenter = new ProfitPresenter();

        setViews();
        doAnimation();
        showDatePickerDialog();
        showTotalSum();
        showCurrentSum(getAllItems());
        doRecyclerView();

        //Swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void setViews() {
        incomeTextView = findViewById(R.id.incomeTextView);
        expenceTextView = findViewById(R.id.expenceTextView);
        balanceTextView = findViewById(R.id.balanceTextView);
        displayDateTextView = findViewById(R.id.showDateTextView);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        totalExpenceTextView = findViewById(R.id.totalExpenceTextView);
        linearLayout = findViewById(R.id.showMonth);
        listView = findViewById(R.id.listView);
        buttonDay = findViewById(R.id.btnDay);
    }

    public void doAnimation() {
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down_listview);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_listview);
        animateNumbers = AnimationUtils.loadAnimation(this, R.anim.scale_numbers);
    }

    // RecycleView
    public void doRecyclerView() {
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpenceAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Removing items from RecycleView and Database.
     * Method is implemented in Swipe to delete code -> onSwiped method (in onCreate()).
     */
    private void removeItem(long id) {
        mDatabasePresenter.deleteFromDatabase(id);
        showCurrentSum(getAllItems());
        showTotalSum();
        mAdapter.swapCursor(getAllItems());
    }

    /**
     * Adding Cursor to the mAdapter (in doRecycleView() method)
     * By default, the cursor displays data for all time.
     * Depending on the button pressed in method filterClicked()
     * will display data for the selected period.
     */
    public Cursor getAllItems() {
        return mDatabasePresenter.getCursorPresenter(
                sCheckNumber, sLongDate, sLongMonth, sLongYear);
    }

    /**
     * Buttons Year, Month, Show All. The Day btn in showDatePickerDialog() method;
     * When you click on the button, the variable checkNumber is assigned a number.
     * It determines which cursor gets into the adapter --> getAllItems() method.
     */
    public void filterClicked(View view) {
        //This lists goes to ListView which shows when Month/Year button is pressed.
        sYearInStringList = mDatabasePresenter.fillArrayPresenter(sYearInStringList, 0);
        sMonthInStringList = mDatabasePresenter.fillArrayPresenter(sMonthInStringList, 1);

        switch (view.getId()) {
            // Month btn
            case R.id.btnMonth:
                sCheckNumber = 2;
                if (!sMonthInStringList.isEmpty()) {
                    if (!flagMonth) {
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout.startAnimation(slideUp);
                        showListView(sMonthInStringList, mDatabasePresenter.getmYearList());
                        flagMonth = true;
                    } else {
                        linearLayout.startAnimation(slideDown);
                        linearLayout.setVisibility(View.INVISIBLE);
                        flagMonth = false;
                    }
                }
                break;
            // Year btn
            case R.id.btnYear:
                sCheckNumber = 3;
                if (!sYearInStringList.isEmpty()) {
                    if (!flagYear) {
                        linearLayout.setVisibility(View.VISIBLE);
                        linearLayout.startAnimation(slideUp);
                        showListView(sYearInStringList, mDatabasePresenter.getmYearForYearList());
                        flagYear = true;
                    } else {
                        linearLayout.startAnimation(slideDown);
                        linearLayout.setVisibility(View.INVISIBLE);
                        flagYear = false;
                    }
                }
                break;
            // Show all btn
            default:
                sCheckNumber = 0;
                displayDateTextView.setText(R.string.btn_total);
                incomeTextView.setText(mPanelPresenter.customStringFormat("###,###.##"
                                , sIncomeTotalAllBtn));
                expenceTextView.setText(mPanelPresenter.customStringFormat("###,###.##"
                                , sExpenceTotalAllBtn));
                balanceTextView.setText(mPanelPresenter.customStringFormat("###,###.##"
                                , (sIncomeTotalAllBtn + sExpenceTotalAllBtn)));

                incomeTextView.startAnimation(animateNumbers);
                expenceTextView.startAnimation(animateNumbers);
                balanceTextView.startAnimation(animateNumbers);

                doRecyclerView();
                break;
        }
    }

    /**
     * This view is hidden by default.
     * It is displayed by pressing the buttons in filterClicked().
     * Shows a list of added dates (Month + Year or just Year).
     * Hides when you select an item or press a button.
     * Lists in params comes from showTotalFromDB();
     */
    public void showListView(final ArrayList<String> listForListView,
                             final ArrayList<String> listLongYear) {

        ArrayAdapter adapterForListView = new ArrayAdapter(this,
                android.R.layout.simple_expandable_list_item_1, listForListView);
        listView.setAdapter(adapterForListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDatabasePresenter.getAllDataFromPresenter();

                try {
                    sLongMonth = Long.parseLong(mDatabasePresenter.getmMonthList().get(position));
                    sLongYear = Long.parseLong(listLongYear.get(position));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                linearLayout.startAnimation(slideDown);
                displayDateTextView.setText(listForListView.get(position));

                showCurrentSum(getAllItems());
                doRecyclerView();
                linearLayout.setVisibility(View.INVISIBLE);
                flagYear = false;
                flagMonth = false;
            }
        });
    }

    /**
     * Calendar
     * When the user selects a date, the variable checkNumber is assigned the number 1.
     * It is passed to method getAllItems() to pass the cursor to the adapter.
     */
    public void showDatePickerDialog() {
        displayDateTextView.setText(R.string.btn_total);

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

                String currentDate;
                if (String.valueOf(month).length() == 1 && String.valueOf(dayOfMonth).length() == 1) {
                    currentDate = "0" + dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(month).length() == 1) {
                    currentDate = dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(dayOfMonth).length() == 1) {
                    currentDate = "0" + dayOfMonth + "/" + month + "/" + year;
                } else {
                    currentDate = dayOfMonth + "/" + month + "/" + year;
                }
                displayDateTextView.setText(currentDate);
                sLongDate = mProfitPresenter.getLongDate(currentDate);
                sCheckNumber = 1;
                showCurrentSum(getAllItems());
                doRecyclerView();
            }
        };
    }

    /**
     * Shows the amount on the screen for the selected day/month/year.
     * Income, Expence and Balance Views are changing
     */
    public void showCurrentSum(Cursor cursor) {
        mDatabasePresenter.getDataToSetTextViewsPresenter(cursor);
        mDatabasePresenter.setExpenceTextView(expenceTextView);
        mDatabasePresenter.setIncomeTextView(incomeTextView);
        mDatabasePresenter.setBalanceTextView(balanceTextView);

        expenceTextView.startAnimation(animateNumbers);
        incomeTextView.startAnimation(animateNumbers);
        balanceTextView.startAnimation(animateNumbers);
    }

    /**
     * This method shows the results of income and expenses (Total Income and Total Expence Views).
     * Lists with dates go to filterClicked() method.
     */
    public void showTotalSum() {
        mDatabasePresenter.getDataToSetTextViewsPresenter(
                mDatabasePresenter.getAllDataFromPresenter());

        sIncomeTotalAllBtn = mDatabasePresenter.getIncome();
        sExpenceTotalAllBtn = mDatabasePresenter.getExpence();

        mDatabasePresenter.setTotalExpenceTextView(totalExpenceTextView);
        mDatabasePresenter.setTotalIncomeTextView(totalIncomeTextView);
    }

    // Closing DataBase
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}