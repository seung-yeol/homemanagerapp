package com.example.sy.myapplication.Utils.list;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.example.sy.myapplication.Utils.DBUtil;


public class ArrayListUtil implements View.OnTouchListener {
    private ListView listView;
    public ListAdapter madapter;
    final static int end = 1;
    final static int warning = 2;
    final static int normal = 3;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
    // 시급 리스트 내용
    public void li_urgent(ListView list , Context c , int type){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        madapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(madapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDBUtil.getTpyeData(type,madapter,end);
    }
    // 주의 리스트 내용
    public void li_warning(ListView list , Context c , int type){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        madapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(madapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDBUtil.getTpyeData(type,madapter,warning);
    }
    // 괜춘 리스트 내용
    public void li_normal(ListView list , Context c , int type){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        madapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(madapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDBUtil.getTpyeData(type,madapter,normal);
    }
}