package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TabHost;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.MyAdapter;

import java.util.ArrayList;


public class MainFragment extends Fragment implements TabHost.OnTabChangeListener{
    //private ListSet ALU = new ListSet();
    private DRDialog DRD = new DRDialog();
    private StatusSave stat = StatusSave.getInstance();
    private DBUtil dbUtil;

    final static String s_urgent = "제발";
    final static String s_warning = "주의";

    private ArrayList<String>[] mMyData;
    private SwipeMenuListView[] mListView;
    private MyAdapter[] mMyAdapter;

    private Drawable face_1, face_2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_main,null);

        mListView = new SwipeMenuListView[2];
        mListView[0] = (SwipeMenuListView) root.findViewById(R.id.list1);
        mListView[1] = (SwipeMenuListView) root.findViewById(R.id.list2);

        stat.setCategory(StatusSave.Category.MAIN);
        stat.setTabGrade(StatusSave.TabGrade.URGENT);
        stat.setListView(mListView[0]);

        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        face_1 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_sad_selector);
        face_2 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_soso_selector);

        setTabHost( root );

        dbUtil = DBUtil.getInstance(getActivity(), "HomeManager.db", null, 1);

        mMyAdapter = new MyAdapter[2];

        for (int i = 0 ; i < 2 ; i++){
            final MyAdapter myAdapter = new MyAdapter(getActivity());
            myAdapter.setTabGrade( i+1 );
            myAdapter.dataRefresh();

            mMyAdapter[i] = myAdapter;

            final SwipeMenuListView swipeMenuView = mListView[i];
            swipeMenuView.setAdapter(myAdapter);
            swipeMenuView.setMenuCreator(swipeMenu());
            //냉장고일때는 삭제만
            if (StatusSave.getInstance().getCategory() == StatusSave.Category.REFRIGERATOR){
                swipeMenuView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                // 삭제
                                swipeMenuView.closeMenu();
                                myAdapter.removeItem(position);
                                break;
                        }
                        return false;
                    }
                });
            }
            else{
                swipeMenuView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        switch (index) {
                            case 0:
                                // 갱신
                                swipeMenuView.closeMenu();
                                myAdapter.updateItem(position);
                                break;
                            case 1:
                                // 삭제
                                swipeMenuView.closeMenu();
                                myAdapter.removeItem(position);
                                break;
                        }
                        return false;
                    }
                });
            }
            swipeMenuView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = myAdapter.getTitle(position);
                    String memo = myAdapter.getMemo(position);
                    DRD.DRDialog( getActivity() , title, memo, StatusSave.getInstance().getListView() );
                }
            });

        }

        return root;
    }


    //정지될시 리스트 재생성, db의 내용이 바뀔 수도 있어서
    @Override
    public void onResume(){
        super.onResume();
        //어뎁터 데이터들 다시 갱신.
        for (MyAdapter myAdapter : mMyAdapter){
            myAdapter.dataRefresh();
        }
    }

    public void setTabHost( View root ){
        TabHost host = (TabHost)root.findViewById(R.id.tabHost);

        setTabWidget(host, face_1, R.id.tab1, s_urgent);
        setTabWidget(host, face_2, R.id.tab2, s_warning);

        host.setOnTabChangedListener(this);
        host.setup();
    }

    public void setTabWidget(TabHost host, Drawable draw, int i, String s){
        TabHost.TabSpec spec = host.newTabSpec(s);
        spec.setContent(i);
        spec.setIndicator("",draw);
        host.addTab(spec);
    }

    @Override
    public void onTabChanged(String tabId) {
        switch (tabId){
            case "제발":
                stat.setTabGrade(StatusSave.TabGrade.URGENT);
                stat.setListView(mListView[0]);
                break;
            case "주의":
                stat.setTabGrade(StatusSave.TabGrade.WARNING);
                stat.setListView(mListView[1]);
                break;
        }
    }
    private SwipeMenuCreator swipeMenu(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type

                Log.e("이게무슨", "create: " + menu.getViewType()+ " " + StatusSave.Category.REFRIGERATOR.getNum()  ,null );
                if (menu.getViewType() == StatusSave.Category.REFRIGERATOR.getNum()){
                    refrigeratorMenu(menu);
                }
                else createMenu(menu);
            }

            public void refrigeratorMenu(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem( getActivity() );
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
            public void createMenu(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem( getActivity() );
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set a icon
                openItem.setIcon(R.mipmap.refresh_btn);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem( getActivity() );
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        return creator;
    }
    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}