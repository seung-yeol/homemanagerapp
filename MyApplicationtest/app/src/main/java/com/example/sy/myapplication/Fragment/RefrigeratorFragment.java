package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sy.myapplication.AdderActivity.RefrigeratorAdderActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DialogUtil;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.TabHostUtil;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;
import com.example.sy.myapplication.Utils.list.List_Item;
import com.example.sy.myapplication.Utils.swipe.SwipeDismissListViewTouchListener;
import com.melnykov.fab.FloatingActionButton;

public class RefrigeratorFragment extends Fragment{
    private FloatingActionButton fab;

    final static int urgent = 1;
    final static int warning = 2;
    final static int normal = 3;
    final static int Refrigerator = 3;

    ArrayListUtil ALU = new ArrayListUtil();
    DialogUtil dialogU = new DialogUtil();
    StatusSave stat = new StatusSave();
    TabHostUtil THU = new TabHostUtil();

    ListView lv1;
    ListView lv2;
    ListView lv3;
    static ListView D_lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_sub,null);

        stat.setNumber(Refrigerator);
        stat.setTabNumber(urgent);

        setFloatngButton(root);

        lv1 = (ListView)root.findViewById(R.id.list3);
        lv2 = (ListView)root.findViewById(R.id.list4);
        lv3 = (ListView)root.findViewById(R.id.list5);
        THU.set_tabhost(root,lv1,lv2,lv3);
        stat.setD_lv(lv1);

        list_setting(lv1,urgent);
        list_setting(lv2,warning);
        list_setting(lv3,normal);

        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        list_setting(lv1,urgent);
        list_setting(lv2,warning);
        list_setting(lv3,normal);
    }

    public void list_setting(final ListView lv,final int tabname){
        if(tabname==urgent){
            ALU.li_urgent(lv,getActivity(),Refrigerator);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabname==warning){
            ALU.li_warning(lv,getActivity(),Refrigerator);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else{
            ALU.li_normal(lv,getActivity(),Refrigerator);
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

                                    list_update(dbUtil,title,lv,tabname);
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }
    public void list_update(DBUtil dbUtil,String title,ListView lv,int tabname){
        dbUtil.update(title); //드래그시 자동갱신
        if(tabname==urgent){
            ALU.li_urgent(lv,getActivity(),Refrigerator);
        }
        else if(tabname==warning){
            ALU.li_warning(lv,getActivity(),Refrigerator);
        }
        else if(tabname==normal){
            ALU.li_normal(lv,getActivity(),Refrigerator);
        }
    }
    public void setFloatngButton(View root){
        fab = (FloatingActionButton)root.findViewById(R.id.adder);
        //fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RefrigeratorAdderActivity.class);
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

            dialogU.dialog(getActivity(),s,D_lv);
        }
    };
}