package com.budgetfirst.financialapp.presenter.chart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import com.budgetfirst.financialapp.model.Data;
import com.budgetfirst.financialapp.model.filter.DataFilter;
import com.budgetfirst.financialapp.presenter.data.DatabasePresenter;
import com.budgetfirst.financialapp.utils.UtilRemoveDuplicates;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MultiBarChartPresenter implements OnChartValueSelectedListener {

    private static final String TAG = "MultiBarChartPresenter";

    private BarChart chart;
    private DatabasePresenter databasePresenter;
    private ChartContract.View view;

    MultiBarChartPresenter(ChartContract.View view, Context context, BarChart chart, SQLiteDatabase database) {
        this.view = view;
        this.chart = chart;
        databasePresenter = new DatabasePresenter(database, context);
    }

    public void startMultiBarChart(DataFilter dataFilter) {
        setMultiBarChartStyle(getListWithData(dataFilter));
    }

    private ArrayList<Data> getListWithData(DataFilter dataFilter) {
        return databasePresenter.getDataForMultiBarChart(dataFilter);
    }

    private void setMultiBarChartStyle(ArrayList<Data> list) {

        if (!list.isEmpty()) {
            view.showViews();
        } else {
            view.hideViews();
            return;
        }

        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(8f);
        l.setXOffset(25f);
        l.setYEntrySpace(0f);
        l.setTextSize(12f);
        l.setFormSize(10f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);

        setDataOn(list);
    }

    private void setDataOn(ArrayList<Data> list) {
        float groupSpace = 0.16f;
        float barSpace = 0.06f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        // (0.4 + 0.06) * 4 + 0.16 = 1.00 -> interval per "group"
        float income = 0.0f;
        float expense = 0.0f;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            income += list.get(i).getIncome();
            expense += list.get(i).getExpense() * -1;
        }

        values1.add(new BarEntry(0, income));
        values2.add(new BarEntry(0, expense));

        BarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);

            set1.setValues(values1);
            set2.setValues(values2);

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 2 DataSets
            set1 = new BarDataSet(values1, "Income");
            set1.setColor(Color.rgb(24, 204, 147));
            set2 = new BarDataSet(values2, "Expense");
            set2.setColor(Color.rgb(216, 81, 130));

            set1.setValueTextSize(12f);
            set2.setValueTextSize(12f);

            BarData data = new BarData(set1, set2);
            data.setValueFormatter(new LargeValueFormatter());

            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        ArrayList<String> arrayList = new ArrayList<>();
        for (Data data : list) {
            arrayList.add(data.getYear());
        }

        ArrayList<Integer> intList = UtilRemoveDuplicates.removeDuplicatesAndSortData(arrayList);

        chart.getXAxis().setAxisMinimum(intList.get(0));
        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(intList.get(0)
                + chart.getBarData().getGroupWidth(groupSpace, barSpace));
        chart.groupBars(intList.get(0), groupSpace, barSpace);

        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i(TAG, "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "Nothing selected.");
    }
}
