package com.budgetfirst.financialapp.presenter.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.budgetfirst.financialapp.databinding.ActivityFragmentBinding;
import com.budgetfirst.financialapp.presenter.calculation.CalculationFragment;
import com.budgetfirst.financialapp.presenter.panel.PanelFragment;
import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.presenter.chart.ChartFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    private static final String TAG = "FragmentActivity";

    private boolean flag_panel, flag_history, flag_charts;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        setViewsByBinding();

        flag_panel = false;
        flag_history = true;
        flag_charts = true;

        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PanelFragment()).commit();
    }

    public void setViewsByBinding() {
        ActivityFragmentBinding binding = ActivityFragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        bottomNav = binding.bottomNavigation;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_dialpad:
                            if (flag_panel) {
                                flag_charts = true;
                                flag_history = true;
                                flag_panel = false;
                                selectedFragment = new PanelFragment();
                            }
                            break;
                        case R.id.nav_storage:
                            if (flag_history) {
                                flag_panel = true;
                                flag_charts = true;
                                flag_history = false;
                                selectedFragment = new CalculationFragment();
                            }
                            break;
                        case R.id.nav_chart_pie:
                            if (flag_charts) {
                                flag_panel = true;
                                flag_history = true;
                                flag_charts = false;
                                selectedFragment = new ChartFragment();
                            }
                            break;
                    }

                    try {
                        getSupportFragmentManager().
                                beginTransaction().
                                setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,
                                        R.anim.enter_from_right, R.anim.exit_to_right).
                                replace(R.id.fragment_container, selectedFragment).
                                commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            };
}