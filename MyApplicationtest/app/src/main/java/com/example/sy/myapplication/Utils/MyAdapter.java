package com.example.sy.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Service.MyService;

import java.util.ArrayList;

/**
 * Created by Osy on 2017-08-01.
 */
public class MyAdapter extends BaseSwipListAdapter {
    private ArrayList<MyData> mMyData;
    private ArrayList<String> mDataTitle;
    private Context context;

    private StatusSave.TabGrade tabGrade;

    public MyAdapter(Context context){
        this.context = context;
    }

    public void setTabGrade(int i){
        for (StatusSave.TabGrade tabGrade : StatusSave.TabGrade.values()) {
            if (tabGrade.getNum() == i){
                this.tabGrade = tabGrade;
                break;
            }
        }
    }
    public void setTabGrade(StatusSave.TabGrade tabGrade){
        this.tabGrade = tabGrade;
    }

    public void dataRefresh(){
        mMyData = new ArrayList<>();

        mDataTitle = DBUtil.getInstance().getTypeData(tabGrade);
        for (int i = 0 ; i < mDataTitle.size() ; i++ ){
            mMyData.add(new MyData(mDataTitle.get(i)));
        }

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        // menu type 수 >> 냉장고는 스와잎하면 삭제만 뜨고 나머지는 갱신도 뜸
        // 5한 이유는 타입카운트가 5이면 0 ~ 4 이니까 > 카테고리에서 기타까지의 getNum이 1~4
        return 5;
    }
    @Override
    public int getItemViewType(int position) {
        return mMyData.get(position).getCategory().getNum();
    }

    @Override
    public int getCount() {
        return mMyData.size();
    }

    @Override
    public MyData getItem(int position) {
        return mMyData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context.getApplicationContext(),
                    R.layout.item_list_app, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        final String item = getTitle(position);
        holder.tv_name.setText(item);

        return convertView;
    }

    public String getTitle(int position){
        return mMyData.get(position).getTitle();
    }

    public String getMemo(int position){
        return mMyData.get(position).getMemo();
    }

    public void removeItem(int position){
        DBUtil.getInstance().delete(mMyData.get(position).getTitle());
        mMyData.remove(position);
        dataRefresh();
        notifyDataSetChanged();
        serviceStart();
    }

    public void updateItem(int position){
        DBUtil.getInstance().update(mMyData.get(position).getTitle());
        mMyData.remove(position);
        dataRefresh();
        notifyDataSetChanged();
        serviceStart();
    }

    public void serviceStart(){
        Intent intent = new Intent(context, MyService.class);
        context.startService(intent);
    }

    class ViewHolder {
        TextView tv_name;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(this);
        }
    }

    public boolean getSwipeEnableByPosition(int position) {
        if(position % 2 == 0){
            return false;
        }
        return true;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}