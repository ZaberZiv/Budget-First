package com.budgetfirst.financialapp.presenter.calculation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;

import com.budgetfirst.financialapp.model.ModelConverter;
import com.budgetfirst.financialapp.model.database.ModelDatabase;
import com.budgetfirst.financialapp.presenter.panel.PanelPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CalculationPresenter implements CalculationContract.Presenter {

    private static final String TAG = "CalculationPresenter";

    private ModelDatabase moduleDatabase;
    private PanelPresenter mPanelPresenter;
    private double income;
    private double expence;
    private ArrayList<String> mMonthList = new ArrayList<>();
    private ArrayList<String> mYearList = new ArrayList<>();
    private ArrayList<String> mYearForYearList = new ArrayList<>();

    public CalculationPresenter(SQLiteDatabase database) {
        moduleDatabase = new ModelDatabase(database);
    }

    public long getLongDate(String date) {
        return ModelConverter.convertStringToLongDate(date);
    }

    @Override
    public void getDataToSetTextViewsPresenter(Cursor cursor) {
        mPanelPresenter = new PanelPresenter();
        moduleDatabase.getDataFromCursor(cursor);

        income = moduleDatabase.getIncome();
        expence = moduleDatabase.getExpence();
    }

    @Override
    public Cursor getAllDataFromPresenter() {
        return moduleDatabase.getCursorOfAllData();
    }

    @Override
    public void setExpenceTextView(TextView expenceTextView) {
        if (expence == 0.0) {
            expenceTextView.setText("0.0");
        } else {
            expenceTextView.setText(mPanelPresenter.customFormat("###,###.##", expence));
        }
    }

    @Override
    public void setIncomeTextView(TextView incomeTextView) {
        if (income == 0.0) {
            incomeTextView.setText("0.0");
        } else {
            incomeTextView.setText(mPanelPresenter.customFormat("###,###.##", income));
        }
    }

    @Override
    public void setBalanceTextView(TextView balanceTextView) {
        balanceTextView.setText(mPanelPresenter.customFormat("###,###.##", (income + expence)));
    }

    @Override
    public void setTotalExpenceTextView(TextView totalExpenceTextView) {
        totalExpenceTextView.setText(mPanelPresenter.customFormat("###,###.##", expence));
    }

    @Override
    public void setTotalIncomeTextView(TextView totalIncomeTextView) {
        totalIncomeTextView.setText(mPanelPresenter.customFormat("###,###.##", income));
    }

    @Override
    public ArrayList<String> fillArrayPresenter(ArrayList<String> dateInStringList, int num) {
        dateInStringList.clear();

        moduleDatabase.getDataFromCursor(moduleDatabase.getCursorOfAllData());
        mMonthList = moduleDatabase.getmMonthList();
        mYearList = moduleDatabase.getmYearList();
        mYearForYearList = moduleDatabase.getmYearForYearList();

        for (int i = 0; i < mMonthList.size(); i++) {
            SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM");
            SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");

            String pickMonth = "";
            String pickYear = "";
            try {
                pickMonth = monthDateFormat.format(Long.parseLong(mMonthList.get(i)));
                pickYear = yearDateFormat.format(Long.parseLong(mYearList.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "yearList is empty, maybe after cleaning DB." + mYearList);
            }

            //This lists goes to ListView which shows when press Month/Year button
            if (num == 0) dateInStringList.add(pickYear);
            if (num == 1) dateInStringList.add(pickMonth + " " + pickYear);
        }

        removeDuplicates(dateInStringList, mMonthList, mYearList, 1);
        removeDuplicates(mYearForYearList, null, null, 0);

        return dateInStringList;
    }

    private void removeDuplicates(ArrayList<String> arrayList,
                                  ArrayList<String> arrayListTwo,
                                  ArrayList<String> arrayListThree,
                                  int num) {

        for (int i = 0; i < arrayList.size() - 1; i++) {
            for (int k = arrayList.size() - 1; k > i; k--) {
                if (arrayList.get(i).equals(arrayList.get(k))) {
                    arrayList.remove(k);

                    if (num == 1) {
                        arrayListTwo.remove(k);
                        arrayListThree.remove(k);
                    }
                }
            }
        }
    }

    @Override
    public String customFormat(String pattern, double value) {
        return ModelConverter.customStringFormat(pattern, value);
    }

    public double getIncome() {
        return income;
    }

    public double getExpence() {
        return expence;
    }

    public ArrayList<String> getmMonthList() {
        return mMonthList;
    }

    public ArrayList<String> getmYearList() {
        return mYearList;
    }

    public ArrayList<String> getmYearForYearList() {
        return mYearForYearList;
    }
}
