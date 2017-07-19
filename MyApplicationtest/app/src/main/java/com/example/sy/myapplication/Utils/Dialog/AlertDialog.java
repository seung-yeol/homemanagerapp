package com.example.sy.myapplication.Utils.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by WJM on 2017-07-19.
 */

public class AlertDialog {

    private android.support.v7.app.AlertDialog.Builder builder;

    //리스트 추가시 null인부분이 있는경우 띄워지는 다이얼로그
    public void alertDialog(Context c, String message){
        builder = new android.support.v7.app.AlertDialog.Builder(c);

        TextView title = new TextView(c);
        title.setText("으 아닝!");
        title.setPadding(10,15,10,15);
        title.setLines(1);
        title.setGravity(Gravity.CENTER);
        //title.setTextColor(Color.rgb(136,136,136));
        title.setTextSize(20);
        builder.setCustomTitle(title);

        // 여기서 부터는 알림창의 속성 설정
        builder.setMessage(message)        // 메세지 설정
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener(){
                            // 갱신 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
        android.support.v7.app.AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xba333333));;
        dialog.show();    // 알림창 띄우기

    }
}
