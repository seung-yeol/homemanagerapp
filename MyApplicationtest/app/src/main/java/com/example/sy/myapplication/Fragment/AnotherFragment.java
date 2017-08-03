package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.example.sy.myapplication.AdderActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.MyAdapter;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

public class AnotherFragment extends Fragment implements TabHost.OnTabChangeListener{
    //private ListSet ALU = new ListSet();
    private DRDialog DRD = new DRDialog();
    private StatusSave stat = StatusSave.getInstance();
    private DBUtil dbUtil;

    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    private Drawable face_1, face_2, face_3;

    private ArrayList<String>[] mMyData;
    private SwipeMenuListView[] mListView;
    private MyAdapter[] mMyAdapter;

    public static AnotherFragment newInstance(){
        AnotherFragment INSTANCE = new AnotherFragment();
        Bundle args =  new Bundle();
        INSTANCE.setArguments(args);

        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_sub,null);

        mListView = new SwipeMenuListView[3];
        mListView[0] = (SwipeMenuListView) root.findViewById(R.id.list3);
        mListView[1] = (SwipeMenuListView) root.findViewById(R.id.list4);
        mListView[2] = (SwipeMenuListView) root.findViewById(R.id.list5);

        stat.setTabGrade(StatusSave.TabGrade.URGENT);
        stat.setListView(mListView[0]);

        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        setFloatngButton(root);

        face_1 = getResources().getDrawable(R.drawable.ic_sad_selector);
        face_2 = getResources().getDrawable(R.drawable.ic_soso_selector);
        face_3 = getResources().getDrawable(R.drawable.ic_smile_selector);

        setTabHost(root);

        dbUtil = DBUtil.getInstance();

        mMyData = new ArrayList[3];
        mMyAdapter = new MyAdapter[3];

        mMyData[0] = dbUtil.getTypeData(StatusSave.TabGrade.URGENT);
        mMyData[1] = dbUtil.getTypeData(StatusSave.TabGrade.WARNING);
        mMyData[2] = dbUtil.getTypeData(StatusSave.TabGrade.NORMAL);

        for (int i = 0 ; i < 3 ; i++){
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
    @Override
    public void onResume(){
        super.onResume();

        for (MyAdapter myAdapter : mMyAdapter){
            myAdapter.notifyDataSetChanged();
        }

        listRefresh();
    }

    public void setFloatngButton(final View root){
        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.adder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(),AdderActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setTabHost( View root ){
        TabHost host = (TabHost)root.findViewById(R.id.tabHost);

        setTabWiget(host, face_1, R.id.tab1, s_urgent);
        setTabWiget(host, face_2, R.id.tab2, s_warning);
        setTabWiget(host, face_3, R.id.tab3, s_normal);

        host.setOnTabChangedListener(this);
        host.setup();
    }

    public void setTabWiget(TabHost host, Drawable draw, int i, String s){
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
            case "괜춘":
                stat.setTabGrade(StatusSave.TabGrade.NORMAL);
                stat.setListView(mListView[2]);
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
    public void listRefresh(){
        mMyData[0] = dbUtil.getTypeData(StatusSave.TabGrade.URGENT);
        mMyData[1] = dbUtil.getTypeData(StatusSave.TabGrade.WARNING);
        mMyData[2] = dbUtil.getTypeData(StatusSave.TabGrade.NORMAL);

        for (int i = 0 ; i < 3 ; i++){
            final ArrayList<String> data = mMyData[i];
            mMyAdapter[i].listRefresh( mMyData[i] );

            mListView[i].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String title = data.get(position);
                    DRD.DRDialog( getActivity() , title, StatusSave.getInstance().getListView() );
                }
            });
        }
    }

    /*public class exampleSimpleAdapter extends com.baoyz.swipemenulistview.SwipeMenuAdapter{

        public exampleSimpleAdapter(Context context, ListAdapter adapter) {
            super(context, adapter);
        }

        AdapterView.OnItemClickListener
    }*/
}