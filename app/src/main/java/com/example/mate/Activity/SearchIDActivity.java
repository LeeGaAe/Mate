package com.example.mate.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class SearchIDActivity extends Activity {

    private Context mContext;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;
    @BindView(R.id.btn_search_id)
    ImageButton mBtnId;
    @BindView(R.id.find_user_id)
    LinearLayout mAreaUserId;
    @BindView(R.id.txt_find_user_id)
    TextView mTxtUserId;
    @BindView(R.id.nickname)
    EditText mEditName;
    @BindView(R.id.phone)
    EditText mEditPhone;
    @BindView(R.id.btn_birth)
    Button mBtnBirth;

    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);
        ButterKnife.bind(this);
        mContext = this;

        init();

    }


    private void init() {

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        mBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initFireBase();

            }
        });


    }

    private void initFireBase() {

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();

        mDBRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SignUpVo vo = snapshot.getValue(SignUpVo.class);

                    if (mEditName.getText().toString().equals(vo.getNickname())
                            && mEditPhone.getText().toString().equals(vo.getPhone_num())
                            && mBtnBirth.getText().toString().equals(vo.getBirth())) {

                        email = vo.getEmail();
                        mTxtUserId.setText(email);


                        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                        mAreaUserId.setVisibility(View.VISIBLE);

                        return;

                    }

                }

                mAreaUserId.setVisibility(View.GONE);
                Toast.makeText(mContext, "정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                mBtnBirth.setText(String.format("%d. %d. %d.", year, (monthOfYear + 1), dayOfMonth));

            }
        }, mYear, mMonth, mDay);

        dialog.show();
    }
}
