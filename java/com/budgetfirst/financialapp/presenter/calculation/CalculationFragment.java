package com.budgetfirst.financialapp.presenter.calculation;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.adapter.ExpenceAdapter;
import com.budgetfirst.financialapp.databinding.FragmentCalculationBinding;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.floatingbutton.FloatingButtonContract;
import com.budgetfirst.financialapp.presenter.floatingbutton.FloatingButtonSettings;
import com.budgetfirst.financialapp.presenter.data.DatabasePresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CalculationFragment extends Fragment implements CalculationContract.View, View.OnClickListener, FloatingButtonContract.View {

    private static final String TAG = "CalculationFragment";

    private CalculationPresenter mCalculationPresenter;
    private DatabasePresenter mDatabasePresenter;

    private ExpenceAdapter mAdapter;
    private SQLiteDatabase mDatabase;
    private RecyclerView mRecyclerView;

    private TextView mDisplayDateTextView, mIncomeTextView, mExpenseTextView,
            mBalanceTextView, mTotalIncomeTextView, mTotalExpenseTextView;
    private Animation mAnimateNumbers;

    private static int sCheckNumber;
    private static long sLongMonth, sLongYear, sLongDate;
    private static double sIncomeTotalAllBtn, sExpenseTotalAllBtn;

    private FragmentCalculationBinding binding;
    private FloatingActionButton fab;
    private FloatingButtonSettings fbs;
    private Button mYearBtn, mMonthBtn, mDayBtn, mShowAllBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalculationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mDatabase = new FinancialDBHelper(getContext()).getWritableDatabase();
        mDatabasePresenter = new DatabasePresenter(mDatabase, getContext());
        mCalculationPresenter = new CalculationPresenter(mDatabase);
        fbs = new FloatingButtonSettings(this);

        setViewsByBinding();
        doAnimation();
        showTotalSum();
        showCurrentSum(getAllItems());
        doRecyclerView();
        fbs.floatingButtonPressed(fab);

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

        return view;
    }

    @Override
    public void setViewsByBinding() {
        mRecyclerView = binding.recycleView;
        mIncomeTextView = binding.incomeTextView;
        mExpenseTextView = binding.expenceTextView;
        mBalanceTextView = binding.balanceTextView;
        mDisplayDateTextView = binding.showDateTextView;
        mTotalIncomeTextView = binding.totalIncomeTextView;
        mTotalExpenseTextView = binding.totalExpenceTextView;

        fab = binding.includedRelativeLayout.fab;
        mDayBtn = binding.includedRelativeLayout.dayBtnFilter;
        mYearBtn = binding.includedRelativeLayout.yearBtnFilter;
        mMonthBtn = binding.includedRelativeLayout.monthBtnFilter;
        mShowAllBtn = binding.includedRelativeLayout.showAllBtnFilter;

        listenerForButtons(mDayBtn);
        listenerForButtons(mYearBtn);
        listenerForButtons(mMonthBtn);
        listenerForButtons(mShowAllBtn);
    }

    void listenerForButtons(Button button) {
        button.setOnClickListener(CalculationFragment.this);
    }

    public void doAnimation() {
        mAnimateNumbers = AnimationUtils.loadAnimation(getContext(), R.anim.scale_numbers);
    }

    public void doRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ExpenceAdapter(getContext(), getAllItems());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Removing items from RecycleView and Database.
     * Method is implemented in Swipe to delete code -> onSwiped method (in onCreateView()).
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
        return mDatabasePresenter.getCursorForSelectedDate(
                sCheckNumber, sLongDate, sLongMonth, sLongYear);
    }

    /**
     * Shows the sum on the screen for the selected day/month/year.
     * Income, Expense and Balance Views are changing
     */
    public void showCurrentSum(Cursor cursor) {
        mCalculationPresenter.getDataToSetTextViewsPresenter(cursor);
        mCalculationPresenter.setExpenseTextView(mExpenseTextView);
        mCalculationPresenter.setIncomeTextView(mIncomeTextView);
        mCalculationPresenter.setBalanceTextView(mBalanceTextView);

        animateNumbers(mIncomeTextView);
        animateNumbers(mExpenseTextView);
        animateNumbers(mBalanceTextView);
    }

    /**
     * This method shows the results of income and expenses (Total Income and Total Expense Views).
     */
    public void showTotalSum() {
        mCalculationPresenter.getDataToSetTextViewsPresenter(
                mCalculationPresenter.getAllDataFromPresenter());

        sIncomeTotalAllBtn = mCalculationPresenter.getIncome();
        sExpenseTotalAllBtn = mCalculationPresenter.getExpence();

        mCalculationPresenter.setTotalExpenseTextView(mTotalExpenseTextView);
        mCalculationPresenter.setTotalIncomeTextView(mTotalIncomeTextView);
    }

    /**
     * Buttons Year, Month, Day, Show All.
     * When you click on the button, the variable checkNumber is assigned a number.
     * It determines which cursor gets into the adapter --> getAllItems() method.
     */
    @Override
    public void onClick(View v) {
        ArrayList<String> yearInStringList = mCalculationPresenter.fillArrayYear();
        ArrayList<String> monthInStringList = mCalculationPresenter.fillArrayMonth();
        ArrayList<String> dayInStringList = mCalculationPresenter.fillArrayDay();

        switch (v.getId()) {
            // Day btn
            case R.id.day_btn_filter:
                sCheckNumber = 1;
                if (!dayInStringList.isEmpty()) {
                    showListView(dayInStringList);
                }
                break;
            // Month btn
            case R.id.month_btn_filter:
                sCheckNumber = 2;
                if (!monthInStringList.isEmpty()) {
                    showListView(monthInStringList);
                }
                break;
            // Year btn
            case R.id.year_btn_filter:
                sCheckNumber = 3;
                if (!yearInStringList.isEmpty()) {
                    showListView(yearInStringList);
                }
                break;
            // Show all btn
            default:
                sCheckNumber = 0;
                mDisplayDateTextView.setText(R.string.btn_total);
                mIncomeTextView.setText(mCalculationPresenter.customFormat(sIncomeTotalAllBtn));
                mExpenseTextView.setText(mCalculationPresenter.customFormat(sExpenseTotalAllBtn));
                mBalanceTextView.setText(mCalculationPresenter.customFormat((sIncomeTotalAllBtn + sExpenseTotalAllBtn)));

                animateNumbers(mIncomeTextView);
                animateNumbers(mExpenseTextView);
                animateNumbers(mBalanceTextView);

                doRecyclerView();
                break;
        }

        fbs.hideFloatButtons();
    }

    private void animateNumbers(TextView textView) {
        textView.startAnimation(mAnimateNumbers);
    }

    public void showListView(final ArrayList<String> listForListView) {

        ListView listView = new ListView(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, listForListView);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setView(listView);
        final AlertDialog dialogAlert = builder.create();

        dialogAlert.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    sLongMonth = Long.parseLong(mCalculationPresenter.getmMonthList().get(position));

                    if (mCalculationPresenter.getmYearForYearList().size() > position) {
                        sLongYear = Long.parseLong(mCalculationPresenter.getmYearForYearList().get(position));
                    } else {
                        sLongYear = Long.parseLong(mCalculationPresenter.getmYearList().get(position));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    sLongDate = Long.parseLong(mCalculationPresenter.getmDateList().get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mDisplayDateTextView.setText(listForListView.get(position));

                showCurrentSum(getAllItems());
                doRecyclerView();
                dialogAlert.dismiss();
            }
        });
    }

    @Override
    public Button getmYearBtn() {
        return mYearBtn;
    }
    @Override
    public Button getmMonthBtn() {
        return mMonthBtn;
    }
    @Override
    public Button getmDayBtn() {
        return mDayBtn;
    }
    @Override
    public Button getmShowAllBtn() {
        return mShowAllBtn;
    }

    // Closing DataBase
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}