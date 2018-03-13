package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.GsonBuilder;

import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-05.
 */

public class LoginActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @BindView(R.id.btn_login)
    ImageButton mBtnLogin;

    @BindView(R.id.btn_sign_up)
    ImageButton mBtnSignUp;

    @BindView(R.id.btn_search_info)
    ImageButton mBtnSearchInfo;

    @BindView(R.id.edit_email)
    EditText mEditEmail;

    @BindView(R.id.edit_pwd)
    EditText mEditPwd;

    @BindView(R.id.loading)
    ImageView mLoading;

    String refreshToken;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("user");
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        refreshToken = FirebaseInstanceId.getInstance().getToken();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mEditEmail.getText().toString()) || TextUtils.isEmpty(mEditPwd.getText().toString())) {
                    Toast.makeText(mContext, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Glide.with(mContext).load(R.raw.loading).into(mLoading);

                    mAuth.signInWithEmailAndPassword(mEditEmail.getText().toString(), mEditPwd.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        mDBRef.child(task.getResult().getUser().getUid()).addListenerForSingleValueEvent(isPartner);

                                    } else {
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Glide.with(mContext).clear(mLoading);

                                        Toast.makeText(mContext, "실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext, SignUpOneActivity.class);
                startActivity(mIntent);
            }
        });

        mBtnSearchInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext, SearchInfoActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }

    //파트너 연결 유무확인
    ValueEventListener isPartner = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            SignUpVo vo = dataSnapshot.getValue(SignUpVo.class);

            //내정보 json형식으로 입력 ( why : 파트너연결하기에서 본인의 번호를 적지 않기 위해서 )
            String json = new GsonBuilder().serializeNulls().create().toJson(vo, SignUpVo.class);
            PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);

            if (vo.getPartnerVo()==null) {
                mIntent = new Intent(mContext, ConnectPartnerActivity.class);
                startActivity(mIntent);
                finish();

            } else {

                mDBRef.child(mUser.getUid()).child("fcmToken").setValue(refreshToken);

                mIntent = new Intent(mContext, FragmentMain.class);
                startActivity(mIntent);
                finish();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

}

