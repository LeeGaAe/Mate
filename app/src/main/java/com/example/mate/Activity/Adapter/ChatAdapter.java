package com.example.mate.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.ChatVo;
import com.example.mate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2017-12-28.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {

    private Context mContext;
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("a h:mm", Locale.getDefault());
    private ArrayList<ChatVo> mItems;
    String stEmail;
    String Profile;


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDBRef = mDatabase.getReference().child("user");
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public ChatAdapter(Context context, ArrayList<ChatVo> mItems, String email) {
        this.mContext = context;
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

        initFireBase();

        holder.mMessage.setText(mItems.get(position).getMessage());
        holder.mTime.setText(mSimpleDateFormat.format(mItems.get(position).getTime()));

        if (mItems.get(position).getEmail().equals(stEmail)) {

            if (mItems.get(position).getUserPhotoUrl().equals("")) {
                Glide.with(mContext).load(R.mipmap.ic_launcher_ban).into(holder.mMyPic);
            } else {
                Glide.with(mContext).load(mItems.get(position).getUserPhotoUrl()).into(holder.mMyPic);
            }

        } else {

            if (mItems.get(position).getUserPhotoUrl().equals("")) {
                Glide.with(mContext).load(R.mipmap.ic_launcher_ban).into(holder.mPartnerPic);
            } else {
                Glide.with(mContext).load(mItems.get(position).getUserPhotoUrl()).into(holder.mPartnerPic);
            }

        }

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessage;
        private TextView mTime;
        private CircleImageView mMyPic;
        private CircleImageView mPartnerPic;

        public ItemViewHolder(View view) {
            super(view);

            mMessage = (TextView) itemView.findViewById(R.id.message);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mMyPic = (CircleImageView) itemView.findViewById(R.id.my_pic);
            mPartnerPic = (CircleImageView) itemView.findViewById(R.id.partner_pic);

        }
    }


    private void initFireBase() {

        DatabaseReference profile = mDBRef.child("user").child(mUser.getUid()).child("profile");
        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Profile = "";
                } else {
                    Profile = dataSnapshot.getValue().toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
