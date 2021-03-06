package com.example.mate.Activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.DiaryDetailActivity;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryPageAdapter extends RecyclerView.Adapter<DiaryPageAdapter.ItemViewHolder> {

    private Context mContext;
    private Intent mIntent;

//    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference mDBRef = mDatabase.getReference().child("user");
//    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private ArrayList<DiaryVo> mItems = new ArrayList<>();

    public DiaryPageAdapter(Context context) {

        mContext = context;

    }

    public void setData(ArrayList<DiaryVo> diary){
        mItems = diary;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_diary_page, parent, false);

        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        holder.mTitle.setText(mItems.get(position).getTitle());
        holder.mWriter.setText(mItems.get(position).getWriterId());
        holder.mDate.setText(mItems.get(position).getDate());

        if (mItems.get(position).getWriterProfileUri().equals("")) {
            Glide.with(mContext).load(R.mipmap.ic_launcher_ban).into(holder.mMyPic);
        } else {
            Glide.with(mContext).load(mItems.get(position).getWriterProfileUri()).into(holder.mMyPic);
        }

        if (mItems.get(position).getPhotoUri().equals("")) {

            holder.mNotImage.setVisibility(View.VISIBLE);
            holder.mImage.setVisibility(View.GONE);

        } else {
            holder.mNotImage.setVisibility(View.GONE);
            holder.mImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mWriter;
        private TextView mDate;
        private CircleImageView mMyPic;

        private ImageView mNotImage;
        private ImageView mImage;


        public ItemViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            mTitle = (TextView) itemView.findViewById(R.id.diary_title);
            mWriter = (TextView) itemView.findViewById(R.id.writer);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mMyPic = (CircleImageView) itemView.findViewById(R.id.my_pic);
            mNotImage = (ImageView) itemView.findViewById(R.id.isNotImage);
            mImage = (ImageView) itemView.findViewById(R.id.isImage);

        }

        @Override
        public void onClick(View v) {

            mIntent = new Intent(v.getContext(), DiaryDetailActivity.class);
            mIntent.putExtra("postingId", mItems.get(getAdapterPosition()).getPostingId());
            v.getContext().startActivity(mIntent);

        }
    }

}
