package com.example.sy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.sy.myapplication.Utils.StatusBarUtil;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        StatusBarUtil SBU = new StatusBarUtil();
        SBU.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler() , 10); // 3초 후에 hd Handler 실행
    }

    private class splashhandler implements Runnable{
        public void run() {
            startActivity(new Intent(getApplication(), NavigationActivity.class)); // 로딩이 끝난후 이동할 Activity
            finish();
        }
    }
}

