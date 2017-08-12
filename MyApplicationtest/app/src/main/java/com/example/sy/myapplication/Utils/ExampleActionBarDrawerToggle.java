package com.example.sy.myapplication.Utils;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Osy on 2017-08-13.
 */

public class ExampleActionBarDrawerToggle extends ActionBarDrawerToggle {
    private DrawerSlideListener listener;
    public ExampleActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public ExampleActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);

        if (listener != null){
            listener.onDrawerSlide();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);

        if (listener != null){
            StatusSave.Category category = StatusSave.getInstance().getCategory();
            if (category != StatusSave.Category.MAIN &&
                    category != StatusSave.Category.LICENSE &&
                    category != StatusSave.Category.DEVELOPER){
                listener.onDrawerClosed();
            }
        }
    }

    public void addDrawerSlideListener(DrawerSlideListener listener){
        this.listener = listener;
    }

    public static interface DrawerSlideListener{
        void onDrawerSlide();
        void onDrawerClosed();
    }
}
