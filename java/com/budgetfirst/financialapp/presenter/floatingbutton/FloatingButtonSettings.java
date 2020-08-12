package com.budgetfirst.financialapp.presenter.floatingbutton;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloatingButtonSettings {

    private FloatingButtonContract.View view;
    private boolean flag_float_btn;

    public FloatingButtonSettings(FloatingButtonContract.View view) {
        this.view = view;
    }

    public void floatingButtonPressed(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_float_btn) {
                    showFloatButtons();
                } else {
                    hideFloatButtons();
                }
            }
        });
    }

    public void showFloatButtons() {
        flag_float_btn = true;
        floatButtonAnimation(view.getmYearBtn(), "translationX", -200);
        floatButtonAnimation(view.getmMonthBtn(), "translationX", -400);
        floatButtonAnimation(view.getmDayBtn(),"translationX", -600);
        floatButtonAnimation(view.getmShowAllBtn(), "translationY", -200);
    }

    public void hideFloatButtons() {
        flag_float_btn = false;
        floatButtonAnimation(view.getmYearBtn(), "translationX", 0);
        floatButtonAnimation(view.getmMonthBtn(), "translationX", 0);
        floatButtonAnimation(view.getmDayBtn(),"translationX", 0);
        floatButtonAnimation(view.getmShowAllBtn(), "translationY", 0);
    }

    private void floatButtonAnimation(Button button, String propertyName, float f) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(button, propertyName, f);
        animator.setDuration(500);
        animator.start();
    }
}