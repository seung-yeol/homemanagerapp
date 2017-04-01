package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sy.myapplication.AdderActivity.LaundryAdderActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DialogUtil;
import com.example.sy.myapplication.Utils.StatusSave;
import com.example.sy.myapplication.Utils.TabHostUtil;
import com.example.sy.myapplication.Utils.list.ArrayListUtil;
import com.example.sy.myapplication.Utils.list.List_Item;
import com.example.sy.myapplication.Utils.swipe.SwipeDismissListViewTouchListener;
import com.melnykov.fab.FloatingActionButton;


public class LaundryFragment extends Fragment{
    private FloatingActionButton fab;

    final static int urgent = 1;
    final static int warning = 2;
    final static int normal = 3;
    final static int Laundry = 2;

    ArrayListUtil ALU = new ArrayListUtil();
    DialogUtil dialogU = new DialogUtil();
    StatusSave stat = new StatusSave();
    TabHostUtil THU = new TabHostUtil();

    ListView lv1;
    ListView lv2;
    ListView lv3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_sub,null);

        stat.setNumber(Laundry);
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

    public void list_setting(final ListView lv,final int tabName){
        if(tabName==urgent){
            ALU.li_urgent(lv,getActivity(),Laundry);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabName==warning){
            ALU.li_warning(lv,getActivity(),Laundry);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else{
            ALU.li_normal(lv,getActivity(),Laundry);
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
    public void list_update(DBUtil dbUtil,String title,ListView lv,int tabName){
        dbUtil.update(title); //드래그시 자동갱신
        if(tabName==urgent){
            ALU.li_urgent(lv,getActivity(),Laundry);
        }
        else if(tabName==warning){
            ALU.li_warning(lv,getActivity(),Laundry);
        }
        else if(tabName==normal){
            ALU.li_normal(lv,getActivity(),Laundry);
        }
    }
    public void setFloatngButton(final View root){
        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.adder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(),LaundryAdderActivity.class);
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

            dialogU.dialog(getActivity(),s,stat.D_lv);
        }
    };

}

