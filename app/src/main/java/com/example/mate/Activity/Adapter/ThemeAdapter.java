package com.example.mate.Activity.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.example.mate.Activity.Vo.ThemeHolder;
import com.example.mate.R;

import java.util.ArrayList;

/**
 * Created by 가애 on 2017-12-11.
 */

public class ThemeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> list;

    LayoutInflater inflater;

    public ThemeAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ThemeHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_backcolor, null);
            holder = new ThemeHolder();
            holder.Bg_Color = (LinearLayout) convertView.findViewById(R.id.bg_color);
            convertView.setTag(holder);
        } else {
            holder = (ThemeHolder) convertView.getTag();
        }

        holder.Bg_Color.setBackgroundColor(Color.parseColor(list.get(position)));
        return convertView;

    }
}


