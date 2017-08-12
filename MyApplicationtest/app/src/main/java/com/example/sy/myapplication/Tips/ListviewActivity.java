package com.example.sy.myapplication.Tips;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.StatusBarUtil;

import java.util.ArrayList;

/**
 * Created by WJM on 2017-08-09.
 */

public class ListviewActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        ListView listView=(ListView)findViewById(R.id.listview);

        ArrayList<Listviewitem> data=new ArrayList<>();

        StatusBarUtil SBU = new StatusBarUtil();
        SBU.setStatusBarColor(this, getResources().getColor(R.color.TipsBar));

        Listviewitem tip1=new Listviewitem("흰 셔츠 소매 찌든 때 빼기");
        Listviewitem tip2=new Listviewitem("김치얼룩 빠르게 제거하기");
        Listviewitem tip3=new Listviewitem("청바지 세탁 방법");
        Listviewitem tip4=new Listviewitem("세탁기 청소하기");

        data.add(tip1);
        data.add(tip2);
        data.add(tip3);
        data.add(tip4);

        ListviewAdapter adapter=new ListviewAdapter(this,R.layout.activity_tips,data);
        listView.setAdapter(adapter);
    }
}