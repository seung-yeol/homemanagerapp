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
    private DateUtil DateU = new DateUtil();
    private ArrayListUtil ALU = new ArrayListUtil();

    private StatusSave statusSave = StatusSave.getInstance();

    private static String date;
    private AlertDialog.Builder builder;

    //리스트 터치시 다이얼로그 띄우는 함수
    public void dialog(final Context context, final String name,final ListView lv){
        builder = new AlertDialog.Builder(context);

        final DBUtil dbUtil = new DBUtil(context, "HomeManager.db", null, 1);
        final String dbs = dbUtil.getData(name);

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
               .setCancelable(true)        // 뒤로 버튼 클릭시 취소 가능 설정
               .setNegativeButton("삭제",
                    new DialogInterface.OnClickListener(){
                        // 삭제 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton){
                            dbUtil.delete(name);
                            if (statusSave.getTabGrade() == StatusSave.TabGrade.URGENT){
                                ALU.li_urgent(lv,context, statusSave.getCategory());
                            }
                            else if(statusSave.getTabGrade() == StatusSave.TabGrade.WARNING){
                                ALU.li_warning(lv,context, statusSave.getCategory());
                            }
                            else{
                                ALU.li_normal(lv,context, statusSave.getCategory());
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
                                ALU.li_urgent(lv, context, statusSave.getCategory());
                            }
                            else if(statusSave.getTabGrade() == StatusSave.TabGrade.WARNING){
                                ALU.li_warning(lv, context, statusSave.getCategory());
                            }
                            else{
                                ALU.li_normal(lv, context, statusSave.getCategory());
                            }

                            Intent intent = new Intent(context, MyService.class);
                            context.startService(intent);
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
    public void dialog_a(Context c, String message){
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
        builder.setMessage(message)        // 메세지 설정
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
                        cal.set(year, monthOfYear, dayOfMonth);
                        updateDate(tv, cal);
                        DateU.W_Date_set( cal);
                    }
                };
        DatePickerDialog alert =
                new DatePickerDialog(context, mDateSetListener, year, month, day);
        alert.show();
    }

    //바뀐날짜값 저장되있음.
    private void updateDate(TextView tv, Calendar c){
        SimpleDateFormat fm = new SimpleDateFormat(
                "yyyy - MM - dd");
        String str =  fm.format(c.getTime());
        tv.setText(str);
        SimpleDateFormat fm2 = new SimpleDateFormat(
                "yyyyMMdd");
        date = fm2.format(c.getTime());
    }

    public String getDate(){
        return date;
    }
}