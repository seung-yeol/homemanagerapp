package com.example.sy.myapplication.Utils.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sy.myapplication.Service.MyService;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.StatusSave;


public class DRDialog {
    //private ListSet ALU = new ListSet();

    private StatusSave statusSave = StatusSave.getInstance();

    private AlertDialog.Builder builder;

    //리스트 터치시 다이얼로그 띄우는 함수
    public void DRDialog(final Context context, final String name, final ListView lv){
        builder = new AlertDialog.Builder(context);

        final DBUtil dbUtil = DBUtil.getInstance();
        final String dbs = dbUtil.getDate(name);

        TextView title = new TextView(context);
        title.setText(name + " 메모 ");
        title.setPadding(10,15,10,15);
        title.setLines(1);
        title.setGravity(Gravity.CENTER);
        //title.setTextColor(Color.rgb(136,136,136));
        title.setTextSize(20);
        builder.setCustomTitle(title);


        // 여기서부터는 알림창의 속성 설정
        builder.setMessage(dbs)        // 메세지 설정
                .setCancelable(true);       // 뒤로 버튼 클릭시 취소 가능 설정
               /* .setNegativeButton("삭제",
                        new DialogInterface.OnClickListener(){
                            // 삭제 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton){
                                dbUtil.delete(name);
                                if (statusSave.getTabGrade() == StatusSave.TabGrade.URGENT){
                                    //ALU.listSet(lv,context, statusSave.getCategory());
                                }
                                else if(statusSave.getTabGrade() == StatusSave.TabGrade.WARNING){
                                    //ALU.li_warning(lv,context, statusSave.getCategory());
                                }
                                else{
                                    //ALU.li_normal(lv,context, statusSave.getCategory());
                                }
                                Intent intent = new Intent(context, MyService.class);
                                context.startService(intent);
                                dialog.cancel();
                            }
                        });

        if(statusSave.getCategory() != StatusSave.Category.REFRIGERATOR){
            builder.setPositiveButton("갱신",
                    new DialogInterface.OnClickListener(){
                        // 갱신 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dbUtil.update(name);

                            if (statusSave.getTabGrade() == StatusSave.TabGrade.URGENT){
                                //ALU.listSet(lv, context, statusSave.getCategory());
                            }
                            else if(statusSave.getTabGrade() == StatusSave.TabGrade.WARNING){
                                //ALU.li_warning(lv, context, statusSave.getCategory());
                            }
                            else{
                                //ALU.li_normal(lv, context, statusSave.getCategory());
                            }

                            Intent intent = new Intent(context, MyService.class);
                            context.startService(intent);
                            dialog.cancel();
                        }
                    });
        }*/


        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 뒤에 안어두워짐
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xba333333));;
        dialog.show();    // 알림창 띄우기
    }
}
