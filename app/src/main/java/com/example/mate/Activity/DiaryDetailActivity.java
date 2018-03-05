package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Adapter.DiaryPageAdapter;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryDetailActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private Uri mImageUri;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.ll)
    LinearLayout mll;

    @BindView(R.id.btn_cancel)
    LinearLayout mBtnCancel;

    @BindView(R.id.btn_more)
    LinearLayout mBtnMore;

    @BindView(R.id.postingId)
    TextView mPostingId;

    @BindView(R.id.diary_title)
    TextView mDiaryTitle;

    @BindView(R.id.diary_content)
    TextView mDiaryContent;

    @BindView(R.id.txt_diary_date)
    TextView mDiaryDate;

    @BindView(R.id.img)
    ImageView mImage;

    private int mYear, mMonth, mDay;

    private String ThemeColor;

    private DiaryPageAdapter adapter;
    private ArrayList<DiaryVo> mItems = new ArrayList<>();

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        setResult(Const.RESULT_REMOVE_DIARY);
//        finish();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_diary);

        ButterKnife.bind(this);
        mContext = this;

        mIntent = getIntent();

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("diary");

        ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        mPostingId.setText(mIntent.getStringExtra("postingId"));

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, DiaryMoreDialog.class);
                mIntent.putExtra("postingId", mPostingId.getText().toString());
//                startActivity(mIntent);
                startActivityForResult(mIntent, Const.REQUEST_REMOVE_DIARY);
            }
        });


        mDBRef.addChildEventListener(diary);
    }

    ChildEventListener diary = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            DiaryVo vo = dataSnapshot.getValue(DiaryVo.class);
            if (mPostingId.getText().toString().equals(vo.getPostingId())) {
                mDiaryTitle.setText(vo.getTitle());
                mDiaryContent.setText(vo.getContent());
                mDiaryDate.setText(vo.getDate());
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            DiaryVo vo = dataSnapshot.getValue(DiaryVo.class);
            if (mPostingId.getText().toString().equals(vo.getPostingId())) {
                mDiaryTitle.setText(vo.getTitle());
                mDiaryContent.setText(vo.getContent());
                mDiaryDate.setText(vo.getDate());
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            finish();

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case Const.REQUEST_REMOVE_DIARY:
//
////                setResult(Const.RESULT_REMOVE_DIARY);
//                finish();
//
//                break;
//        }
//
//    }
}