package com.example.sy.myapplication.Utils;

import android.graphics.drawable.Drawable;
import android.widget.ListView;

import com.example.sy.myapplication.Utils.list.ArrayListUtil;

public class TabHostUtil {
    private ArrayListUtil ALU;
    private StatusSave stat;

    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    private Drawable face_1, face_2, face_3;
    private ListView lv1, lv2, lv3;

    private StatusSave statusSave = StatusSave.getInstance();

    public TabHostUtil(final ListView lv1, final ListView lv2, final ListView lv3){

        ALU = new ArrayListUtil();
        stat = StatusSave.getInstance();

    }


}
