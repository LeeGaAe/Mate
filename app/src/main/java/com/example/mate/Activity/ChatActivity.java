package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.mate.Activity.Adapter.ChatAdapter;
import com.example.mate.Activity.Vo.ChatVo;
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
 * Created by 가애 on 2018-02-20.
 */

public class ChatActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private FirebaseUser mUser;

    private ChildEventListener mChildEventListener;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.chatting)
    RecyclerView mChatting;

    @BindView(R.id.back_chat)
    LinearLayout mBackChat;

    @BindView(R.id.edit_chat)
    EditText mEditChat;

    @BindView(R.id.btn_send)
    LinearLayout mBtnSend;


    private ChatAdapter adapter;
    private ArrayList<ChatVo> mItems = new ArrayList<>();

    String email;
    String json;
    SignUpVo java;
    String groupID;

    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        email = mUser.getEmail();

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);


        search();
        init();
        initFireBase();

    }

    private void init() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mEditChat.addTextChangedListener(textWatcher);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = mEditChat.getText().toString();
                long time = System.currentTimeMillis();

                if (!TextUtils.isEmpty(message)) {
                    ChatVo vo = new ChatVo();
                    vo.setMessage(mEditChat.getText().toString());
                    vo.setTime(time);
                    vo.setEmail(email);
                    vo.setGroupId(groupID);
                    mDBRef.child("chat").push().setValue(vo);
                }
                mEditChat.setText("");
            }
        });


    }

    private void initFireBase() {

        mDBRef.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    ChatVo vo = dataSnapshot.getValue(ChatVo.class);

                    if (vo.getGroupId().equals(groupID)) {
                        mItems.add(vo);

                        adapter = new ChatAdapter(mItems, email);
                        mChatting.setAdapter(adapter);
                        mChatting.scrollToPosition(mItems.size() - 1);

                        mChatting.setHasFixedSize(true);
                        mLayoutManager = new LinearLayoutManager(mContext);
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mChatting.setLayoutManager(mLayoutManager);
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
                groupID = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (TextUtils.isEmpty(mEditChat.getText().toString())) {
                mBtnSend.setVisibility(View.INVISIBLE);
            } else {
                mBtnSend.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
