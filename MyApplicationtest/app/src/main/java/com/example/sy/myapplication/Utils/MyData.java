package com.example.sy.myapplication.Utils;

/**
 * Created by Osy on 2017-08-11.
 */

public class MyData {
    private StatusSave.Category category;
    private StatusSave.TabGrade tabGrade;
    private String title;
    private String memo;

    public MyData(String title){
        this.title = title;
        memo = DBUtil.getInstance().getDate(title);
        category = DBUtil.getInstance().getCategory(title);
    }

    public StatusSave.Category getCategory() {
        return category;
    }

    /*
    public StatusSave.TabGrade getTabGrade() {
        return tabGrade;
    }
    public void setTabGrade(StatusSave.TabGrade tabGrade) {
        this.tabGrade = tabGrade;
    }*/

    public String getTitle() {
        return title;
    }

    public String getMemo() {
        return memo;
    }
}
