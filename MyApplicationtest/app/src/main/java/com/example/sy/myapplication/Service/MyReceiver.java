package com.example.sy.myapplication.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, MyService.class);
            context.startService(i);
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }

}
