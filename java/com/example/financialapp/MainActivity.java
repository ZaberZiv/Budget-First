package com.example.financialapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    TextView text;
    TextView mDisplayDate;
    EditText editText;
    static StringBuilder inputStr;
    static double mFirstIncome;
    static double mFirstExpence;
    static String mNumbers;
    static String mItemName;

    private SQLiteDatabase mDatabase;
    static public String mFormatedDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FinancialDBHelper dbHelper = new FinancialDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        text = (TextView) findViewById(R.id.text);
        editText = (EditText) findViewById(R.id.editText);
        mDisplayDate = (TextView) findViewById(R.id.textView4);
        showDatePickerDialog();

        mNumbers = "";
        mFirstIncome = 0.0;
        mFirstExpence = 0.0;
    }

    // Set numbers
    public void buttonClicked(View view) {
        inputStr = new StringBuilder(mNumbers);

        if (mNumbers.length() < 10) {
            switch (Integer.parseInt(view.getTag().toString())) {
                case 0:
                    if (!mNumbers.isEmpty()) {
                        mNumbers = inputStr.append("0").toString();
                    }
                    break;
                case 1:
                    mNumbers = inputStr.append("1").toString();
                    break;
                case 2:
                    mNumbers = inputStr.append("2").toString();
                    break;
                case 3:
                    mNumbers = inputStr.append("3").toString();
                    break;
                case 4:
                    mNumbers = inputStr.append("4").toString();
                    break;
                case 5:
                    mNumbers = inputStr.append("5").toString();
                    break;
                case 6:
                    mNumbers = inputStr.append("6").toString();
                    break;
                case 7:
                    mNumbers = inputStr.append("7").toString();
                    break;
                case 8:
                    mNumbers = inputStr.append("8").toString();
                    break;
                case 9:
                    mNumbers = inputStr.append("9").toString();
                    break;
            }
            checkIfNumbersEmpty();
        } else {
            Toast.makeText(getApplicationContext(), "It is maximum number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkIfNumbersEmpty() {
        if (!mNumbers.equals("")) {
            text.setText(customFormat("###,###.##", Double.parseDouble(mNumbers)));
        }
    }

    public void refresherStringBuilder() {
        inputStr = new StringBuilder(mNumbers);
    }

    // Delete numbers
    public void deleteChar(View view) {
        if (mNumbers.length() > 0) {
            mNumbers = mNumbers.substring(0, mNumbers.length() - 1);

            refresherStringBuilder();
            checkIfNumbersEmpty();

            if (mNumbers.length() == 0) {
                text.setText("");
            }
        }
    }

    // CLEAN DB
    public void clearDB(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.mipmap.sym_def_app_icon)
                .setTitle("DELETE DATA BASE")
                .setMessage("You are going to DELETE ALL DATA! Are you sure?")
                .setPositiveButton("YES, DELETE ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.delete(FinancialContract.FinancialEntry.TABLE_NAME, null, null);
                        mNumbers = "";
                        text.setText("");
                        Toast.makeText(MainActivity.this, "Database has been cleaned!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO, DO NOT DELETE!", null)
                .show();
    }

    // Opens ProfitActivity.java
    public void onActionPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfitActivity.class);
        startActivity(intent);
    }

    public void putPeriod(View view) {
        refresherStringBuilder();

        if (!mNumbers.contains(".") && mNumbers.length() != 0) {
            mNumbers = inputStr.append(".").toString();
            text.setText(mNumbers);
        } else if (!mNumbers.contains(".") && mNumbers.length() == 0) {
            mNumbers = inputStr.append("0,").toString();
            text.setText(mNumbers);
        }
    }

    // Button +
    public void income(View view) {
        if (mNumbers.length() > 0 && editText.getText().toString().trim().length() > 0) {
            mFirstIncome = Double.parseDouble(mNumbers);

            mNumbers = "";
            text.setText("");
            mItemName = editText.getText().toString();
            editText.getText().clear();
            mFirstExpence = 0.0;
            // ADD TO DATA BASE
            addToDataBase();

        } else {
            Toast.makeText(this, "Please add numbers and description.", Toast.LENGTH_SHORT).show();
        }
    }

    // Button -
    public void expenses(View view) {
        if (mNumbers.length() > 0 && editText.getText().toString().trim().length() > 0) {
            mFirstExpence = Double.parseDouble(mNumbers);

            mNumbers = "";
            text.setText("");
            mFirstExpence = mFirstExpence * -1;

            mItemName = editText.getText().toString();
            editText.setText("");
            mFirstIncome = 0.0;
            // ADD TO DATA BASE
            addToDataBase();

        } else {
            Toast.makeText(this, "Please add numbers and description.", Toast.LENGTH_SHORT).show();
        }
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
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                if (String.valueOf(month).length() == 1 && String.valueOf(dayOfMonth).length() == 1) {
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

    // DATA BASE
    public void addToDataBase() {
        long longDateDB = convertStringToLongDate(mFormatedDate);
        long longMonthDB = convertStringToLongMonth(mFormatedDate);
        long longYearDB = convertStringToLongYear(mFormatedDate);

        ContentValues cv = new ContentValues();
        cv.put(FinancialContract.FinancialEntry.COLUMN_TITLE, mItemName);
        cv.put(FinancialContract.FinancialEntry.COLUMN_EXPENCE, mFirstExpence);
        cv.put(FinancialContract.FinancialEntry.COLUMN_INCOME, mFirstIncome);
        cv.put(FinancialContract.FinancialEntry.COLUMN_TIMESTAMP, longDateDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_MONTH, longMonthDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_YEAR, longYearDB);
        mDatabase.insert(FinancialContract.FinancialEntry.TABLE_NAME, null, cv);
    }

    // Converting Date from String type to Long type for adding to Database in method addToDataBase()
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

    static public long convertStringToLongMonth(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        String pickMonth = simpleDateFormat.format(longDate);
        try {
            longDate = new SimpleDateFormat("MMMM").parse(pickMonth).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }

        return longDate;
    }

    static public long convertStringToLongYear(String dateInString) {
        long longDate = 0;
        try {
            longDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateInString).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String pickYear = simpleDateFormat.format(longDate);
        try {
            longDate = new SimpleDateFormat("yyyy").parse(pickYear).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }

        return longDate;
    }

    static public String customFormat(String pattern, double value) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        return myFormatter.format(value);
    }
}