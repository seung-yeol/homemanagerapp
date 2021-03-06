package com.example.sy.myapplication.Utils;

import android.widget.ListView;

//normal 인지 warning 인지 urgent 인지 잠시 내용저장하기위한 클래스
public enum StatusSave {
    INSTANCE;

    public static StatusSave getInstance() {
        return INSTANCE;
    }

    private TabGrade tabGrade;
    private Category category;
    private ListView listView;

    public void setTabGrade(TabGrade tabGrade){
        this.tabGrade = tabGrade;
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public void setListView(ListView lv){
        listView = lv;
    }

    public TabGrade getTabGrade() {
        return tabGrade;
    }

    public Category getCategory() {
        return category;
    }

    public ListView getListView() {
        return listView;
    }

    public enum TabGrade{
        URGENT(1), WARNING(2), NORMAL(3);

        private int num;

        TabGrade(int num){
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public TabGrade convertForNum(int num){
            StatusSave.TabGrade tabGrade = null;
            for (StatusSave.TabGrade mTabGrade : StatusSave.TabGrade.values()) {
                if (mTabGrade.getNum() == num) {
                    tabGrade = mTabGrade;
                }
            }
            return tabGrade;
        }
    }

    public enum Category {
        CLEARUP( 1, "ex)대청소" ), LAUNDRY( 2, "ex)이불빨래"), REFRIGERATOR( 3, "ex)우유"),
        MAIN(4), DEVELOPER(5), LICENSE(6);

        Category(int num, String hint){
            this.num = num;
            this.hint = hint;
        }

        Category(int num){
            this.num = num;
        }

        private int num;
        private String hint;

        public int getNum() {
            return num;
        }
        public String getHint() {
            return hint;
        }
    }
}