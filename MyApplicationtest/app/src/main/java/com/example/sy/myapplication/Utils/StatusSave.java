package com.example.sy.myapplication.Utils;

import android.widget.ListView;

//normal 인지 warning 인지 urgent 인지 잠시 내용저장하기위한 클래스
public class StatusSave {
    public static int ActNumber;
    public static int TabNumber;
    public static ListView D_lv;

    public void setNumber(int A){
        ActNumber = A;
    }
    public void setTabNumber(int s){
        TabNumber = s;
        //Log.i("이거다ㅏㅏㅏㅏㅏㅏ", "setTabStr: "+TabString);
    }
    public void setD_lv(ListView lv){
        D_lv = lv;
    }
}
