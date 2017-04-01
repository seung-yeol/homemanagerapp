package com.example.sy.myapplication.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.sy.myapplication.NavigationActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate(){
        super.onCreate();
    }
    public void onDestroy(){
        super.onDestroy();
    }
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent,flags,startId);

        MyThread Mythread = new MyThread();
        //Mythread.setDaemon(true);
        Mythread.start();

        return START_STICKY;
    }
    public void noti(){
        PendingIntent intent = PendingIntent.getActivity(
                this, 0, new Intent(this, NavigationActivity.class), 0);

        //커스텀 notification
        RemoteViews RV = new RemoteViews(getPackageName(),
                R.layout.notification);

        DBUtil mDBUtil = new DBUtil(this, "HomeManager.db", null, 1);
        int sadCount = mDBUtil.getTypeCount(1);
        int sosoCount = mDBUtil.getTypeCount(2);
        //<b>의 속성이 적용되도록 CharSequencs 이용
        CharSequence s = getText(R.string.noti_title);

        RV.setTextViewText(R.id.sad_txt,""+sadCount);
        RV.setTextViewText(R.id.soso_txt,""+sosoCount);
        RV.setTextViewText(R.id.noti_title,s);
        RV.setTextViewText(R.id.noti_message,"세부내용은 눌러주세요!");
        if(sadCount+sosoCount !=0){
            Notification noti = new NotificationCompat.Builder(getApplicationContext())
                    .setTicker("으햏")
                    .setSmallIcon(R.mipmap.ic_status_icon)
                    .setContent(RV)
                    .setContentIntent(intent)
                    .build();
            startForeground(1,noti);
        }
        else {
            this.stopForeground(true);
        }

    }
    class MyThread extends Thread{
        public void run(){
            while(true){
                noti();
                try {
                    Thread.sleep(60*60*1000);
                } catch (InterruptedException e) {}
            }
        }
    }
}
