package com.example.mate.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mate.Activity.Vo.PwdHolder;
import com.example.mate.R;

import java.util.ArrayList;

import Util.Const;

import static android.media.CamcorderProfile.get;
import static com.example.mate.R.color.DeepPink;

/**
 * Created by 가애 on 2017-12-09.
 */

public class PwdAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Integer> list;

    LayoutInflater inflater;

    public PwdAdapter(Context mContext, ArrayList<Integer> list) {
        this.mContext = mContext;
        this.list = list;

        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        PwdHolder holder = null;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.row_pwd, null);
            holder = new PwdHolder();

            holder.Num_Pwd = (TextView) convertView.findViewById(R.id.num_pwd);
            holder.Img_Pwd = (ImageView) convertView.findViewById(R.id.img_pwd);

            convertView.setTag(holder);

        } else {

            holder = (PwdHolder) convertView.getTag();

        }

        if (position == 9 || position == 11) {
            holder.Num_Pwd.setVisibility(View.GONE);
            holder.Img_Pwd.setVisibility(View.VISIBLE);

            holder.Img_Pwd.setImageResource(Const.APP_PW_SETTING[position]);

        } else{
            holder.Num_Pwd.setVisibility(View.VISIBLE);
            holder.Img_Pwd.setVisibility(View.GONE);

            holder.Num_Pwd.setText(Integer.toString(list.get(position)));

        }


        return convertView;
    }
}
