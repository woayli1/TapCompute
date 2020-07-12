package com.tapcompute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class RefreshItem extends BaseAdapter {
    private Context context;
    private ArrayList<String> items,items2,items3;

    TextView textView;  //用于接收项目名称
    TextView textView2; //用于接收攻击力
    TextView textView3; //用于接收项目花费
    TextView textView4; //用于接收项目性价比

    RefreshItem(Context context){
        this.context=context;
        items=MainActivity.items_name;
        items2=MainActivity.items_name2;
        items3=MainActivity.items_name3;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.content_view, null);
        textView= view.findViewById(R.id.textView);
        textView2= view.findViewById(R.id.textView2);
        textView3= view.findViewById(R.id.textView3);
        textView4= view.findViewById(R.id.textView4);

        String items_name= items.get(position);
        String items_name2= items2.get(position);
        String items_name3= items3.get(position);
        textView.setText(items_name);
        textView2.setText(items_name2);
        textView3.setText(items_name3);

        float a=Float.parseFloat(items_name2);
        float b=Float.parseFloat(items_name3);
        String c=Float.toString(a/b);
        textView4.setText(c);

        return view;
    }
}
