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

import com.example.mate.Activity.Adapter.ChatAdapter;
import com.example.mate.Activity.Adapter.DiaryPageAdapter;
import com.example.mate.Activity.Vo.ChatVo;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryPageActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private FirebaseUser mUser;

    @BindView(R.id.layout_list)
    LinearLayout mLayList;
    @BindView(R.id.layout_empty)
    LinearLayout mLayEmpty;
    @BindView(R.id.top_area)
    LinearLayout mTopArea;
    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;
    @BindView(R.id.btn_add)
    LinearLayout mBtnAdd;
    @BindView(R.id.list_diary)
    RecyclerView mLv_Diary;

    private DiaryPageAdapter adapter;
    private ArrayList<DiaryVo> mItems = new ArrayList<>();

    private String ThemeColor;
    private String GroupID;

//    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_diary);

        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        search();

        init();
        initFireBase();
    }


    private void init() {

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

    }



    private void initFireBase() {

        mDBRef.child("diary").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    DiaryVo vo = dataSnapshot.getValue(DiaryVo.class);

                    if (vo.getGroupID().equals(GroupID)) {
                        mItems.add(vo);

                        mLayEmpty.setVisibility(View.GONE);
                        mLayList.setVisibility(View.VISIBLE);

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mLayoutManager.setReverseLayout(true);
                        mLayoutManager.setStackFromEnd(true);

                        adapter = new DiaryPageAdapter(getApplicationContext());
                        adapter.setData(mItems);
                        mLv_Diary.setLayoutManager(mLayoutManager);
                        mLv_Diary.setAdapter(adapter);
                        mLv_Diary.setHasFixedSize(true);

                    } else {
                        mLayEmpty.setVisibility(View.VISIBLE);
                        mLayList.setVisibility(View.GONE);
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
        });
    }


    private void search() {

        DatabaseReference group = mDBRef.child("user").child(mUser.getUid()).child("groupId");
        group.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GroupID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}