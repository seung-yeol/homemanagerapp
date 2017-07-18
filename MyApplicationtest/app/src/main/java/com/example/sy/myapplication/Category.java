package com.example.sy.myapplication;

/**
 * Created by Osy on 2017-07-18.
 */

public enum Category {
    Clearup( 1, "ex)화장실청소" ), Laundry( 2, "ex)이불빨래"), Refrigerator( 3, "ex)먹다남은치킨"),
    Main(4), Developer(5), License(6);

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
