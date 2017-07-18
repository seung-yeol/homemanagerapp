package com.example.sy.myapplication.Utils;

import android.widget.ListView;

import com.example.sy.myapplication.Category;

//normal 인지 warning 인지 urgent 인지 잠시 내용저장하기위한 클래스
public enum StatusSave {
    INSTANCE;

    public static StatusSave getInstance() {
        return INSTANCE;
    }

    private int TabNumber;
    private Category category;
    private ListView listView;

    public void setTabNumber(int A){
        TabNumber = A;
    }
    public void setCategory(Category category){
        this.category = category;
        //Log.i("이거다ㅏㅏㅏㅏㅏㅏ", "setTabStr: "+TabString);
    }
    public void setListView(ListView lv){
        listView = lv;
    }

    public int getTabNumber() {
        return TabNumber;
    }

    public Category getCategory() {
        return category;
    }

    public ListView getListView() {
        return listView;
    }
}
