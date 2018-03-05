package com.example.mate.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Calendar;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryModifyActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

//    private Uri mImageUri;
//
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_cancel)
    LinearLayout mBtnCancel;

    @BindView(R.id.btn_check)
    LinearLayout mBtnChk;

    @BindView(R.id.btn_date_dialog)
    LinearLayout mBtnDateDialog;

    @BindView(R.id.edit_title)
    EditText mEditTitle;

    @BindView(R.id.edit_content)
    EditText mEditContent;

    @BindView(R.id.txt_diary_date)
    TextView mDiaryDate;

//    @BindView(R.id.img)
//    ImageView mImage;

    @BindView(R.id.postingId)
    TextView mPostingId;

//    @BindView(R.id.ll)
//    LinearLayout mll;

    private int mYear, mMonth, mDay;
    private String ThemeColor;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_diary);

        ButterKnife.bind(this);
        mContext = this;

        mIntent = getIntent();

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("diary");

        mPostingId.setText(mIntent.getStringExtra("postingId"));

        ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        mBtnChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String postingId = mPostingId.getText().toString();

                String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
                SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

                DiaryVo vo = new DiaryVo();
                vo.setPostingId(postingId);
                vo.setTitle(mEditTitle.getText().toString());
                vo.setContent(mEditContent.getText().toString());
                vo.setDate(mDiaryDate.getText().toString());
                vo.setWriterId(java.getNickname());

                mDBRef.child(postingId).setValue(vo);
                Toast.makeText(mContext, "완료되었습니다.", Toast.LENGTH_SHORT).show();

                onBackPressed();
            }
        });

        mDBRef.addChildEventListener(diary);
    }

    ChildEventListener diary = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            DiaryVo vo = dataSnapshot.getValue(DiaryVo.class);
            if (mPostingId.getText().toString().equals(vo.getPostingId())) {

                mEditTitle.setText(vo.getTitle());
                mEditContent.setText(vo.getContent());
                mDiaryDate.setText(vo.getDate());
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void openDatePickerDialog() {

        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDiaryDate.setText(String.format("%d. %d. %d", year, (monthOfYear + 1), dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }
}