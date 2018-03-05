package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Util.PreferenceUtil;

/**
 * Created by 가애 on 2017-10-23.
 */

public class IntroActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser(); //로그인이 되어있는지 아닌지(자동로그인)
        mDBRef = mDatabase.getReference().child("user");

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            public void run() {

                if (mUser == null) { //로그인을 안했을 때
//                    mIntent = new Intent(mContext, LoginActivity.class);
                    mIntent = new Intent(mContext, FragmentMain.class);
                    startActivity(mIntent);
                    finish();
                }
//                else { //했을 때
//                    mDBRef.addValueEventListener(isPartner);
//                }
            }
        }, 2000);
    }

//    //파트너 연결 유무확인
//    ValueEventListener isPartner = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//
//            SignUpVo vo = dataSnapshot.getValue(SignUpVo.class);
//
//            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//
//            if (java.getGroupID()==null) {
//                mIntent = new Intent(mContext, ConnectPartnerActivity.class);
//                startActivity(mIntent);
//                finish();
//            } else {
//                Toast.makeText(mContext, "메인으로 가기", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//        }
//    };
}
