package com.budgetfirst.financialapp.presenter.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.model.database.FinancialDBHelper;
import com.budgetfirst.financialapp.presenter.base.FragmentActivity;
import com.budgetfirst.financialapp.presenter.locker.LockerActivity;
import com.budgetfirst.financialapp.presenter.locker.LockerPresenter;

public class SplashScreenActivity extends Activity {

    private static final String TAG = "SplashScreenActivity";
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private LockerPresenter lockerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SQLiteDatabase database = new FinancialDBHelper(this).getWritableDatabase();
        lockerPresenter = new LockerPresenter(database);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getExtraData();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getExtraData() {
        Bundle arguments = getIntent().getExtras();
        int code = 0;

        if (arguments != null) {
            code = arguments.getInt("code");
            Log.i(TAG, "code: " + code);
        }

        if (code != lockerPresenter.getCodeForLocker()) {
            startNewActivity(LockerActivity.class);
        } else {
            startNewActivity(FragmentActivity.class);
        }
    }

    void startNewActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        SplashScreenActivity.this.startActivity(intent);
        SplashScreenActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}