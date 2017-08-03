package com.example.sy.myapplication.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.example.sy.myapplication.R;
import com.example.sy.myapplication.Utils.Dialog.DRDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Osy on 2017-08-01.
 */
public class MyAdapter extends BaseSwipListAdapter {
    private List<String> MyData;
    private Context context;
    private DRDialog DRD;

    public MyAdapter(Context context, ArrayList< String > MyData){
        this.context = context;
        this.MyData = MyData;
        DRD = new DRDialog();
    }

    public void listRefresh(ArrayList< String > MyData){
        this.MyData = MyData;
    }

    @Override
    public int getCount() {
        return MyData.size();
    }

    @Override
    public String getItem(int position) {
        return MyData.get(position);
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
        final String item = getItem(position);
        holder.tv_name.setText(item);

        /*holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DRD.DRDialog( context , item, StatusSave.getInstance().getListView() );
            }
        });
        holder.tv_name.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });*/

        return convertView;
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