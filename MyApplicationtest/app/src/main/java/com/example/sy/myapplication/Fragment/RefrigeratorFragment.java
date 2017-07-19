package com.example.sy.myapplication.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sy.myapplication.AdderActivity;
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

    private ArrayListUtil ALU = new ArrayListUtil();
    private DialogUtil dialogU = new DialogUtil();
    private StatusSave stat = StatusSave.getInstance();
    private TabHostUtil THU = new TabHostUtil();

    private ListView lv1;
    private ListView lv2;
    private ListView lv3;
    private ListView D_lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View root = inflater.inflate(R.layout.fragment_sub,null);

        stat.setCategory(StatusSave.Category.REFRIGERATOR);
        stat.setTabGrade(StatusSave.TabGrade.URGENT);

        setFloatngButton(root);

        lv1 = (ListView)root.findViewById(R.id.list3);
        lv2 = (ListView)root.findViewById(R.id.list4);
        lv3 = (ListView)root.findViewById(R.id.list5);
        THU.set_tabhost(root,lv1,lv2,lv3);
        stat.setListView(lv1);

        list_setting(lv1, StatusSave.TabGrade.URGENT);
        list_setting(lv2, StatusSave.TabGrade.WARNING);
        list_setting(lv3, StatusSave.TabGrade.NORMAL);

        return root;
    }
    @Override
    public void onResume(){
        super.onResume();

        list_setting(lv1, StatusSave.TabGrade.URGENT);
        list_setting(lv2, StatusSave.TabGrade.WARNING);
        list_setting(lv3, StatusSave.TabGrade.NORMAL);
    }

    private void list_setting(final ListView lv, final StatusSave.TabGrade tabGrade){
        if(tabGrade == StatusSave.TabGrade.URGENT){
            ALU.li_urgent(lv, getActivity(), StatusSave.Category.REFRIGERATOR);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            ALU.li_warning(lv, getActivity(), StatusSave.Category.REFRIGERATOR);
            lv.setOnItemClickListener(mItemClickListener);
        }
        else{
            ALU.li_normal(lv, getActivity(), StatusSave.Category.REFRIGERATOR);
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

                                    list_update( dbUtil, title, lv, tabGrade );
                                }
                            }
                        });
        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener(touchListener.makeScrollListener());
    }
    private void list_update(DBUtil dbUtil, String title, ListView lv, StatusSave.TabGrade tabGrade){
        dbUtil.update(title); //드래그시 자동갱신
        if(tabGrade == StatusSave.TabGrade.URGENT){
            ALU.li_urgent(lv,getActivity(), StatusSave.Category.REFRIGERATOR);
        }
        else if(tabGrade == StatusSave.TabGrade.WARNING){
            ALU.li_warning(lv,getActivity(), StatusSave.Category.REFRIGERATOR);
        }
        else if(tabGrade == StatusSave.TabGrade.NORMAL){
            ALU.li_normal(lv,getActivity(), StatusSave.Category.REFRIGERATOR);
        }
    }
    private void setFloatngButton(View root){
        fab = (FloatingActionButton)root.findViewById(R.id.adder);
        //fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdderActivity.class);
                startActivity(intent);
            }
        });
    }

    //리스트 터치시 다이얼로그 실행 리스너
    private ListView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            List_Item item = (List_Item) parent.getItemAtPosition(position) ;
            String s = item.getTitle();

            dialogU.dialog(getActivity(),s,D_lv);
        }
    };
}