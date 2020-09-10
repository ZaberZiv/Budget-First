package com.budgetfirst.financialapp.presenter.locker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.databinding.ActivityLockerBinding;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.base.FragmentActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class LockerActivity extends AppCompatActivity {

    private static final String TAG = "LockerActivity";

    private LockerPresenter mLockerPresenter;

    private static String sPinCode = "";
    private static String sSavedPinCode = "";
    private boolean mFlag, mCorrectPIN;

    private EditText mFirstEnteredNumber, mSecondEnteredNumber, mThirdEnteredNumber, mFourthEnteredNumber;
    private TextView mHiddenTextView, mMainTextView, mSkipButton;
    private TextView mFirstTextView, mSecondTextView, mThirdTextView, mFourthTextView;
    private SwitchMaterial mSwitchMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        SQLiteDatabase database = new FinancialDBHelper(this).getWritableDatabase();
        mLockerPresenter = new LockerPresenter(database);

        setViewsByBinding();
        setListenersForEditText();
        showKeyBoard();
        isPinIsAlreadyExist();
        skipButtonListener(mSkipButton);
    }

    @Override
    public void onBackPressed() {

    }

    private void setViewsByBinding() {
        ActivityLockerBinding binding = ActivityLockerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mSwitchMaterial = binding.switcher;
        mSkipButton = binding.skipButton;

        mFirstEnteredNumber = binding.firstLetterPass;
        mSecondEnteredNumber = binding.secondLetterPass;
        mThirdEnteredNumber = binding.thirdLetterPass;
        mFourthEnteredNumber = binding.fourthLetterPass;

        mHiddenTextView = binding.repeatText;
        mMainTextView = binding.mainText;

        mFirstTextView = binding.firstTextView;
        mSecondTextView = binding.secondTextView;
        mThirdTextView = binding.thirdTextView;
        mFourthTextView = binding.fourthTextView;
    }

    private void setListenersForEditText() {
        disableInput(mSecondEnteredNumber);
        disableInput(mThirdEnteredNumber);
        disableInput(mFourthEnteredNumber);

        editTextChangedListener(mFirstEnteredNumber, mSecondEnteredNumber, mFirstTextView);
        editTextChangedListener(mSecondEnteredNumber, mThirdEnteredNumber, mSecondTextView);
        editTextChangedListener(mThirdEnteredNumber, mFourthEnteredNumber, mThirdTextView);
        editTextChangedListener(mFourthEnteredNumber, null, mFourthTextView);
    }

    private void showKeyBoard() {
        mFirstEnteredNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void isPinIsAlreadyExist() {
        if (!mLockerPresenter.isCodeForLockerInDatabase()) {
            mMainTextView.setText(R.string.locker_save_pin);
        } else {
            mSwitchMaterial.setVisibility(View.VISIBLE);
            mSkipButton.setVisibility(View.INVISIBLE);
            mFlag = true;
        }
    }

    private void editTextChangedListener(final EditText firstEditText,
                                         final EditText secondEditText,
                                         final TextView textView) {

        firstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0) return;

                mHiddenTextView.setVisibility(View.INVISIBLE);

                sPinCode += String.valueOf(s);
                if (!mFlag) sSavedPinCode += String.valueOf(s);

                if (secondEditText != null) {
                    Log.i(TAG, "onTextChanged: EditText is not NULL, keep adding Text!");

                    firstEditText.clearFocus();
                    secondEditText.requestFocus();

                    firstEditText.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                } else {
                    Log.i(TAG, "onTextChanged: All 4 EditText are FULL!");
                    Log.i(TAG, "onTextChanged: Checking if PIN right or not!");

                    if (mLockerPresenter.isCodeForLockerInDatabase()
                            && sPinCode.equals(String.valueOf(mLockerPresenter.getCodeForLocker()))
                            && !mSwitchMaterial.isChecked()) {

                        Log.i(TAG, "onTextChanged: RIGHT");

                        mCorrectPIN = true;
                        startActivityWithExtra(true);

                    } else if (mLockerPresenter.isCodeForLockerInDatabase()
                            && sPinCode.equals(String.valueOf(mLockerPresenter.getCodeForLocker()))
                            && mSwitchMaterial.isChecked()) {

                        Log.i(TAG, "onTextChanged: PIN Deleted!");

                        mLockerPresenter.deleteCodeForLockerFromDatabase(Integer.parseInt(sPinCode));
                        mCorrectPIN = true;
                        showToast(R.string.locker_deleted_success);
                        startActivityWithExtra(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) return;

                if (secondEditText == null
                        && !mLockerPresenter.isCodeForLockerInDatabase()
                        && !mCorrectPIN) {

                    if (!mFlag) {
                        Log.i(TAG, "afterTextChanged: First code is ACCEPTED!");

                        showHiddenTextAndClearEditText(
                                R.string.locker_repeat_pin,
                                true, false);

                        setVisibilityOfPinViews();

                    } else if (sSavedPinCode.equals(sPinCode)
                            && !sSavedPinCode.isEmpty()) {
                        Log.i(TAG, "afterTextChanged: Second code is CORRECT!");

                        mLockerPresenter.saveCodeForLockerInDatabase(Integer.parseInt(sPinCode));
                        startActivityWithExtra(true);
                        mFlag = false;
                        showToast(R.string.locker_saved_success);
                        startActivityWithExtra(false);

                    } else {
                        Log.i(TAG, "afterTextChanged: Second code is INCORRECT!");

                        showHiddenTextAndClearEditText(
                                R.string.locker_wrong_pin_start_again,
                                false, true);

                        setVisibilityOfPinViews();
                    }

                } else if (secondEditText == null
                        && mLockerPresenter.isCodeForLockerInDatabase()
                        && !mCorrectPIN) {
                    Log.i(TAG, "afterTextChanged: Second code is INCORRECT TRY AGAIN!");

                    showHiddenTextAndClearEditText(
                            R.string.locker_wrong_pin_try_again,
                            false, true);

                    setVisibilityOfPinViews();
                }

                if (secondEditText != null) unableInput(secondEditText);
            }
        });
    }

    private void disableInput(EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
    }

    private void unableInput(EditText editText) {
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    private void showToast(int textID) {
        Toast.makeText(LockerActivity.this,
                textID, Toast.LENGTH_SHORT).show();
    }

    private void setVisibilityOfPinViews() {
        mFirstTextView.setVisibility(View.GONE);
        mSecondTextView.setVisibility(View.GONE);
        mThirdTextView.setVisibility(View.GONE);
        mFourthTextView.setVisibility(View.GONE);

        mFirstEnteredNumber.setVisibility(View.VISIBLE);
        mSecondEnteredNumber.setVisibility(View.VISIBLE);
        mThirdEnteredNumber.setVisibility(View.VISIBLE);
        mFourthEnteredNumber.setVisibility(View.VISIBLE);

        mFirstEnteredNumber.requestFocus();
    }

    private void skipButtonListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityWithExtra(false);
            }
        });
    }

    private void showHiddenTextAndClearEditText(int text, boolean flag, boolean clearSavedPin) {
        mHiddenTextView.setVisibility(View.VISIBLE);
        mHiddenTextView.setText(text);
        clearTextInEditText();
        mFlag = flag;
        if (clearSavedPin) sSavedPinCode = "";
    }

    private void startActivityWithExtra(boolean extraFlag) {
        Intent intent = new Intent(this, FragmentActivity.class);
        if (extraFlag) intent.putExtra("code", Integer.parseInt(sPinCode));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        hideKeyBoard();
    }

    private void clearTextInEditText() {
        sPinCode = "";

        mFirstEnteredNumber.getText().clear();
        mSecondEnteredNumber.getText().clear();
        mThirdEnteredNumber.getText().clear();
        mFourthEnteredNumber.getText().clear();

        mFirstEnteredNumber.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sPinCode = "";
        sSavedPinCode = "";
    }
}