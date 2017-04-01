package com.example.sy.myapplication.AdderActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Service.MyService;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.DateUtil;
import com.example.sy.myapplication.Utils.Dialog.DialogUtil;
import com.example.sy.myapplication.Utils.StatusBarUtil;

public class LaundryAdderActivity extends AppCompatActivity {

    DialogUtil dU = new DialogUtil();
    DateUtil DateU = new DateUtil();
    final static int Laundry = 2;
    public TextView date;
    public static int i;
    public static String str;
    public static String str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adder);
        i = 7;//싴바 움직이지않으면 기본값 7

        StatusBarUtil SBU = new StatusBarUtil();
        SBU.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

        final DBUtil mDBUtil = new DBUtil(getApplicationContext(), "HomeManager.db", null, 1);
        final TextView warning = (TextView) findViewById(R.id.seekbar_text);
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                warning.setText("경고기간 : " + (progress + 3) + "일");
                i = progress + 3;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final EditText edit_name = (EditText) findViewById(R.id.edit_name);
        edit_name.setHint("ex)이불빨래");
        edit_name.setHintTextColor(getResources().getColor(R.color.hint));
        edit_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    str = edit_name.getText().toString();
                } else {
                    return;
                }
            }
        });
        final EditText detail= (EditText)findViewById(R.id.editText2);
        detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    str2 = detail.getText().toString();
                } else {
                    return;
                }
            }
        });
        date = (TextView)findViewById(R.id.expiry_date) ;
        ImageButton btn_date = (ImageButton) findViewById(R.id.type_select);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerdialog();
            }
        });
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals("")){
                    dU.dialog_a(LaundryAdderActivity.this,"이름칸이 비었습니다.");
                }
                else if(mDBUtil.overlap_check(str)){
                    dU.dialog_a(LaundryAdderActivity.this,"중복되는 이름입니다. \n 다시 작성해주세요.");
                }
                else if(date.getText().length()==0){
                    dU.dialog_a(LaundryAdderActivity.this,"완료일을 지정해주세요");
                }
                else{
                    DBinsert(mDBUtil,str,dU.s,DateU.ToDay(),DateU.W_Date(i),str2);
                    Intent intent = new Intent(LaundryAdderActivity.this, MyService.class);
                    startService(intent);
                }
            }
        });
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    private void timepickerdialog() {
        dU.DialogDatePicker(LaundryAdderActivity.this, date);
    }

    private void DBinsert(DBUtil mDBUtil, String str, String p_d, String t_d, String e_d, String detail) {
        mDBUtil.insertData(str, p_d, t_d, e_d,detail ,Laundry);
        StatusSave an = new StatusSave();
        an.setNumber(Laundry);
        finish();
    }
}