package com.budgetfirst.financialapp.presenter.chart;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.databinding.FragmentChartBinding;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.data.DatabasePresenter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ChartFragment extends Fragment implements ChartContract.View, View.OnClickListener {

    private static final String TAG = "ChartFragment";

    private PieChart mChart;
    private BarChart mMultiBarChart;
    private SQLiteDatabase mDatabase;
    private ChartPresenter mChartPresenter;
    private DatabasePresenter mDatabasePresenter;
    private PieChartPresenter mPieChartPresenter;
    private MultiBarChartPresenter mMultiBarChartPresenter;

    private static int sCheckNumber = 0;
    private static long sLongMonth = 0;
    private static long sLongYear = 0;
    private static long sLongDate = 0;

    private FragmentChartBinding binding;
    private FloatingActionButton fab;
    private Button mDayBtn, mShowAllBtn, mYearBtn, mMonthBtn;

    private ScrollView mScrollView;
    private RelativeLayout mRelativeLayout;
    private TextView mDateBreakdownTextView, mIncomeTextView, mExpenseTextView, mBalanceTextView;
    private boolean flag_float_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mDatabase = new FinancialDBHelper(getContext()).getWritableDatabase();
        mChartPresenter = new ChartPresenter(mDatabase);
        mDatabasePresenter = new DatabasePresenter(mDatabase, getContext());

        setViewsByBinding();

        mPieChartPresenter = new PieChartPresenter(mChart, mDatabase, getContext());
        mPieChartPresenter.startPieChart();
        mMultiBarChartPresenter = new MultiBarChartPresenter(this, getContext(), mMultiBarChart, mDatabase);

        getDataWithChosenDate();
        floatingButtonPressed();
        showCurrentSum(getAllItems());

        return view;
    }

    public void setViewsByBinding() {
        fab = binding.fab;
        mChart = binding.pieChart;
        mMultiBarChart = binding.chart1;
        mScrollView = binding.scrollView;
        mRelativeLayout = binding.noDataLayout;
        mDateBreakdownTextView = binding.dateBreakdown;

        mIncomeTextView = binding.incomeTextView;
        mExpenseTextView = binding.expenceTextView;
        mBalanceTextView = binding.balanceTextView;

        mDayBtn = binding.dayBtnFilter;
        mYearBtn = binding.yearBtnFilter;
        mMonthBtn = binding.monthBtnFilter;
        mShowAllBtn = binding.showAllBtnFilter;

        listenerForButtons(mDayBtn);
        listenerForButtons(mYearBtn);
        listenerForButtons(mMonthBtn);
        listenerForButtons(mShowAllBtn);
    }

    void listenerForButtons(Button button) {
        button.setOnClickListener(ChartFragment.this);
    }

    void floatingButtonPressed() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_float_btn) {
                    flag_float_btn = true;
                    showFloatButtons();

                } else {
                    flag_float_btn = false;
                    hideFloatButtons();
                }
            }
        });
    }

    private void showFloatButtons() {
        mYearBtn.setVisibility(View.VISIBLE);
        mMonthBtn.setVisibility(View.VISIBLE);
        mDayBtn.setVisibility(View.VISIBLE);
        mShowAllBtn.setVisibility(View.VISIBLE);
    }

    private void hideFloatButtons() {
        mYearBtn.setVisibility(View.GONE);
        mMonthBtn.setVisibility(View.GONE);
        mDayBtn.setVisibility(View.GONE);
        mShowAllBtn.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> mYearInStringList = mChartPresenter.fillArrayYear();
        ArrayList<String> mMonthInStringList = mChartPresenter.fillArrayMonth();
        ArrayList<String> mDateInStringList = mChartPresenter.fillArrayDay();

        switch (v.getId()) {
            // Day btn
            case R.id.day_btn_filter:
                sCheckNumber = 1;
                if (!mDateInStringList.isEmpty()) {
                    showListView(mDateInStringList);
                }
                break;
            // Month btn
            case R.id.month_btn_filter:
                sCheckNumber = 2;
                if (!mMonthInStringList.isEmpty()) {
                    showListView(mMonthInStringList);
                }
                break;
            // Year btn
            case R.id.year_btn_filter:
                sCheckNumber = 3;
                if (!mYearInStringList.isEmpty()) {
                    showListView(mYearInStringList);
                }
                break;
            // Show all btn
            default:
                sCheckNumber = 0;
                mDateBreakdownTextView.setText(R.string.btn_total);
                getDataWithChosenDate();
                break;
        }

        flag_float_btn = false;
        hideFloatButtons();
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
                    sLongMonth = Long.parseLong(mChartPresenter.getmMonthList().get(position));

                    if (mChartPresenter.getmYearForYearList().size() > position) {
                        sLongYear = Long.parseLong(mChartPresenter.getmYearForYearList().get(position));
                    } else {
                        sLongYear = Long.parseLong(mChartPresenter.getmYearList().get(position));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    sLongDate = Long.parseLong(mChartPresenter.getmDateList().get(position));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mDateBreakdownTextView.setText(listForListView.get(position));
                showCurrentSum(getAllItems());
                getDataWithChosenDate();
                dialogAlert.dismiss();
            }
        });
    }

    public void showCurrentSum(Cursor cursor) {
        mChartPresenter.getDataToSetTextViewsPresenter(cursor);
        mChartPresenter.setExpenseTextView(mExpenseTextView);
        mChartPresenter.setIncomeTextView(mIncomeTextView);
        mChartPresenter.setBalanceTextView(mBalanceTextView);
    }

    public Cursor getAllItems() {
        return mDatabasePresenter.getCursorForSelectedDate(
                sCheckNumber, sLongDate, sLongMonth, sLongYear);
    }

    public void getDataWithChosenDate() {
        mPieChartPresenter.getDataFromActivity(sCheckNumber, sLongDate, sLongMonth, sLongYear);
        mMultiBarChartPresenter.startMultiBarChart(sCheckNumber, sLongDate, sLongMonth, sLongYear);
    }

    @Override
    public void hideViews() {
        fab.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.INVISIBLE);
        mRelativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showViews() {
        mRelativeLayout.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
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