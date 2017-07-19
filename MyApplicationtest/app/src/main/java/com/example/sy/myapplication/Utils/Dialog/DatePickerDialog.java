package com.example.sy.myapplication.Utils.Dialog;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.sy.myapplication.Utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by WJM on 2017-07-19.
 */

public class DatePickerDialog {

    private DateUtil DateU = new DateUtil();
    private static String date;
    
    //리스트 추가시 만기일자를 선택하는 타임핔ㅓ 다이얼로그
    public void datePickerDialog(Context context, final TextView tv) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog.OnDateSetListener mDateSetListener =
                new android.app.DatePickerDialog.OnDateSetListener() {
                    // onDateSet method
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        updateDate(tv, cal);
                        DateU.W_Date_set( cal);
                    }
                };
        android.app.DatePickerDialog alert =
                new android.app.DatePickerDialog(context, mDateSetListener, year, month, day);
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
