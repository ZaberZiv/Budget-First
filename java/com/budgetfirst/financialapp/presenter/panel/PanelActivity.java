package com.budgetfirst.financialapp.presenter.panel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetfirst.financialapp.databinding.ActivityPanelBinding;
import com.budgetfirst.financialapp.presenter.calculation.CalculationActivity;
import com.budgetfirst.financialapp.presenter.database.DatabasePresenter;
import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelActivity extends AppCompatActivity implements PanelContract.View {

    private SQLiteDatabase mDatabase;
    private PanelPresenter mPanelPresenter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mText;
    private TextView mDisplayDate;
    private EditText mEditText;
    private ImageView mImageViewAlert;
    private Button mIncomeButton;
    private Button mExpenceButton;

    private double mFirstIncome;
    private double mFirstExpence;
    private static String sNumbers = "";
    private static String sItemName;
    public static String sFormatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);

        mDatabase = new FinancialDBHelper(this).getWritableDatabase();
        mPanelPresenter = new PanelPresenter();

        setViewsByBinding();
        showDatePickerDialog();
        incomeButtonListener();
        expenceButtonListener();
    }

    @Override
     public void setViewsByBinding() {
        ActivityPanelBinding binding = ActivityPanelBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mText = binding.text;
        mEditText = binding.editText;
        mDisplayDate = binding.textView4;
        mImageViewAlert = binding.imageViewTop;
        mIncomeButton = binding.plus;
        mExpenceButton = binding.minus;
    }

    // BUTTONS set numbers
    public void buttonClicked(View view) {
        mImageViewAlert.setVisibility(View.GONE);

        if (sNumbers.length() < 10) {
            sNumbers = mPanelPresenter.addNumbersToThePanel(
                    sNumbers,
                    Integer.parseInt(view.getTag().toString()));

            mPanelPresenter.checkIfNumIsNull(sNumbers, mText);
        } else {
            Toast.makeText(this, R.string.toast_max_number
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // BUTTON sets the Period
    public void putPeriod(View view) {
        sNumbers = mPanelPresenter.putPeriod(sNumbers, mText);
    }

    // BUTTON Deletes char
    public void deleteChar(View view) {
        sNumbers = mPanelPresenter.deleteCharFromPanel(sNumbers, mText);
    }

    // BUTTON CLEANs DB
    public void clearDB(View view) {
        DatabasePresenter presenter = new DatabasePresenter(mDatabase, this);
        boolean flag = presenter.clearDatabase();
        if (flag) {
            sNumbers = "";
            mText.setText("");
        }
    }

    // BUTTON Opens ProfitActivity.java
    public void onActionPressed(View view) {
        startActivity(new Intent(getApplicationContext(), CalculationActivity.class));
    }

    // BUTTON PLUS (adding the income to database)
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

    // BUTTON MINUS (adding the expence to database)
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
    @Override
    public double checkOfEnteredNameAndNumbers(int plusOrMinus) {
        if (sNumbers.length() > 0 && mEditText.getText().toString().trim().length() > 0) {
            double counter = Double.parseDouble(sNumbers);

            // If NO: show toast
            if (sNumbers.equals("0.0") || sNumbers.equals("0.") || sNumbers.equals("0")) {
                Toast.makeText(this, R.string.toast_not_equal_zero,
                        Toast.LENGTH_SHORT).show();
            } else {
                // If YES: set positive or negative sign, and set name of operation
                counter = counter * plusOrMinus;
                sItemName = mEditText.getText().toString().trim();
                mEditText.setText("");
            }
            sNumbers = "";
            mText.setText("");

            return counter;
        } else {
            Toast.makeText(this,
                    R.string.toast_add_numbers, Toast.LENGTH_SHORT).show();
            if (sNumbers.isEmpty()) {
                mImageViewAlert.setVisibility(View.VISIBLE);
            }
            return 0.0;
        }
    }

    // Save to Database
    public void saveToDatabase() {
        new DatabasePresenter(mDatabase)
                .saveToDatabase(mFirstIncome, mFirstExpence, sFormatedDate, sItemName);
    }

    // Calendar
    @Override
    public void showDatePickerDialog() {
        // Show current date
        sFormatedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        mDisplayDate.setText(sFormatedDate);

        // Date Picker
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelPresenter.getCalendarModule(mDateSetListener, PanelActivity.this);
            }
        });
        //After set date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                sFormatedDate = mPanelPresenter.dateFormatModule(dayOfMonth, month, year);
                mDisplayDate.setText(sFormatedDate);
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