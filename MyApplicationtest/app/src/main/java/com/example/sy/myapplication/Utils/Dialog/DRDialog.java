package com.example.sy.myapplication.Utils.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sy.myapplication.Utils.StatusSave;


public class DRDialog {
    //private ListSet ALU = new ListSet();

    private StatusSave statusSave = StatusSave.getInstance();

    private AlertDialog.Builder builder;

    //리스트 터치시 다이얼로그 띄우는 함수
    public void DRDialog(final Context context, final String name, final String memo ,final ListView lv){
        builder = new AlertDialog.Builder(context);

        TextView title = new TextView(context);
        title.setText(name + " 세부내용 및 메모 ");
        title.setPadding(10,15,10,15);
        title.setLines(1);
        title.setGravity(Gravity.CENTER);
        //title.setTextColor(Color.rgb(136,136,136));
        title.setTextSize(20);

        builder.setCustomTitle(title)
                .setMessage(memo);        // 메세지 설정
        if(statusSave.getCategory() != StatusSave.Category.REFRIGERATOR){
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
        }

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 뒤에 안어두워짐
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xba333333));;
        dialog.show();    // 알림창 띄우기
    }
}
