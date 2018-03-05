package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

    @BindView(R.id.part_phone)
    EditText mEditPartPhone;

    @BindView(R.id.btn_connect)
    ImageView mBtnConnect;

    String groupID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_partner);

        ButterKnife.bind(this);

        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();

        groupID = mDBRef.push().getKey();

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

                if (mEditPartPhone.getText().toString().equals(java.getPhone_num())) { // java<- 내정보 shared에 저장
                    Toast.makeText(mContext, "번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show(); //내 번호와 같다면
                } else {
                    mDBRef.child("user").addListenerForSingleValueEvent(mCreateGroupID);
                }
            }
        });
    }

    ValueEventListener mCreateGroupID = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                SignUpVo vo = snapshot.getValue(SignUpVo.class);

                if (mEditPartPhone.getText().toString().equals(vo.getPhone_num())) { // 상대방 번호찾기

                    mDBRef.child("user").addValueEventListener(mFindGroupID); //대기하고 있는지 확인.

                    return;
                }
            }
            Toast.makeText(mContext, "실패", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    ValueEventListener mFindGroupID = new ValueEventListener() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String me = user.getUid();

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                SignUpVo vo = snapshot.getValue(SignUpVo.class);

                if (mEditPartPhone.getText().toString().equals(vo.getPhone_num())) { // 상대방 번호찾기

                    if (vo.getGroupID() == null) {

                        PartnerVo partnerVo = new PartnerVo();

                        partnerVo.setPart_email(vo.getEmail());
                        partnerVo.setPart_phone_num(vo.getPhone_num());
                        partnerVo.setPart_birth(vo.getBirth());
                        partnerVo.setPart_name(vo.getNickname());

                        java.setGroupID(groupID);
                        java.setPartnerVo(partnerVo);

                        mDBRef.child("wait").child(java.getGroupID()).child(java.getPhone_num()).setValue(java);
                        mDBRef.child("user").child(me).setValue(java);

                        mIntent = new Intent(mContext, ConnectWaitActivity.class);
                        mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
                        startActivity(mIntent);
                        finish();

                        break;

                    }

                    else {

                        PartnerVo partnerVo = new PartnerVo();

                        partnerVo.setPart_email(vo.getEmail());
                        partnerVo.setPart_phone_num(vo.getPhone_num());
                        partnerVo.setPart_birth(vo.getBirth());
                        partnerVo.setPart_name(vo.getNickname());

                        java.setGroupID(vo.getGroupID());
                        java.setPartnerVo(partnerVo);

                        mDBRef.child("wait").child(vo.getGroupID()).child(java.getPhone_num()).setValue(java);
                        mDBRef.child("user").child(me).setValue(java);
                        Toast.makeText(mContext, "그룹아이디 가져오기.", Toast.LENGTH_SHORT).show();

                        mIntent = new Intent(mContext, ConnectWaitActivity.class);
                        mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
                        startActivity(mIntent);
                        finish();


                        return;

                    }

                }

            }

//            mIntent = new Intent(mContext, ConnectWaitActivity.class);
//            mIntent.putExtra("partnerNum", mEditPartPhone.getText().toString());
//            startActivity(mIntent);
//            finish();


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
}