package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class SearchPwdActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_back) LinearLayout mBtnBack;
    @BindView(R.id.chan_user_pwd) LinearLayout mAreaUserPwd;

    @BindView(R.id.btn_search_pwd) ImageButton mBtnSearchPwd;
    @BindView(R.id.btn_chan_pwd) ImageButton mBtnChanPwd;

    @BindView(R.id.edit_email) EditText mEditEmail;
    @BindView(R.id.edit_name) EditText mEditName;
    @BindView(R.id.edit_phone) EditText mEditPhone;
    @BindView(R.id.edit_password) EditText mEditPassword;
    @BindView(R.id.edit_confirm) EditText mEditConfirm;


    private String mUid;
    private String pwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pwd);
        ButterKnife.bind(this);

        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();

        mAreaUserPwd.setVisibility(View.GONE);

        init();


    }



    private void init(){

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initFireBase();

            }
        });

        mBtnChanPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setPassword();

                mIntent = new Intent(mContext, LoginActivity.class);
                startActivity(mIntent);
                finish();

            }
        });

    }




    private void initFireBase() {

        mDBRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignUpVo vo = snapshot.getValue(SignUpVo.class);

                    if (mEditName.getText().toString().equals(vo.getNickname())
                            && mEditPhone.getText().toString().equals(vo.getPhone_num())
                            && mEditEmail.getText().toString().equals(vo.getEmail())) {

                        mUid = vo.getUid();
                        mAreaUserPwd.setVisibility(View.VISIBLE);

                        return;
                    }
                }

                mAreaUserPwd.setVisibility(View.GONE);
                Toast.makeText(mContext, "정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setPassword() {


        if (mEditPassword.getText().toString().equals(mEditConfirm.getText().toString())) {

            mDBRef.child("user").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {



                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SignUpVo vo = snapshot.getValue(SignUpVo.class);

                        if (mEditEmail.getText().toString().equals(vo.getEmail())) {

                            mUid = vo.getUid();
                            pwd = mEditConfirm.getText().toString();

                            mDBRef.child("user").child(mUid).child("password").setValue(pwd);

                            return;

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//
//            mIntent = new Intent( mContext, LoginActivity.class );
//            startActivity(mIntent);
//            finish();
//
//            Toast.makeText(mContext, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(mContext, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
        }


    }



}
