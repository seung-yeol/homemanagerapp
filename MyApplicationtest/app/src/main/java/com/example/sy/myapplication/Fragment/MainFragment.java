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
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DialogUtil;
import com.example.sy.myapplication.Utils.TabHostUtil;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;
import com.example.sy.myapplication.Utils.list.List_Item;
import com.example.sy.myapplication.Utils.swipe.SwipeDismissListViewTouchListener;


public class MainFragment extends Fragment{


    final static int urgent = 1;
    final static int warning = 2;

    ArrayListUtil ALU = new ArrayListUtil();
    DialogUtil dialogU = new DialogUtil();
    TabHostUtil THU = new TabHostUtil();

    ListView lv1;
    ListView lv2;
    static ListView D_lv ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_main,null);

        StatusSave an = new StatusSave();
        an.setNumber(4);
        an.setTabNumber(urgent);

        TabHost host = (TabHost)root.findViewById(R.id.tabHost);
        host.setup();

        Drawable i1 = getResources().getDrawable(R.drawable.ic_sad_selector);
        Drawable i2 = getResources().getDrawable(R.drawable.ic_soso_selector);
        Drawable i3 = getResources().getDrawable(R.drawable.ic_smile_selector);
        THU.setface(i1,i2,i3);

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("제발");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",i1);
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("주의");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",i2);
        host.addTab(spec);

        lv1 = (ListView)root.findViewById(R.id.list1);
        lv2 = (ListView)root.findViewById(R.id.list2);
        D_lv = lv1;

        list_setting(lv1,urgent);
        list_setting(lv2,warning);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                StatusSave an = new StatusSave();
                switch (s){
                    case "제발":
                        an.setTabNumber(urgent);
                        D_lv = lv1;
                        break;
                    case "주의":
                        an.setTabNumber(warning);
                        D_lv = lv2;
                        break;
                }
            }
        });

        return root;
    }
    //리스트 터치시 다이얼로그 실행 함수
    ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            List_Item item = (List_Item) parent.getItemAtPosition(position) ;
            String s = item.getTitle();

            dialogU.dialog(getActivity(),s,D_lv);
        }
    };

    public void list_setting(final ListView lv,final int tabName){
        if(tabName==urgent){
            ALU.li_urgent(lv,getActivity(),4);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabName==warning){
            ALU.li_warning(lv,getActivity(),4);
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

                                    list_update(dbUtil,title,lv,tabName);
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }
    public void list_update(DBUtil dbUtil,String s1,ListView lv,int tabName){
        dbUtil.update(s1); //드래그시 자동갱신
        if(tabName==urgent){
            ALU.li_urgent(lv,getActivity(),4);
        }
        else if(tabName==warning){
            ALU.li_warning(lv,getActivity(),4);
        }
    }
    /*
    //윈도우의 포커스가 변경이 되는경우 중 db의 내용이 바뀌는 경우가 많아서 리스트 재생성.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus == true) {
            ALU.li_normal(lv,this,type);
        } else {
            ALU.li_normal(lv,this,type);
        }
    }
    //백버튼 누를시
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK :
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }*/
    //정지될시 리스트 재생성, db의 내용이 바뀔 수도 있어서
    @Override
    public void onResume(){
        super.onResume();
        list_setting(lv1,urgent);
        list_setting(lv2,warning);
    }
}
