package com.example.sy.myapplication.Utils;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.sy.myapplication.R;

public class TabHostUtil extends Fragment{
    final static int urgent = 1;
    final static int warning = 2;
    final static int normal = 3;
    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    static Drawable face_1;
    static Drawable face_2;
    static Drawable face_3;

    private StatusSave statusSave = StatusSave.getInstance();

    public void set_tabhost(View root,final ListView lv1,final ListView lv2,final ListView lv3){
        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        setTabWiget(host, face_1, R.id.tab1, s_urgent);
        setTabWiget(host, face_2, R.id.tab2, s_warning);
        setTabWiget(host, face_3, R.id.tab3, s_normal);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case "제발":
                        statusSave.setTabNumber(urgent);
                        statusSave.setListView(lv1);
                        break;
                    case "주의":
                        statusSave.setTabNumber(warning);
                        statusSave.setListView(lv2);
                        break;
                    case "괜춘":
                        statusSave.setTabNumber(normal);
                        statusSave.setListView(lv3);
                        break;
                }
            }
        });
    }
    public void setTabWiget(TabHost host, Drawable draw,int i,String s){
        TabHost.TabSpec spec = host.newTabSpec(s);
        spec.setContent(i);
        spec.setIndicator("",draw);
        host.addTab(spec);
    }
    //메인프래그먼트 실행시 실행되는 메서드 위젯의 아이콘 표정 세팅.
    public void setface(Drawable d1, Drawable d2, Drawable d3){
        face_1 = d1;
        face_2 = d2;
        face_3 = d3;
    }
}
