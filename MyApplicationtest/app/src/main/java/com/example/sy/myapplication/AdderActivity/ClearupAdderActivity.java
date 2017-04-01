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

public class ClearupAdderActivity extends AppCompatActivity {
    DialogUtil dU = new DialogUtil();
    DateUtil DateU = new DateUtil();
    final static int Clear_up = 1;
    public TextView date;
    public static int i;
    public static String str;
    public static String str2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adder);
        i=7;//싴바 움직이지않으면 기본값 7

        StatusBarUtil SBU = new StatusBarUtil();
        SBU.setStatusBarColor(this, getResources().getColor(R.color.colorPrimaryDark));

        final DBUtil mDBUtil = new DBUtil(getApplicationContext(), "HomeManager.db", null, 1);
        final TextView warning = (TextView)findViewById(R.id.seekbar_text);
        //싴바ㅏㅏ  변경 시 경고기간의 변경 및 텍스트 자동변경.
        SeekBar seekBar2 = (SeekBar) findViewById(R.id.seekBar);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                warning.setText("경고기간 : " + (progress +3)+"일");
                //최소 3일.
                i = progress+3;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //포커스가 바뀔 때 작성한 내용 임시 저장후 저장버튼클릭시 저장.
        final EditText edit_name = (EditText)findViewById(R.id.edit_name);
        edit_name.setHint("ex)화장실청소");
        edit_name.setHintTextColor(getResources().getColor(R.color.hint));
        edit_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    str = edit_name.getText().toString();
                }else {
                    return;
                }
            }
        });
        //포커스가 바뀔 때 작성한 내용 임시 저장후 저장버튼클릭시 저장.
        final EditText detail =(EditText)findViewById(R.id.editText2);
        detail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    str2 = detail.getText().toString();
                }else {
                    return;
                }
            }
        });

        date = (TextView)findViewById(R.id.expiry_date) ;
        //버튼클릭시 타임피커 다이얼로그 실행
        ImageButton btn_date = (ImageButton) findViewById(R.id.type_select);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerdialog();
            }
        });
        //저장버튼 클릭시 db에 내용 저장
        Button btn_save =(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals("")){
                    //이름칸 비었을경우
                    dU.dialog_a(ClearupAdderActivity.this,"이름칸이 비었습니다.");
                }
                else if(mDBUtil.overlap_check(str)){
                    //이름이 중복되는경우
                    dU.dialog_a(ClearupAdderActivity.this,"중복되는 이름입니다. \n 다시 작성해주세요.");
                }
                else if(date.getText().length()==0){
                    //만기일을 지정하지 않은경우
                    dU.dialog_a(ClearupAdderActivity.this,"완료일을 지정해주세요");
                }
                else{
                    //다 잘된경우 db에 입력.
                    DBinsert(mDBUtil,str,dU.s,DateU.ToDay(),DateU.W_Date(i),str2);
                    Intent intent = new Intent(ClearupAdderActivity.this, MyService.class);
                    startService(intent);
                }
            }
        });
        //취소버튼 클릭시
        Button btn_cancel = (Button)findViewById(R.id.btn_cancel);
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
    //에디트텍스트 포커스 없애기
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    //타임핔ㅓ 다이얼로그 실행함수
    private void timepickerdialog(){
        dU.DialogDatePicker(ClearupAdderActivity.this,date);
    }
    //db에 내용 저장하는 함수
    private void DBinsert(DBUtil mDBUtil, String str, String p_d, String t_d, String warning, String detail){
        mDBUtil.insertData(str, p_d , t_d, warning,detail,Clear_up);
        StatusSave an = new StatusSave();
        an.setNumber(Clear_up);
        finish();
    }
}