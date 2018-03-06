package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class DiaryPageActivity extends Activity {

    //    private Context mContext;
    private Intent mIntent;

    public static Context mContext;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.btn_add)
    LinearLayout mBtnAdd;

    @BindView(R.id.list_diary)
    RecyclerView mLv_Diary;

    @BindView(R.id.null_diary)
    TextView mDiaryNull;

    private DiaryPageAdapter adapter;
    private ArrayList<DiaryVo> mItems = new ArrayList<>();

    private String ThemeColor;
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_diary);

        ButterKnife.bind(this);
        mContext = this;

        mIntent = getIntent();

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("diary");

        ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, DiaryWriteActivity.class);
                startActivity(mIntent);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mDBRef.addChildEventListener(newDiary);
    }

    ChildEventListener newDiary = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            DiaryVo vo = dataSnapshot.getValue(DiaryVo.class);
            mItems.add(vo);

//            adapter = new DiaryPageAdapter(mItems);
            mLv_Diary.setAdapter(adapter);

            adapter.setData(mItems);
            adapter.notifyDataSetChanged();

            mLv_Diary.setHasFixedSize(true);

            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            mLv_Diary.setLayoutManager(mLayoutManager);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            adapter.notifyDataSetChanged();

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };
}