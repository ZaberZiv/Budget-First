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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    static private TextView text;
    static public TextView mDisplayDate;
    public EditText editText;
    static private StringBuilder inputStr;
    static double firstIncome;
    static double firstExpence;
    static String numbers;
    static String itemName;

    static SQLiteDatabase expencesDB;
    private SQLiteDatabase mDatabase;
    static public String formatedDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FinancialDBHelper dbHelper = new FinancialDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        editText = (EditText) findViewById(R.id.editText);
        mDisplayDate = findViewById(R.id.textView4);
        showDatePickerDialog();

        numbers = "";
        firstIncome = 0.0;
        firstExpence = 0.0;

        text = findViewById(R.id.text);
    }

    public void buttonClicked(View view) {
        inputStr = new StringBuilder(numbers);

        if (numbers.length() < 10) {
            switch (Integer.parseInt(view.getTag().toString())) {
                case 0:
                    if (!numbers.isEmpty()) {
                        numbers = inputStr.append("0").toString();
                    }
                    break;
                case 1:
                    numbers = inputStr.append("1").toString();
                    break;
                case 2:
                    numbers = inputStr.append("2").toString();
                    break;
                case 3:
                    numbers = inputStr.append("3").toString();
                    break;
                case 4:
                    numbers = inputStr.append("4").toString();
                    break;
                case 5:
                    numbers = inputStr.append("5").toString();
                    break;
                case 6:
                    numbers = inputStr.append("6").toString();
                    break;
                case 7:
                    numbers = inputStr.append("7").toString();
                    break;
                case 8:
                    numbers = inputStr.append("8").toString();
                    break;
                case 9:
                    numbers = inputStr.append("9").toString();
                    break;
            }

            text.setText(numbers);
        } else {
            Toast.makeText(getApplicationContext(), "It is maximum number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActionPressed(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfitActivity.class);
        startActivity(intent);
    }

    public void deleteChar(View view) {
        if (numbers.length() > 0) {
            numbers = numbers.substring(0, numbers.length() - 1);
            text.setText(numbers);
        }
    }

    // CLEAN DB
    public void clearDB(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.mipmap.sym_def_app_icon)
                .setTitle("DELETE DATA BASE")
                .setMessage("YOU ARE GOING TO DELETE ALL DATA! ARE YOU SURE?")
                .setPositiveButton("YES, DELETE ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.delete(FinancialContract.FinancialEntry.TABLE_NAME, null, null);
                        numbers = "";
                        text.setText("");
                        Toast.makeText(MainActivity.this, "Data Base has been cleaned!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO, PLEASE DO NOT!", null)
                .show();
    }

//    public void putPeriod(View view) {
//        numbers = inputStr.append(".").toString();
//        text.setText(numbers);
//    }

    public void income(View view) {
        if (numbers.length() > 0 && editText.getText().toString().trim().length() > 0) {
            firstIncome = Double.parseDouble(numbers);

            numbers = "";
            text.setText("");
            itemName = editText.getText().toString();
            editText.getText().clear();
            firstExpence = 0.0;
            // ADD TO DATA BASE
            addToDataBase();

        } else {
            Toast.makeText(this, "Please add numbers and description.", Toast.LENGTH_SHORT).show();
        }
    }

    public void expenses(View view) {
        if (numbers.length() > 0 && editText.getText().toString().trim().length() > 0) {
            firstExpence = Double.parseDouble(numbers);

            numbers = "";
            text.setText("");
            firstExpence = firstExpence * -1;

            itemName = editText.getText().toString();
            editText.setText("");
            firstIncome = 0.0;
            // ADD TO DATA BASE
            addToDataBase();

        } else {
            Toast.makeText(this, "Please add numbers and description.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDatePickerDialog() {
        // Show current date
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formatedDate = simpleDateFormat.format(date);
        mDisplayDate.setText(formatedDate);

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
                formatedDate = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(formatedDate);
            }
        };
    }

    // DATA BASE
    public void addToDataBase() {
        long longDateDB = convertStringToLongDate(formatedDate);
        long longMonthDB = convertStringToLongMonth(formatedDate);
        long longYearDB = convertStringToLongYear(formatedDate);

        Log.i("longDateDB", "LONG DATE: " + longDateDB);
        Log.i("longMonthDB", "LONG MONTH: " + longMonthDB);
        Log.i("longYearDB", "LONG YEAR: " + longYearDB);

        ContentValues cv = new ContentValues();
        cv.put(FinancialContract.FinancialEntry.COLUMN_TITLE, itemName);
        cv.put(FinancialContract.FinancialEntry.COLUMN_EXPENCE, firstExpence);
        cv.put(FinancialContract.FinancialEntry.COLUMN_INCOME, firstIncome);
        cv.put(FinancialContract.FinancialEntry.COLUMN_TIMESTAMP, longDateDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_MONTH, longMonthDB);
        cv.put(FinancialContract.FinancialEntry.COLUMN_YEAR, longYearDB);
        mDatabase.insert(FinancialContract.FinancialEntry.TABLE_NAME, null, cv);
    }

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY");
        String pickMonth = simpleDateFormat.format(longDate);
        try {
            longDate = new SimpleDateFormat("YYYY").parse(pickMonth).getTime();
        } catch (Exception e) {
            Log.e("Data Error", "Check the convertion of Date from String to long");
            e.printStackTrace();
        }

        return longDate;
    }
}