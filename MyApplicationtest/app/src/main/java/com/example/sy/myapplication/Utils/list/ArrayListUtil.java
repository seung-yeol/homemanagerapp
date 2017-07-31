package com.example.sy.myapplication.Utils.list;

import android.content.Context;
import android.widget.ListView;

import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.StatusSave;


public class ArrayListUtil{
    private ListView listView;
    public ListAdapter mAdapter;

    // 시급 리스트 내용
    public void li_urgent(ListView list , Context c , StatusSave.Category category){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        mAdapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDBUtil.getTpyeData( category, mAdapter, StatusSave.getInstance().getTabGrade());
    }
    // 주의 리스트 내용
    public void li_warning(ListView list , Context c , StatusSave.Category category){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        mAdapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(mAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mDBUtil.getTpyeData( category, mAdapter, StatusSave.getInstance().getTabGrade());
    }
    // 괜춘 리스트 내용
    public void li_normal(ListView list , Context c , StatusSave.Category category){
        DBUtil mDBUtil = new DBUtil(c, "HomeManager.db", null, 1);
        mAdapter = new ListAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listView = list;
        listView.setAdapter(mAdapter);
        listView.setChoiceMode( ListView.CHOICE_MODE_SINGLE );

        mDBUtil.getTpyeData( category, mAdapter, StatusSave.getInstance().getTabGrade());
    }
}