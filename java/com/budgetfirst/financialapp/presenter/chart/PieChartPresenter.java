package com.budgetfirst.financialapp.presenter.chart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.SpannableString;
import android.util.Log;

import com.budgetfirst.financialapp.model.Data;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.data.DatabasePresenter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PieChartPresenter implements OnChartValueSelectedListener {

    private static final String TAG = "PieChartPresenter";

    private PieChart chart;
    private Context context;
    private SQLiteDatabase database;
    private DatabasePresenter databasePresenter;

    private int checkNumber;
    private long dateLong, monthLong, yearLong;

    private ArrayList<Data> highlightList;

    public PieChartPresenter(PieChart chart, SQLiteDatabase mDatabase, Context context) {
        this.chart = chart;
        this.database = mDatabase;
        this.context = context;
    }

    public void startPieChart() {
        database = new FinancialDBHelper(context).getWritableDatabase();
        databasePresenter = new DatabasePresenter(database, context);

        setChartStyle();
        setData(fillEntryWithData());
    }

    private void setChartStyle() {
        chart.setOnChartValueSelectedListener(this);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setEnabled(false);

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(16f);
    }

    public void getDataFromActivity(int checkNumber, long dateLong, long monthLong, long yearLong) {
        this.checkNumber = checkNumber;
        this.dateLong = dateLong;
        this.monthLong = monthLong;
        this.yearLong = yearLong;

        setData(fillEntryWithData());
    }

    private HashMap<String, Double> fillEntryWithData() {
        return combineOperations(databasePresenter.getDataForPieChart(checkNumber, dateLong, monthLong, yearLong));
    }

    private HashMap<String, Double> combineOperations(ArrayList<Data> list) {
        highlightList = new ArrayList<>();
        HashMap<String, Double> map = new HashMap<>();

        for (Data data : list) {
            map.merge(data.getItemName(), data.getExpence(), Double::sum);
        }

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            highlightList.add(new Data(entry.getKey(), entry.getValue()));
        }

        return map;
    }

    private void setData(HashMap<String, Double> map) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array
        // determines their position around the center of the chart.

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            entries.add(new PieEntry((float) (entry.getValue() * -1), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        chart.setCenterText(generateCenterSpannableText(h));

        Log.i(TAG,
                "VAL SELECTED: Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    public SpannableString generateCenterSpannableText(Highlight h) {
        return new SpannableString( highlightList.get((int) h.getX()).getItemName()
                + ": " + highlightList.get((int) h.getX()).getExpence());
    }

    @Override
    public void onNothingSelected() {
        Log.i(TAG, "PieChart: nothing selected");
    }
}
