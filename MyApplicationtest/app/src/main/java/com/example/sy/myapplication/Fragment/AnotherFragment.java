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
import android.widget.ImageView;
import android.widget.TabHost;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.sy.myapplication.AdderActivity;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Tips.ListviewActivity;
import com.example.sy.myapplication.Utils.DBUtil;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;
import com.example.sy.myapplication.Utils.MyAdapter;
import com.example.sy.myapplication.Utils.StatusSave;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

public class AnotherFragment extends Fragment implements TabHost.OnTabChangeListener{
    private DRDialog DRD = new DRDialog();
    private StatusSave stat = StatusSave.getInstance();
    private DBUtil dbUtil;

    final static String s_urgent = "제발";
    final static String s_warning = "주의";
    final static String s_normal = "괜춘";

    private Drawable face_1, face_2, face_3;

    private FloatingActionButton actionButton;
    public SubActionButton button1;
    private SubActionButton button2;
    private FloatingActionMenu actionMenu;

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

        setFloatingButton(root);

        face_1 = getResources().getDrawable(R.drawable.ic_sad_selector);
        face_2 = getResources().getDrawable(R.drawable.ic_soso_selector);
        face_3 = getResources().getDrawable(R.drawable.ic_smile_selector);

        setTabHost(root);

        dbUtil = DBUtil.getInstance();

        mMyData = new ArrayList[3];
        mMyAdapter = new MyAdapter[3];


        for (int i = 0 ; i < 3 ; i++){
            final MyAdapter myAdapter = new MyAdapter(getActivity());
            myAdapter.setTabGrade( i+1 );
            myAdapter.dataRefresh();
            mMyAdapter[i] = myAdapter;

            final SwipeMenuListView swipeMenuView = mListView[i];
            swipeMenuView.setAdapter(myAdapter);
            swipeMenuView.setMenuCreator(swipeMenu());

            //냉장고일때는 하나만 삭제만 가능하게..
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
            //
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

    @Override
    public void onResume(){
        super.onResume();

        //어뎁터 데이터들 다시 재갱신.
        for (MyAdapter myAdapter : mMyAdapter){
            myAdapter.dataRefresh();
        }

        actionButton.setVisibility(View.VISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause(){
        super.onPause();

        actionButton.setVisibility(View.GONE);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
    }

    public void setFloatingButton(final View root) {
        ImageView icon = new ImageView(root.getContext()); // Create an icon
        icon.setImageDrawable(getResources().getDrawable(R.drawable.more));

        actionButton = new FloatingActionButton.Builder(root.getContext())
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(root.getContext());
        // repeat many times:
        ImageView itemIcon = new ImageView(root.getContext());
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.add));
        button1 = itemBuilder.setContentView(itemIcon).build();

        ImageView itemIcon1 = new ImageView(root.getContext());
        itemIcon1.setImageDrawable(getResources().getDrawable(R.drawable.tips));
        button2 = itemBuilder.setContentView(itemIcon1).build();

        actionMenu = new FloatingActionMenu.Builder(root.getContext())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .attachTo(actionButton)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), AdderActivity.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(), ListviewActivity.class);
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

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
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