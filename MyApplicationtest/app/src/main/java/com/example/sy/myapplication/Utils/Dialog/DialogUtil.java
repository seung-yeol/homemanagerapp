package com.example.sy.myapplication.Utils.Dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sy.myapplication.Service.MyService;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.DateUtil;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogUtil extends AppCompatActivity {
    DateUtil DateU = new DateUtil();
    ArrayListUtil ALU = new ArrayListUtil();

    //Warning w = new Warning();
    public static String s;
    public AlertDialog.Builder builder;

    static int refrigerator =3;

    //리스트 터치시 다이얼로그 띄우는 함수
    public void dialog(final Context c, final String str,final ListView lv){
        builder = new AlertDialog.Builder(c);

        final DBUtil dbUtil = new DBUtil(c, "HomeManager.db", null, 1);
        final String dbs = dbUtil.getData(str);

        TextView title = new TextView(c);
        title.setText(str+" 세부 내용");
        title.setPadding(10,15,10,15);
        title.setLines(1);
        title.setGravity(Gravity.CENTER);
        //title.setTextColor(Color.rgb(136,136,136));
        title.setTextSize(20);
        builder.setCustomTitle(title);

        // 여기서부터는 알림창의 속성 설정
        builder.setMessage(dbs)        // 메세지 설정
        .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
        .setNegativeButton("삭제",
            new DialogInterface.OnClickListener(){
                // 삭제 버튼 클릭시 설정
                public void onClick(DialogInterface dialog, int whichButton){
                    dbUtil.delete(str);
                    StatusSave an = new StatusSave();
                    if (an.TabNumber==1){
                        ALU.li_urgent(lv,c,an.ActNumber);
                    }
                    else if(an.TabNumber==2){
                        ALU.li_warning(lv,c,an.ActNumber);
                    }
                    else{
                        ALU.li_normal(lv,c,an.ActNumber);
                    }
                    Intent intent = new Intent(c, MyService.class);
                    c.startService(intent);
                    dialog.cancel();
                }
        });
        StatusSave an = new StatusSave();
        if(an.ActNumber!=refrigerator){
            builder.setPositiveButton("갱신",
                    new DialogInterface.OnClickListener(){
                        // 갱신 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dbUtil.update(str);
                            StatusSave an = new StatusSave();
                            if (an.TabNumber==1){
                                ALU.li_urgent(lv,c,an.ActNumber);
                            }
                            else if(an.TabNumber==2){
                                ALU.li_warning(lv,c,an.ActNumber);
                            }
                            else{
                                ALU.li_normal(lv,c,an.ActNumber);
                            }
                            Intent intent = new Intent(c, MyService.class);
                            c.startService(intent);
                            dialog.cancel();
                        }
                    });
        }
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 뒤에 안어두워짐
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xba333333));;
        dialog.show();    // 알림창 띄우기
    }
    //리스트 추가시 null인부분이 있는경우 띄워지는 다이얼로그
    public void dialog_a(Context c, String s){
        builder = new AlertDialog.Builder(c);

        TextView title = new TextView(c);
        title.setText("으 아닝!");
        title.setPadding(10,15,10,15);
        title.setLines(1);
        title.setGravity(Gravity.CENTER);
        //title.setTextColor(Color.rgb(136,136,136));
        title.setTextSize(20);
        builder.setCustomTitle(title);

        // 여기서 부터는 알림창의 속성 설정
        builder.setMessage(s)        // 메세지 설정
                .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener(){
                            // 갱신 버튼 클릭시 설정
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0xba333333));;
        dialog.show();    // 알림창 띄우기

    }
    //리스트 추가시 만기일자를 선택하는 타임핔ㅓ 다이얼로그
    public void DialogDatePicker(Context context, final TextView tv) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year,monthOfYear,dayOfMonth);
                        updateDate(tv,cal);
                        DateU.W_Date_set(cal);
                    }
                };
        DatePickerDialog alert =
                new DatePickerDialog(context, mDateSetListener, year, month, day);
        alert.show();
    }
    //바뀐날짜값 저장되있음.
    public void updateDate(TextView tv, Calendar c){
        SimpleDateFormat fm = new SimpleDateFormat(
                "yyyy - MM - dd");
        String str =  fm.format(c.getTime());
        tv.setText(str);
        SimpleDateFormat fm2 = new SimpleDateFormat(
                "yyyyMMdd");
        s = fm2.format(c.getTime());
    }

}