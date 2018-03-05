package com.example.mate.Activity.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mate.Activity.DiaryDetailActivity;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.Activity.Vo.FestivalVo;
import com.example.mate.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryPageAdapter extends RecyclerView.Adapter<DiaryPageAdapter.ItemViewHolder> {

    private Intent mIntent;
    private ArrayList<DiaryVo> mItems;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    public DiaryPageAdapter(ArrayList<DiaryVo> mItems) {
        this.mItems = mItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_diary_page, parent, false);

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("diary");

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        holder.mTitle.setText(mItems.get(position).getTitle());
        holder.mWriter.setText(mItems.get(position).getWriterId());
        holder.mDate.setText(mItems.get(position).getDate());

//        holder.itemView.setTag(mItems);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mWriter;
        private TextView mDate;

        public ItemViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            mDBRef.addChildEventListener(is);

            mTitle = (TextView) itemView.findViewById(R.id.diary_title);
            mWriter = (TextView) itemView.findViewById(R.id.writer);
            mDate = (TextView) itemView.findViewById(R.id.date);

        }

        @Override
        public void onClick(View v) {

            mIntent = new Intent(v.getContext(), DiaryDetailActivity.class);
            mIntent.putExtra("postingId", mItems.get(getAdapterPosition()).getPostingId());
            v.getContext().startActivity(mIntent);


//            ((Activity) v.getContext()).startActivityForResult(mIntent, 5);
//
//            onActivityResult(Const.REQUEST_REMOVE_DIARY, Const.RESULT_REMOVE_DIARY, mIntent);

        }

        ChildEventListener is = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                mItems.remove(getAdapterPosition());
//                notifyItemRemoved(getAdapterPosition());

//                remove();
            }


            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

//        public void remove(){
//            mItems.remove(getAdapterPosition());
//            notifyItemRemoved(getAdapterPosition());
//        }

//        public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//            if (resultCode == Activity.RESULT_OK) {
//                switch (requestCode) {
//
//                    case Const.REQUEST_REMOVE_DIARY:
//
//                        mItems.remove(getAdapterPosition());
//                        notifyItemRemoved(getAdapterPosition());
//
//                        break;
//                }
//            }
//
//        }

    }


}
