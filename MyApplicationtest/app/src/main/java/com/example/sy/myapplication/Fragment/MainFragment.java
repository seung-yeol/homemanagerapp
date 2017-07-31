package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;
import com.example.sy.myapplication.Utils.list.List_Item;
import com.example.sy.myapplication.Utils.swipe.SwipeDismissListViewTouchListener;


public class MainFragment extends Fragment implements TabHost.OnTabChangeListener{
    private ArrayListUtil ALU = new ArrayListUtil();
    private DRDialog DRD = new DRDialog();
    private StatusSave stat = StatusSave.getInstance();

    private ListView lv1, lv2;
    private static ListView D_lv ;

    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    private Drawable face_1, face_2;

    private StatusSave statusSave = StatusSave.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_main,null);

        statusSave.setCategory(StatusSave.Category.MAIN);
        statusSave.setTabGrade(StatusSave.TabGrade.URGENT);

        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        face_1 = getResources().getDrawable(R.drawable.ic_sad_selector);
        face_2 = getResources().getDrawable(R.drawable.ic_soso_selector);

        lv1 = (ListView)root.findViewById(R.id.list3);
        lv2 = (ListView)root.findViewById(R.id.list4);

        setTabHost( root );

        /*//Tab 1
        TabHost.TabSpec spec = host.newTabSpec("제발");
        spec.setContent( R.id.tab1 );
        spec.setIndicator("",i1);
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("주의");
        spec.setContent( R.id.tab2 );
        spec.setIndicator("",i2);
        host.addTab(spec);*/

        lv1 = (ListView)root.findViewById(R.id.list1);
        lv2 = (ListView)root.findViewById(R.id.list2);
        D_lv = lv1;

        list_setting(lv1, StatusSave.TabGrade.URGENT);
        list_setting(lv2, StatusSave.TabGrade.WARNING);

        return root;
    }
    //리스트 터치시 다이얼로그 실행 함수
    ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            List_Item item = (List_Item) parent.getItemAtPosition(position) ;
            String name = item.getTitle();

            DRD.DRDialog(getActivity(), name, D_lv);
        }
    };

    public void list_setting(final ListView lv,final StatusSave.TabGrade tabGrade){
        if(tabGrade == StatusSave.TabGrade.URGENT){
            ALU.li_urgent(lv,getActivity(), StatusSave.Category.MAIN);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            ALU.li_warning(lv,getActivity(), StatusSave.Category.MAIN);
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
                                    DBUtil dbUtil = new DBUtil(getActivity(), "HomeManager.db", null, 1);

                                    List_Item item = (List_Item)listView.getAdapter().getItem(position);
                                    String title = item.getTitle();

                                    list_update(dbUtil,title,lv,tabGrade);
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }
    public void list_update(DBUtil dbUtil, String s1, ListView lv, StatusSave.TabGrade tabGrade){
        dbUtil.update(s1); //드래그시 자동갱신
        if(tabGrade == StatusSave.TabGrade.URGENT){
            ALU.li_urgent(lv,getActivity(), StatusSave.Category.MAIN);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            ALU.li_warning(lv,getActivity(), StatusSave.Category.MAIN);
        }
    }

    //정지될시 리스트 재생성, db의 내용이 바뀔 수도 있어서
    @Override
    public void onResume(){
        super.onResume();
        list_setting(lv1,StatusSave.TabGrade.URGENT);
        list_setting(lv2,StatusSave.TabGrade.WARNING);
    }

    public void setTabHost( View root ){
        TabHost host = (TabHost)root.findViewById(R.id.tabHost);

        setTabWiget(host, face_1, R.id.tab1, s_urgent);
        setTabWiget(host, face_2, R.id.tab2, s_warning);

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
        }
    }
}