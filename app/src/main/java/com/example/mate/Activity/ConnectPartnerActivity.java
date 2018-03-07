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

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;
    @BindView(R.id.layout_connect)
    LinearLayout mLayConnect;
    @BindView(R.id.layout_wait)
    LinearLayout mLayWait;
    @BindView(R.id.part_phone)
    EditText mEditPartPhone;
    @BindView(R.id.btn_connect)
    ImageView mBtnConnect;

    @BindView(R.id.loading_connect)
    ImageView mLoadingConnect;

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

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditPartPhone.getText().toString().equals(java.getPhone_num())) {

                    Toast.makeText(mContext, "번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show(); //내 번호와 같다면

                } else {

                    search();

                }
            }
        });

        init();


    }

    Timer timer;

    private void init() {

        if (java.getGroupID() == null) {

            mLayConnect.setVisibility(View.VISIBLE);
            mLayWait.setVisibility(View.GONE);

        } else {
            mLayWait.setVisibility(View.VISIBLE);
            mLayConnect.setVisibility(View.GONE);

//             1초 뒤에 10초에 한번씩 task 돌림
            timer = new Timer();
            timer.scheduleAtFixedRate(task, 1000, 10000);

        }

    }


    TimerTask task = new TimerTask() {

        public void run() {

            try {

                check();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    };


    private void search() {

        mDBRef.child("wait").child(mEditPartPhone.getText().toString()).addListenerForSingleValueEvent(
                new ValueEventListener() {
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

    private void addGroupId() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String me = user.getUid();

        final String groupId = mDBRef.push().getKey();

        mDBRef.child("wait").child(java.getPhone_num()).child("groupId").setValue(groupId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        java.setGroupID(groupId);

                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);

                        mDBRef.child("user").child(me).child("groupId").setValue(groupId);

                        mLayConnect.setVisibility(View.GONE);
                        mLayWait.setVisibility(View.VISIBLE);

                        timer = new Timer();
                        timer.scheduleAtFixedRate(task, 1000, 10000);

                    }
                });


    }

    private void connect(final String gid) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String me = user.getUid();

        mDBRef.child("user").child(me).child("groupId").setValue(gid)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        java.setGroupID(gid);

                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);

                        mLayConnect.setVisibility(View.GONE);
                        mLayWait.setVisibility(View.VISIBLE);

                        timer = new Timer();
                        timer.scheduleAtFixedRate(task, 1000, 10000);

                        mDBRef.child("wait").child(mEditPartPhone.getText().toString()).setValue(null);

                    }

                });

    }

    private void check() {

        DatabaseReference check = mDBRef.child("user");

        check.orderByChild("groupId").equalTo(java.getGroupID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() == 2) {

                    task.cancel();
                    timer.cancel();

                    mIntent = new Intent(mContext,FragmentMain.class);
                    startActivity(mIntent);
                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}