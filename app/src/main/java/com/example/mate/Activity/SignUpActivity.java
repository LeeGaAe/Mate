package com.example.mate.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-05.
 */

public class SignUpActivity extends Activity {

    private Context mContext;
    private Intent mIntent;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_cancel)
    LinearLayout mBtnCancel;

    @BindView(R.id.edit_email)
    EditText mEditEmail; //이메일

    @BindView(R.id.edit_pwd)
    EditText mEditPwd; //비밀번호

    @BindView(R.id.edit_pwd_confirm)
    EditText mConfirm; //비밀번호 확인

    @BindView(R.id.warning_no_same_pwd)
    TextView mWarning; // !비밀번호 = 비밀번호확인

    @BindView(R.id.txt_pwd_6)
    TextView textView; // 비밀번호 6자리 아닐때

    @BindView(R.id.nickname)
    EditText mNickname; //닉네임

    @BindView(R.id.gender)
    LinearLayout mGender; //성별

    @BindView(R.id.gender_boy)
    LinearLayout mGenderBoy; //성별

    @BindView(R.id.gender_girl)
    LinearLayout mGenderGirl; //성별

    @BindView(R.id.txt_boy)
    TextView mBoy; // 남자

    @BindView(R.id.txt_girl)
    TextView mGirl; // 여자

    @BindView(R.id.birth)
    Button mBirth; //생일

    @BindView(R.id.phone_num)
    EditText mPhoneNum; //핸드폰번호

    @BindView(R.id.btn_chk)
    ImageButton mBtnIDChk; //중복버튼

    @BindView(R.id.btn_complete_sign_up)
    ImageButton mBtnSignUp; //가입완료버튼

    @BindView(R.id.loading)
    ImageView mLoading;

    String GenderStr = "M";
    String FCMToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
        mContext = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();
        FCMToken = FirebaseInstanceId.getInstance().getToken();


        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });

        mBtnIDChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEditEmail.getText().toString())) {
                    Toast.makeText(mContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    mDBRef.child("user").addListenerForSingleValueEvent(mIdChkListener);
                }
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        //비밀번호 = 비밀번호확인
        mConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEditPwd.getText().toString().equals(mConfirm.getText().toString())) {
                    mWarning.setVisibility(View.VISIBLE);
                } else {
                    mWarning.setVisibility(View.GONE);
                }
            }
        });


        mGenderBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenderBoy.setBackground(getResources().getDrawable(R.drawable.gender_border_press));
                mBoy.setTextColor(Color.WHITE);

                mGenderGirl.setBackground(getResources().getDrawable(R.drawable.gender_border));
                mGirl.setTextColor(Color.BLACK);

                GenderStr = "M";
            }
        });

        mGenderGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenderGirl.setBackground(getResources().getDrawable(R.drawable.gender_border_press));
                mGirl.setTextColor(Color.WHITE);

                mGenderBoy.setBackground(getResources().getDrawable(R.drawable.gender_border));
                mBoy.setTextColor(Color.BLACK);

                GenderStr = "F";
            }
        });

    }

    ValueEventListener mIdChkListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                SignUpVo vo = snapshot.getValue(SignUpVo.class);

                if (mEditEmail.getText().toString().equals(vo.getEmail())) {
                    Toast.makeText(mContext, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(mContext, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    //입력사항 기록하기
    private void registerUser() {

        final String email = mEditEmail.getText().toString().trim();
        String pwd = mEditPwd.getText().toString().trim();
        String nickname = mNickname.getText().toString().trim();
        String gender = GenderStr;
        String birth = mBirth.getText().toString().trim();
        String phone_num = mPhoneNum.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(mContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(mContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(mContext, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (gender == null) {
            Toast.makeText(mContext, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(birth)) {
            Toast.makeText(mContext, "생일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone_num)) {
            Toast.makeText(mContext, "핸드폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Glide.with(mContext).load(R.raw.loading).into(mLoading);

        mAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    SignUpVo signUpVo = new SignUpVo();

                    signUpVo.setUid(task.getResult().getUser().getUid());
                    signUpVo.setEmail(mEditEmail.getText().toString());
                    signUpVo.setPassword(mConfirm.getText().toString());
                    signUpVo.setNickname(mNickname.getText().toString());
                    signUpVo.setGender(GenderStr);
                    signUpVo.setBirth(mBirth.getText().toString());
                    signUpVo.setPhone_num(mPhoneNum.getText().toString());
                    signUpVo.setFcmToken(FCMToken);

                    mDBRef.child("user").child(task.getResult().getUser().getUid()).setValue(signUpVo);

                    Toast.makeText(mContext, "회원가입 완료", Toast.LENGTH_SHORT).show();

                    finish();

                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Glide.with(mContext).clear(mLoading);

                    Toast.makeText(mContext, "정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        int mYear = cal.get(Calendar.YEAR);
        int mMonth = cal.get(Calendar.MONTH);
        int mDay = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBirth.setText(String.format("%d. %d. %d.", year, (monthOfYear + 1), dayOfMonth));

            }
        }, mYear, mMonth, mDay);

        dialog.show();
    }
}
