package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.sy.myapplication.AdderActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;
import com.example.sy.myapplication.Utils.list.List_Item;
import com.example.sy.myapplication.Utils.swipe.SwipeDismissListViewTouchListener;
import com.melnykov.fab.FloatingActionButton;


public class AnotherFragment extends Fragment implements TabHost.OnTabChangeListener{
    private FloatingActionButton fab;

    private ArrayListUtil ALU = new ArrayListUtil();
    private DRDialog DRD = new DRDialog();
    private StatusSave stat = StatusSave.getInstance();

    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    private Drawable face_1, face_2, face_3;
    private ListView lv1, lv2, lv3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_sub,null);

        stat.setTabGrade(StatusSave.TabGrade.URGENT);
        stat.setListView(lv1);

        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        setFloatngButton(root);

        face_1 = getResources().getDrawable(R.drawable.ic_sad_selector);
        face_2 = getResources().getDrawable(R.drawable.ic_soso_selector);
        face_3 = getResources().getDrawable(R.drawable.ic_smile_selector);

        lv1 = (ListView)root.findViewById(R.id.list3);
        lv2 = (ListView)root.findViewById(R.id.list4);
        lv3 = (ListView)root.findViewById(R.id.list5);

        setTabHost(root);

        list_setting(lv1,StatusSave.TabGrade.URGENT);
        list_setting(lv2,StatusSave.TabGrade.WARNING);
        list_setting(lv3,StatusSave.TabGrade.NORMAL);

        return root;
    }
    @Override
    public void onResume(){
        super.onResume();

        list_setting(lv1,StatusSave.TabGrade.URGENT);
        list_setting(lv2,StatusSave.TabGrade.WARNING);
        list_setting(lv3,StatusSave.TabGrade.NORMAL);
    }

    public void list_setting(final ListView lv, final StatusSave.TabGrade tabGrade){

        Log.e("셋팅중", "list_setting: " + tabGrade, null );
        if ( tabGrade == StatusSave.TabGrade.URGENT)
            ALU.li_urgent( lv,getActivity(), stat.getCategory());
        else if(tabGrade == StatusSave.TabGrade.WARNING)
            ALU.li_warning( lv,getActivity(), stat.getCategory());
        else
            ALU.li_normal(lv,getActivity(),stat.getCategory());
        lv.setOnItemClickListener(mItemClickListener);
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
                                    DBUtil dbUtil = new DBUtil(getActivity(), "HomeManager.db", null, 1);

                                    List_Item item = (List_Item)listView.getAdapter().getItem(position);
                                    String title = item.getTitle();

                                    list_update(dbUtil, title, lv, tabGrade);
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }
    public void list_update(DBUtil dbUtil,String title,ListView lv, StatusSave.TabGrade tabGrade){
        dbUtil.update(title); //드래그시 자동갱신
        if(tabGrade == StatusSave.TabGrade.URGENT){
            ALU.li_urgent(lv,getActivity(), stat.getCategory());
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            ALU.li_warning(lv,getActivity(), stat.getCategory());
        }
        else if(tabGrade == StatusSave.TabGrade.NORMAL){
            ALU.li_normal(lv,getActivity(), stat.getCategory());
        }
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

    //리스트 터치시 다이얼로그 실행 리스너
    ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            List_Item item = (List_Item) parent.getItemAtPosition(position) ;
            String s = item.getTitle();

            DRD.DRDialog(getActivity(),s,stat.getListView());
        }
    };

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
                stat.setListView(lv1);
                ALU.li_urgent(lv1,getActivity(), stat.getCategory());
                break;
            case "주의":
                stat.setTabGrade(StatusSave.TabGrade.WARNING);
                stat.setListView(lv2);
                ALU.li_urgent(lv2,getActivity(), stat.getCategory());
                break;
            case "괜춘":
                stat.setTabGrade(StatusSave.TabGrade.NORMAL);
                stat.setListView(lv3);
                ALU.li_urgent(lv3,getActivity(), stat.getCategory());
                break;
        }
    }
}