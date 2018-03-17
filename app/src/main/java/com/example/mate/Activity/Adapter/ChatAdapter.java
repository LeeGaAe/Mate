package com.example.mate.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.ChatVo;
import com.example.mate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2017-12-28.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());
    private ArrayList<ChatVo> mItems;
    String stEmail;

    public ChatAdapter(ArrayList<ChatVo> mItems, String email) {
        this.mItems = mItems;
        this.stEmail = email;
    }

    @Override
    public int getItemViewType(int position) {

        if (mItems.get(position).getEmail().equals(stEmail)){
            return 1;
        } else {
            return 2;
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_chat_me, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_chat_you, parent, false);
        }
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        holder.mMessage.setText(mItems.get(position).getMessage());
        holder.mTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessage;
        private TextView mTime;
        private CircleImageView mMyPic;

        public ItemViewHolder(View view) {
            super(view);

            mMessage = (TextView) itemView.findViewById(R.id.message);
            mTime = (TextView) itemView.findViewById(R.id.time);
//            mMyPic = (CircleImageView) itemView.findViewById(R.id.my_pic);

        }
    }

}
