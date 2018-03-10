package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-22.
 */

public class MyInfoActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_check)
    LinearLayout mBtnCheck;

    @BindView(R.id.my_name)
    TextView mMyName;

    @BindView(R.id.my_email)
    TextView mMyEmail;

    @BindView(R.id.edit_name)
    EditText mEditName;

    @BindView(R.id.birth)
    TextView mMyBirth;

    @BindView(R.id.gender)
    TextView mMyGender;

    @BindView(R.id.btn_edit_name)
    ImageView mBtnEditName;

    @BindView(R.id.btn_complete)
    ImageView mBtnComplete;

    @BindView(R.id.btn_disconnect)
    LinearLayout mBtnDisconnect;


    String json;
    SignUpVo java;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference("user");

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);


        init();

        /**
         * 한명이 연결을 끊으면 상대방은 앱 실행 하자마자 LoginActivity로 이동.
         */
//        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mIntent = new Intent(mContext,LoginActivity.class);
//                startActivity(mIntent);
//                finish();
//
//                Toast.makeText(mContext,"상대방과 연결이 끊겼습니다.",Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    private void init() {

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });


        mMyName.setText(java.getNickname());
        mMyEmail.setText(java.getEmail());

        mMyName.setVisibility(View.VISIBLE);
        mEditName.setVisibility(View.GONE);

        mBtnEditName.setVisibility(View.VISIBLE);
        mBtnComplete.setVisibility(View.GONE);

        mMyBirth.setText(java.getBirth());

        if (java.getGender().equals("M")) {
            mMyGender.setText("남자");
        } else {
            mMyGender.setText("여자");
        }


        mBtnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyName.setVisibility(View.GONE);
                mEditName.setVisibility(View.VISIBLE);

                mBtnEditName.setVisibility(View.GONE);
                mBtnComplete.setVisibility(View.VISIBLE);

            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String me = user.getUid();

        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyName.setText(mEditName.getText().toString());

                mDBRef.child(me).child("nickname").setValue(mMyName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                java.setNickname(mMyName.getText().toString());

                                json = new Gson().toJson(java);
                                PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);
                                Log.d("lga", "json" + json);
                            }
                        });

                mMyName.setVisibility(View.VISIBLE);
                mEditName.setVisibility(View.GONE);

                mBtnEditName.setVisibility(View.VISIBLE);
                mBtnComplete.setVisibility(View.GONE);


            }
        });

    }
}
