package com.example.mate.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mate.Activity.Vo.FestivalVo;
import com.example.mate.R;

import java.util.ArrayList;

/**
 * Created by 가애 on 2018-01-25.
 */

public class FestivalAdapter extends RecyclerView.Adapter<FestivalAdapter.ItemViewHolder> {

    private ArrayList<FestivalVo> mItems;

    public FestivalAdapter(ArrayList<FestivalVo> mItems) {
        this.mItems = mItems;
    }

    @Override
    public FestivalAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_festival, parent, false);
        return new FestivalAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FestivalAdapter.ItemViewHolder holder, int position) {

        holder.mTitle.setText(mItems.get(position).getTitle());
        holder.mPeriod.setText(mItems.get(position).getPeriod());
        holder.mPlace.setText(mItems.get(position).getPlace());
        holder.mTime.setText(mItems.get(position).getTime());
//        holder.mBtnAdd.setText(mItems.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mPeriod;
        public TextView mPlace;
        public TextView mTime;
        public ImageView mBtnAdd;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.festival_title);
            mPeriod = (TextView) itemView.findViewById(R.id.festival_period);
            mPlace = (TextView) itemView.findViewById(R.id.festival_place);
            mTime = (TextView) itemView.findViewById(R.id.festival_time);
            mBtnAdd = (ImageView) itemView.findViewById(R.id.btn_add);
        }
    }
}