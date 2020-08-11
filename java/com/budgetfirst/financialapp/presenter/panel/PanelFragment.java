package com.budgetfirst.financialapp.presenter.panel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.databinding.FragmentPanelBinding;
import com.budgetfirst.financialapp.utils.UtilCalendar;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.data.DatabasePresenter;
import com.budgetfirst.financialapp.presenter.locker.LockerActivity;
import com.budgetfirst.financialapp.presenter.locker.LockerPresenter;

public class PanelFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PanelFragment";

    private SQLiteDatabase mDatabase;
    private PanelPresenter mPanelPresenter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private LockerPresenter mLockerPresenter;

    private TextView mText, mDisplayDate;
    private EditText mEditText;
    private ImageView mImageViewAlert, mLockerImageView, mLockerLockedImageView, mTrashcan;
    private Button mIncomeButton, mExpenseButton, mPeriod, mDeleteChar;

    private double mFirstIncome, mFirstExpense;
    private static String sNumbers = "";
    private static String sItemName, sFormatedDate;

    private FragmentPanelBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPanelBinding.inflate(inflater, container, false);

        mDatabase = new FinancialDBHelper(getContext()).getWritableDatabase();
        mPanelPresenter = new PanelPresenter();
        mLockerPresenter = new LockerPresenter(mDatabase);

        setViewsByBinding();

        if (mLockerPresenter.isCodeForLockerInDatabase()) {
            mLockerImageView.setVisibility(View.INVISIBLE);
            mLockerLockedImageView.setVisibility(View.VISIBLE);
        }

        showDatePickerDialog();
        incomeButtonListener();
        expenceButtonListener();
        lockerOpenedButtonListener();
        lockerLockedButtonListener();

        View view = binding.getRoot();
        return view;
    }

    public void setViewsByBinding() {
        mText = binding.text;
        mEditText = binding.editText;
        mDisplayDate = binding.textView4;

        mImageViewAlert = binding.imageViewTop;
        mLockerImageView = binding.locker;
        mLockerLockedImageView = binding.lockerLocked;

        mIncomeButton = binding.plus;
        mExpenseButton = binding.minus;
        mPeriod = binding.period;
        mDeleteChar = binding.delete;
        mTrashcan = binding.cleanDatabase;

        Button one = binding.one;
        Button two = binding.two;
        Button three = binding.three;
        Button four = binding.four;
        Button five = binding.five;
        Button six = binding.six;
        Button seven = binding.seven;
        Button eight = binding.eight;
        Button nine = binding.nine;
        Button zero = binding.zero;

        listenerForButtons(one);
        listenerForButtons(two);
        listenerForButtons(three);
        listenerForButtons(four);
        listenerForButtons(five);
        listenerForButtons(six);
        listenerForButtons(seven);
        listenerForButtons(eight);
        listenerForButtons(nine);
        listenerForButtons(zero);

        putThePeriodButtonListener();
        deleteCharButtonListener();
        cleanDatabaseButtonListener();
    }

    void listenerForButtons(Button button) {
        button.setOnClickListener(PanelFragment.this);
    }

    void putThePeriodButtonListener() {
        mPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sNumbers = mPanelPresenter.putPeriod(sNumbers, mText);
            }
        });
    }

    void deleteCharButtonListener() {
        mDeleteChar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sNumbers = mPanelPresenter.deleteCharFromPanel(sNumbers, mText);
            }
        });
    }

    void cleanDatabaseButtonListener() {
        mTrashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabasePresenter presenter = new DatabasePresenter(mDatabase, getContext());
                boolean flag = presenter.clearDatabase();
                if (flag) {
                    sNumbers = "";
                    mText.setText("");
                }
            }
        });
    }

    // BUTTON PLUS (adding the income to database)
    public void incomeButtonListener() {
        mIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstIncome = checkOfEnteredNameAndNumbers(1);
                mFirstExpense = 0.0;

                if (mFirstIncome != 0.0) {
                    saveToDatabase();
                }
            }
        });
    }

    // BUTTON MINUS (adding the expence to database)
    public void expenceButtonListener() {
        mExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstExpense = checkOfEnteredNameAndNumbers(-1);
                mFirstIncome = 0.0;

                if (mFirstExpense != 0.0) {
                    saveToDatabase();
                }
            }
        });
    }

    // BUTTON Locker
    public void lockerOpenedButtonListener() {
        mLockerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LockerActivity.class));
            }
        });
    }

    public void lockerLockedButtonListener() {
        mLockerLockedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LockerActivity.class));
            }
        });
    }

    // Checking if both name and numbers were entered
    public double checkOfEnteredNameAndNumbers(int plusOrMinus) {
        if (sNumbers.length() > 0 && mEditText.getText().toString().trim().toUpperCase().length() > 0) {
            double counter = Double.parseDouble(sNumbers);

            // If NO: show toast
            if (sNumbers.equals("0.0") || sNumbers.equals("0.") || sNumbers.equals("0")) {
                Toast.makeText(getContext(), R.string.toast_not_equal_zero,
                        Toast.LENGTH_SHORT).show();
            } else {
                // If YES: set positive or negative sign, and set name of operation
                counter = counter * plusOrMinus;
                sItemName = mEditText.getText().toString().trim().toUpperCase();
                mEditText.setText("");
            }
            sNumbers = "";
            mText.setText("");

            return counter;
        } else {
            Toast.makeText(getContext(),
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
                .saveToDatabase(mFirstIncome, mFirstExpense, sFormatedDate, sItemName);
    }

    // Calendar
    public void showDatePickerDialog() {
        // Show current date
        sFormatedDate = UtilCalendar.showCurrentDate();
        mDisplayDate.setText(sFormatedDate);

        // Date Picker
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelPresenter.getCalendarModule(mDateSetListener, getContext());
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

    @Override
    public void onClick(View v) {
        mImageViewAlert.setVisibility(View.GONE);

        if (sNumbers.length() < 10) {
            sNumbers = mPanelPresenter.addNumbersToThePanel(
                    sNumbers,
                    Integer.parseInt(v.getTag().toString()));

            mPanelPresenter.checkIfNumIsNull(sNumbers, mText);
        } else {
            Toast.makeText(getContext(), R.string.toast_max_number
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // Closing DataBase when the app destroys
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