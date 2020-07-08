package com.budgetfirst.financialapp.presenter.calculation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.databinding.ActivityCalculationBinding;
import com.budgetfirst.financialapp.model.ModelCalendar;
import com.budgetfirst.financialapp.presenter.database.DatabasePresenter;

import java.util.ArrayList;

public class CalculationActivity extends AppCompatActivity implements CalculationContract.View {

    private static final String TAG = "CalculationActivity";

    private CalculationPresenter mCalculationPresenter;
    private DatabasePresenter mDatabasePresenter;

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;
    private RecyclerView mRecyclerView;
    private Dialog dialog;

    private TextView displayDateTextView;
    private TextView incomeTextView;
    private TextView expenceTextView;
    private TextView balanceTextView;
    private TextView totalIncomeTextView;
    private TextView totalExpenceTextView;
    private LinearLayout linearLayout;
    private ListView listView;
    private Button buttonDay;
    private Button buttonMonth;


    private Animation slideDown;
    private Animation slideUp;
    private Animation animateNumbers;

    private static int sCheckNumber;
    private static long sLongMonth;
    private static long sLongYear;
    private static long sLongDate;
    private static double sIncomeTotalAllBtn;
    private static double sExpenceTotalAllBtn;
    private boolean flagMonth;
    private boolean flagYear;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private static ArrayList<String> sMonthInStringList = new ArrayList<>();
    private static ArrayList<String> sYearInStringList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        mDatabase = new FinancialDBHelper(this).getWritableDatabase();
        mDatabasePresenter = new DatabasePresenter(mDatabase, this);
        mCalculationPresenter = new CalculationPresenter(mDatabase);

        setViewsByBinding();
        doAnimation();
        showDatePickerDialog();
        showTotalSum();
        showCurrentSum(getAllItems());
        doRecyclerView();

        //Swipe left to delete
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
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void setViewsByBinding() {
        ActivityCalculationBinding binding = ActivityCalculationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        incomeTextView = binding.incomeTextView;
        expenceTextView = binding.expenceTextView;
        balanceTextView = binding.balanceTextView;
        displayDateTextView = binding.showDateTextView;
        totalIncomeTextView = binding.totalIncomeTextView;
        totalExpenceTextView = binding.totalExpenceTextView;
        buttonDay = binding.btnDay;
        buttonMonth = binding.btnMonth;
    }

    public void doAnimation() {
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down_listview);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_listview);
        animateNumbers = AnimationUtils.loadAnimation(this, R.anim.scale_numbers);
    }

    public void doRecyclerView() {
        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExpenceAdapter(this, getAllItems());
        mRecyclerView.setAdapter(mAdapter);
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
     * Shows the amount on the screen for the selected day/month/year.
     * Income, Expence and Balance Views are changing
     */
    public void showCurrentSum(Cursor cursor) {
        mCalculationPresenter.getDataToSetTextViewsPresenter(cursor);
        mCalculationPresenter.setExpenceTextView(expenceTextView);
        mCalculationPresenter.setIncomeTextView(incomeTextView);
        mCalculationPresenter.setBalanceTextView(balanceTextView);

        expenceTextView.startAnimation(animateNumbers);
        incomeTextView.startAnimation(animateNumbers);
        balanceTextView.startAnimation(animateNumbers);
    }

    /**
     * This method shows the results of income and expenses (Total Income and Total Expence Views).
     * Lists with dates go to filterClicked() method.
     */
    public void showTotalSum() {
        mCalculationPresenter.getDataToSetTextViewsPresenter(
                mCalculationPresenter.getAllDataFromPresenter());

        sIncomeTotalAllBtn = mCalculationPresenter.getIncome();
        sExpenceTotalAllBtn = mCalculationPresenter.getExpence();

        mCalculationPresenter.setTotalExpenceTextView(totalExpenceTextView);
        mCalculationPresenter.setTotalIncomeTextView(totalIncomeTextView);
    }

    /**
     * Buttons Year, Month, Show All. The Day btn in showDatePickerDialog() method;
     * When you click on the button, the variable checkNumber is assigned a number.
     * It determines which cursor gets into the adapter --> getAllItems() method.
     */
    public void filterClicked(View view) {
        //This lists goes to ListView which shows when Month/Year button is pressed.
        sYearInStringList = mCalculationPresenter.fillArrayPresenter(sYearInStringList, 0);
        sMonthInStringList = mCalculationPresenter.fillArrayPresenter(sMonthInStringList, 1);

        switch (view.getId()) {
            // Month btn
            case R.id.btnMonth:
                sCheckNumber = 2;
                if (!sMonthInStringList.isEmpty()) {
                    showListView(sMonthInStringList, mCalculationPresenter.getmYearList());
                }
                break;
            // Year btn
            case R.id.btnYear:
                sCheckNumber = 3;
                if (!sYearInStringList.isEmpty()) {
                    showListView(sYearInStringList, mCalculationPresenter.getmYearForYearList());
                }
                break;
            // Show all btn
            default:
                sCheckNumber = 0;
                displayDateTextView.setText(R.string.btn_total);
                incomeTextView.setText(mCalculationPresenter.customFormat("###,###.##"
                        , sIncomeTotalAllBtn));
                expenceTextView.setText(mCalculationPresenter.customFormat("###,###.##"
                        , sExpenceTotalAllBtn));
                balanceTextView.setText(mCalculationPresenter.customFormat("###,###.##"
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

        ListView listView = new ListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listForListView);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(CalculationActivity.this);
        builder.setCancelable(true);
        builder.setView(listView);
        final AlertDialog dialogAlert = builder.create();

        dialogAlert.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCalculationPresenter.getAllDataFromPresenter();

                try {
                    sLongMonth = Long.parseLong(mCalculationPresenter.getmMonthList().get(position));
                    sLongYear = Long.parseLong(listLongYear.get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                displayDateTextView.setText(listForListView.get(position));

                showCurrentSum(getAllItems());
                doRecyclerView();
                dialogAlert.dismiss();
            }
        });
    }

    /**
     * Calendar
     * When the user selects a date, the variable checkNumber is assigned the number 1.
     * It is passed to method getAllItems() to pass the cursor to the adapter.
     */
    @Override
    public void showDatePickerDialog() {
        displayDateTextView.setText(R.string.btn_total);

        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelCalendar.getCalendarModule(mDateSetListener,
                        CalculationActivity.this);
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                String currentDate = ModelCalendar.dateFormatModule(dayOfMonth, month, year);
                displayDateTextView.setText(currentDate);
                sLongDate = mCalculationPresenter.getLongDate(currentDate);
                sCheckNumber = 1;
                showCurrentSum(getAllItems());
                doRecyclerView();
            }
        };
    }

    // Closing DataBase
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}