package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.PartnerVo;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class ConnectPartnerActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_back) LinearLayout mBtnBack;
    @BindView(R.id.layout_connect) LinearLayout mLayConnect;
    @BindView(R.id.layout_wait) LinearLayout mLayWait;
    @BindView(R.id.part_phone) EditText mEditPartPhone;
    @BindView(R.id.btn_connect) ImageView mBtnConnect;

    @BindView(R.id.loading_connect) ImageView mLoadingConnect;
//    String groupID;

    String json;
    SignUpVo java;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_partner);
        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();
        Glide.with(this).load(R.raw.loading_connect).into(mLoadingConnect);

        init();

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);



//        groupID = mDBRef.push().getKey();

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
                SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

                if (mEditPartPhone.getText().toString().equals(java.getPhone_num())) {

                    Toast.makeText(mContext, "번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show(); //내 번호와 같다면

                } else {

                    search();

                }
            }
        });

    }

    Timer timer;
    private void init(){

        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

        if (java.getGroupID() == null) {

            mLayConnect.setVisibility(View.VISIBLE);
            mLayWait.setVisibility(View.GONE);

        } else {
            mLayWait.setVisibility(View.VISIBLE);
            mLayConnect.setVisibility(View.GONE);

            // 1초 뒤에 10초에 한번씩 task 돌림
            timer = new Timer();
            timer.scheduleAtFixedRate(task, 1000, 10000);

        }

    }



    TimerTask task = new TimerTask(){

        public void run() {

            try {

                check();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    };


    private void search(){

//        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//        SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

        mDBRef.child("wait").child(mEditPartPhone.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 0) {

                    addGroupId();

                } else {

                    String gid = dataSnapshot.child("groupId").getValue().toString();
                    connect(gid);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addGroupId(){

//        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//        final SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

        final String groupId = mDBRef.push().getKey();

        mDBRef.child("wait").child(java.getPhone_num()).child("groupId").setValue(groupId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        java.setGroupID(groupId);

                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);
                        Log.d("lga",json);

                        mLayConnect.setVisibility(View.GONE);
                        mLayWait.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void connect(final String gid){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String me = user.getUid();

        mDBRef.child("user").child(me).child("groupId").setValue(gid)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        java.setGroupID(gid);

                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);
                        Log.d("lga",json);

                        mLayConnect.setVisibility(View.GONE);
                        mLayWait.setVisibility(View.VISIBLE);

                    }
                });
    }

    private void check(){

        mDBRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int cnt;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignUpVo vo = snapshot.getValue(SignUpVo.class);


                }


//                if(dataSnapshot.getChildrenCount()==2){
//
//                    Toast.makeText(mContext, "FragmentHome 으로 이동", Toast.LENGTH_SHORT).show();
////                    mIntent = new Intent(mContext,FragmentHome.class);
////                    startActivity(mIntent);
////                    finish();
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



//    ValueEventListener mCreateGroupID = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                SignUpVo vo = snapshot.getValue(SignUpVo.class);
//
//                if (mEditPartPhone.getText().toString().equals(vo.getPhone_num())) { // 상대방 번호찾기
//
////                    mDBRef.child("user").addValueEventListener(mFindGroupID); //대기하고 있는지 확인.
//
//                    //대기하고 있는지 확인.
//                    mDBRef.child("wait").child(mEditPartPhone.getText().toString()).addListenerForSingleValueEvent(mFindGroupID);
//
//                    return;
//                }
//            }
//            Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//
//    };
//
//    ValueEventListener mFindGroupID = new ValueEventListener() {
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String me = user.getUid();
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//            if (dataSnapshot.getChildrenCount() == 0) {
//
//                java.setGroupID(groupID);
//
//                mIntent = new Intent(mContext, ConnectWaitActivity.class);
//                startActivity(mIntent);
//
//                mDBRef.child("wait").child(java.getPhone_num()).child(java.getGroupID()).child(java.getNickname()).setValue(java);
//
//            } else {
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    SignUpVo vo = snapshot.getValue(SignUpVo.class);
//
//                    if (vo.getPhone_num().equals(mEditPartPhone.getText().toString())) {
//
//                        Log.d("lga",vo.getGroupID());
//                        java.setGroupID(vo.getGroupID());
//
//                    }
//
//                }
//
//                mDBRef.child("wait").child(mEditPartPhone.getText().toString())
//                        .child(java.getGroupID()).setValue(java.getEmail());
//
//                Toast.makeText(mContext, "메인으로갑니다.", Toast.LENGTH_SHORT).show();
//
//            }
//
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//
//        }
//
//    };
//
//    private void check(){
//
//        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//        SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//
//        DatabaseReference doc = mDBRef.child("wait").child(java.getPhone_num()).child(java.getGroupID());
//        Log.d("lga", "doc" + doc);
//
//
//
//
//    }
}


//
//    ValueEventListener mFindGroupID = new ValueEventListener() {
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String me = user.getUid();
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//
//            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                SignUpVo vo = snapshot.getValue(SignUpVo.class);
//
//                if (mEditPartPhone.getText().toString().equals(vo.getPhone_num())) { // 상대방 번호찾기
//
//                    if (vo.getGroupID() == null) { // groupID 가 없을때
//
//                        java.setGroupID(groupID);
//
//                        mDBRef.child("wait").child(java.getGroupID()).child(java.getPhone_num()).setValue(java);
//                        mDBRef.child("user").child(me).setValue(java);
//
//
//                        mIntent = new Intent(mContext, ConnectWaitActivity.class);
//                        mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
//                        startActivity(mIntent);
//                        finish();
//
//                        return;
//
//                    } else {
//
//                        java.setGroupID(vo.getGroupID());
//
//                        mDBRef.child("wait").child(vo.getGroupID()).child(java.getPhone_num()).setValue(java);
//                        mDBRef.child("user").child(me).setValue(java);
//
//                        Toast.makeText(mContext, "그룹아이디 가져오기.", Toast.LENGTH_SHORT).show();
//
//
//                    }
//
//                }
//
//            }
//
//            mIntent = new Intent(mContext, ConnectWaitActivity.class);
//            mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
//            startActivity(mIntent);
//            finish();
//
//
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//
//        }
//
//    };

//        /    ValueEventListener mFindGroupID = new ValueEventListener() {
//
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        String me = user.getUid();
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//            if (dataSnapshot.getChildrenCount() == 0) {
//
//                java.setGroupID(groupID);
//
//                mDBRef.child("wait").child(java.getPhone_num()).child(groupID).setValue(java);
////                mDBRef.child("user").child(me).setValue(java);
//
//                mIntent = new Intent(mContext, ConnectWaitActivity.class);
////                mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
//                startActivity(mIntent);
////                finish();
//
//            } else {
////
////                String gid = dataSnapshot.child(groupID).getValue().toString();
//
//                String groupId = dataSnapshot.child(groupID).getValue().toString();
//                java.setGroupID(groupId);
//
//                mDBRef.child("wait").child(mEditPartPhone.getText().toString()).child(groupID).setValue(java);
//
////                mDBRef.child("wait").child(mEditPartPhone.getText().toString()).setValue(java);
////                mDBRef.child("couple").child(gid).setValue(java);
//
////                mDBRef.child("wait").child(java.getPhone_num()).setValue(java);
////
//                mIntent = new Intent(mContext, FragmentMain.class);
//                startActivity(mIntent);
//                finish();
//
//                Toast.makeText(mContext, "연결", Toast.LENGTH_SHORT).show();
//
//            }
//