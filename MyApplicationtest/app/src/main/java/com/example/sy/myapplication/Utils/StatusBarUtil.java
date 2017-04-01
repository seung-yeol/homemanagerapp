package com.example.sy.myapplication.Utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class StatusBarUtil {

    public void setStatusBarColor(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }
}
