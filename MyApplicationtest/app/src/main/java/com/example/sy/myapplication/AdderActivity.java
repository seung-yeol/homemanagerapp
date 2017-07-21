package com.example.sy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sy.myapplication.Service.MyService;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.DateUtil;
import com.example.sy.myapplication.Utils.Dialog.AlertDialog;
import com.example.sy.myapplication.Utils.Dialog.DatePickerDialog;
import com.example.sy.myapplication.Utils.StatusBarUtil;
import com.example.sy.myapplication.Utils.StatusSave;

/**
 * Created by Osy on 2017-07-18.
 */

public class AdderActivity extends AppCompatActivity {
    private StatusSave statusSave = StatusSave.getInstance();

    private AlertDialog ALD = new AlertDialog();
    private DateUtil DateU = new DateUtil();
    private DatePickerDialog DPD = new DatePickerDialog();

    private TextView date;
    private static int i;

    private String hint;

    public AdderActivity(){
        statusSave = StatusSave.getInstance();
        hint = statusSave.getCategory().getHint();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder);
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
        edit_name.setHint(hint);
        edit_name.setHintTextColor(getResources().getColor(R.color.hint));

        //포커스가 바뀔 때 작성한 내용 임시 저장후 저장버튼클릭시 저장.
        final EditText memo = (EditText)findViewById(R.id.editText2);

        date = (TextView)findViewById(R.id.expiry_date) ;
        //버튼클릭시 타임피커 다이얼로그 실행
        ImageButton btn_date = (ImageButton) findViewById(R.id.type_select);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog();
            }
        });

        //저장버튼 클릭시 db에 내용 저장
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_name.getText().toString().equals("")){
                    //이름칸 비었을경우
                    ALD.alertDialog(AdderActivity.this,"이름칸이 비었습니다.");
                }
                else if(mDBUtil.overlap_check(edit_name.getText().toString())){
                    //이름이 중복되는경우
                    ALD.alertDialog(AdderActivity.this,"중복되는 이름입니다. \n 다시 작성해주세요.");
                }
                else if(date.getText().length()==0){
                    //만기일을 지정하지 않은경우
                    ALD.alertDialog(AdderActivity.this,"완료일을 지정해주세요");
                }
                else{
                    //다 잘된경우 db에 입력.
                    DBinsert(mDBUtil, edit_name.getText().toString(), DPD.getDate(), DateU.ToDay(), DateU.W_Date(i), memo.getText().toString());
                    Intent intent = new Intent(AdderActivity.this, MyService.class);
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

    //타임핔ㅓ 다이얼로그 실행함수
    private void timePickerDialog(){
        DPD.datePickerDialog(AdderActivity.this,date);
    }

    //db에 내용 저장하는 함수
    private void DBinsert(DBUtil mDBUtil, String name, String exripy_date, String today, String warning, String memo){
        mDBUtil.insertData(name, exripy_date , today, warning, memo, statusSave.getCategory().getNum());
        finish();
    }
}
