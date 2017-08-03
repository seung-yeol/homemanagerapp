package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
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

        mMyData = new ArrayList[2];
        mMyAdapter = new MyAdapter[2];

        mMyData[0] = dbUtil.getTypeData(StatusSave.TabGrade.URGENT);
        mMyData[1] = dbUtil.getTypeData(StatusSave.TabGrade.WARNING);

        for (int i = 0 ; i < 2 ; i++){
            final ArrayList<String> data = mMyData[i];
            mMyAdapter[i] = new MyAdapter(getActivity(), data);

            mListView[i].setAdapter(mMyAdapter[i]);
            mListView[i].setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                    switch (index) {
                        case 0:
                            // open
                            break;
                        case 1:
                            // delete
                            break;
                    }
                    return false;
                }
            });
            mListView[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = data.get(position);
                    DRD.DRDialog( getActivity() , title, StatusSave.getInstance().getListView() );
                }
            });
            mListView[i].setMenuCreator(swipeMenu());
        }

        return root;
    }
    /*//리스트 터치시 다이얼로그 실행 함수
    ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            List_Item item = (List_Item) parent.getItemAtPosition(position) ;
            String name = item.getTitle();

            DRD.DRDialog(getActivity(), name, stat.getListView());
        }
    };*/

    /*public void list_setting(final ListView lv,final StatusSave.TabGrade tabGrade){
        if(tabGrade == StatusSave.TabGrade.URGENT){
            //ALU.listSet(lv,getActivity(), StatusSave.Category.MAIN);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            //ALU.li_warning(lv,getActivity(), StatusSave.Category.MAIN);
            lv.setOnItemClickListener(mItemClickListener);
        }
        //리스트 슬라이드시
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(lv,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //DBUtil dbUtil = new DBUtil(getActivity(), "HomeManager.db", null, 1);

                                    List_Item item = (List_Item)listView.getAdapter().getItem(position);
                                    String title = item.getTitle();

                                    //list_update(dbUtil,title,lv,tabGrade);
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }*/
    /*public void list_update(DBUtil dbUtil, String s1, ListView lv, StatusSave.TabGrade tabGrade){
        dbUtil.update(s1); //드래그시 자동갱신
        if(tabGrade == StatusSave.TabGrade.URGENT){
            //ALU.listSet(lv,getActivity(), StatusSave.Category.MAIN);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            //ALU.li_warning(lv,getActivity(), StatusSave.Category.MAIN);
        }
    }*/

    //정지될시 리스트 재생성, db의 내용이 바뀔 수도 있어서
    @Override
    public void onResume(){
        super.onResume();
        for (MyAdapter myAdapter : mMyAdapter){
            myAdapter.notifyDataSetChanged();
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
                //ALU.listSet(lv1,getActivity(), stat.getCategory());
                break;
            case "주의":
                stat.setTabGrade(StatusSave.TabGrade.WARNING);
                stat.setListView(mListView[1]);
                //ALU.listSet(lv2,getActivity(), stat.getCategory());
                break;
        }
    }
    private SwipeMenuCreator swipeMenu(){
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem( getContext() );
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set a icon
                openItem.setIcon(R.mipmap.refresh_btn);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem( getContext() );
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