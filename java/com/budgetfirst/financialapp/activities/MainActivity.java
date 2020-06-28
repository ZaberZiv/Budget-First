package com.budgetfirst.financialapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetfirst.financialapp.modules.Data;
import com.budgetfirst.financialapp.presenter.DatabasePresenter;
import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.PanelPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mText;
    private TextView mDisplayDate;
    private EditText mEditText;
    private ImageView mImageViewAlert;
    private Button mIncomeButton;
    private Button mExpenceButton;

    private double mFirstIncome;
    private double mFirstExpence;
    private static String mNumbers = "";
    private static String mItemName;
    public static String mFormatedDate;

    private SQLiteDatabase mDatabase;
    private PanelPresenter mPanelPresenter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = new FinancialDBHelper(this).getWritableDatabase();
        mPanelPresenter = new PanelPresenter();

        setViewsById();
        showDatePickerDialog();
        incomeButtonListener();
        expenceButtonListener();
    }

    private void setViewsById() {
        mText = findViewById(R.id.text);
        mEditText = findViewById(R.id.editText);
        mDisplayDate = findViewById(R.id.textView4);
        mImageViewAlert = findViewById(R.id.imageViewTop);
        mIncomeButton = findViewById(R.id.plus);
        mExpenceButton = findViewById(R.id.minus);
    }

    // Set numbers buttons
    public void buttonClicked(View view) {
        mImageViewAlert.setVisibility(View.GONE);

        if (mNumbers.length() < 10) {
            mNumbers = mPanelPresenter.addNumbers(
                    mNumbers,
                    Integer.parseInt(view.getTag().toString()));

            mPanelPresenter.checkIfNumIsNull(mNumbers, mText);
        } else {
            Toast.makeText(this, R.string.toast_max_number
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // Period button
    public void putPeriod(View view) {
        mNumbers = mPanelPresenter.putPeriod(mNumbers, mText);
    }

    // Delete char button
    public void deleteChar(View view) {
        mNumbers = mPanelPresenter.deleteCharFromPanel(mNumbers, mText);
    }

    // CLEAN DB button
    public void clearDB(View view) {
        DatabasePresenter presenter = new DatabasePresenter(mDatabase, this);
        boolean flag = presenter.clearDatabase();
        if (flag) {
            mNumbers = "";
            mText.setText("");
        }
    }

    // Button Opens ProfitActivity.java
    public void onActionPressed(View view) {
        startActivity(new Intent(getApplicationContext(), ProfitActivity.class));
    }

    // Button PLUS (adding the income to database)
    public void incomeButtonListener() {
        mIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstIncome = checkOfEnteredNameAndNumbers(1);
                mFirstExpence = 0.0;

                if (mFirstIncome != 0.0) {
                    saveToDatabase();
                }
            }
        });
    }

    // Button MINUS (adding the expence to database)
    public void expenceButtonListener() {
        mExpenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstExpence = checkOfEnteredNameAndNumbers(-1);
                mFirstIncome = 0.0;

                if (mFirstExpence != 0.0) {
                    saveToDatabase();
                }
            }
        });
    }

    // Checking if both name and numbers were entered
    private double checkOfEnteredNameAndNumbers(int plusOrMinus) {
        if (mNumbers.length() > 0 && mEditText.getText().toString().trim().length() > 0) {
            double counter = Double.parseDouble(mNumbers);

            // If NO: show toast
            if (mNumbers.equals("0.0") || mNumbers.equals("0.") || mNumbers.equals("0")) {
                Toast.makeText(this, R.string.toast_not_equal_zero,
                        Toast.LENGTH_SHORT).show();
            } else {
                // If YES: set positive or negative sign, and set name of operation
                counter = counter * plusOrMinus;
                mItemName = mEditText.getText().toString().trim();
                mEditText.setText("");
            }
            mNumbers = "";
            mText.setText("");

            return counter;
        } else {
            Toast.makeText(this,
                    R.string.toast_add_numbers, Toast.LENGTH_SHORT).show();
            if (mNumbers.isEmpty()) {
                mImageViewAlert.setVisibility(View.VISIBLE);
            }
            return 0.0;
        }
    }

    // Save to Database
    private void saveToDatabase() {
        Data mData = new Data();
        mData.setIncome(mFirstIncome);
        mData.setExpence(mFirstExpence);
        mData.setFormatedDate(mFormatedDate);
        mData.setItemName(mItemName);

        DatabasePresenter presenter = new DatabasePresenter(mDatabase, mData);
        presenter.saveToDatabase();
    }

    // Calendar
    public void showDatePickerDialog() {
        // Show current date
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mFormatedDate = simpleDateFormat.format(date);
        mDisplayDate.setText(mFormatedDate);

        // Date Picker
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //After set date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (String.valueOf(month).length() == 1
                        && String.valueOf(dayOfMonth).length() == 1) {
                    mFormatedDate = "0" + dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(month).length() == 1) {
                    mFormatedDate = dayOfMonth + "/" + "0" + month + "/" + year;
                } else if (String.valueOf(dayOfMonth).length() == 1) {
                    mFormatedDate = "0" + dayOfMonth + "/" + month + "/" + year;
                } else {
                    mFormatedDate = dayOfMonth + "/" + month + "/" + year;
                }
                mDisplayDate.setText(mFormatedDate);
            }
        };
    }

    // Closing DataBase when the app destroys
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }
}