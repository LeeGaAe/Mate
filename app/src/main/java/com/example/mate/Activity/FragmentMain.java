package com.example.mate.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.mate.Activity.Vo.PartnerVo;
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

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-03-01.
 */

//하단바 고정
public class FragmentMain extends FragmentActivity {

    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.bottom_area) LinearLayout mBottomArea;
    @BindView(R.id.btn_home) LinearLayout mBtnHome;
    @BindView(R.id.btn_map) LinearLayout mBtnMap;
    @BindView(R.id.btn_chat) LinearLayout mBtnChat;
    @BindView(R.id.btn_more) LinearLayout mBtnMore;

    String json;
    SignUpVo java;



    @Override
    public void onBackPressed() {
        setResult(Const.RESULT_SETTING_THEME);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("user");

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        connectPartnerVo();

    }

    private void connectPartnerVo() {

        if (java.partnerVo == null) {

            partner();


        } else {

            changeView();
        }

    }

    private void partner() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String me = user.getUid();

        mDBRef.orderByChild("groupId").equalTo(java.getGroupID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignUpVo vo = snapshot.getValue(SignUpVo.class);

                    if (!java.getEmail().equals(vo.getEmail())) {
                        PartnerVo partnerVo = new PartnerVo();

                        partnerVo.setPart_email(vo.getEmail());
                        partnerVo.setPart_name(vo.getNickname());
                        partnerVo.setPart_phone_num(vo.getPhone_num());
                        partnerVo.setPart_birth(vo.getBirth());
                        partnerVo.setPart_fcmToken(vo.getFcmToken());
                        partnerVo.setPart_uid(vo.getUid());

                        java.setPartnerVo(partnerVo);
                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);

                        mDBRef.child(me).child("partnerVo").setValue(partnerVo);

                        // 초기화면
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_place, new FragmentHome())
                                .commit();

                        changeView();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void changeView() {


        FrameLayout frame = (FrameLayout) findViewById(R.id.fragment_place);

        if (frame.getChildCount() > 0) { // FrameLayout에서 뷰 삭제.
            frame.removeViewAt(0);
        }

        // 초기화면
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_place, new FragmentHome())
                .commit();


        View view = null;


        mBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentHome())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentFestival())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(mIntent);
            }
        });

        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentSetting())
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (view != null) {
            frame.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Const.REQUEST_THEME_SET:

                String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
                mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));

                break;
        }
    }
}
