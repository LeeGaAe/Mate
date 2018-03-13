package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.File;
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

    @BindView(R.id.btn_set)
    LinearLayout mBtnSet;

    @BindView(R.id.img) ImageView mImage1;


    private ChatAdapter adapter;
    private ArrayList<ChatVo> mItems = new ArrayList<>();

    private Uri mImageUri;

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


        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
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


    private void openAlbum() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);

        dialog.setTitle("채팅창 화면");
        dialog.setCancelable(false);

        dialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                doTakePictureAlbum();
            }

        });


        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }



    /*
    앨범 호출하기
    */
    public void doTakePictureAlbum() {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.REQUEST_PERMISSION_ALBUM);

        } else {

            mIntent = new Intent(Intent.ACTION_PICK);
            mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case Const.REQUEST_PERMISSION_ALBUM:
                if (grantResults[0] == 0) {

                    mIntent = new Intent(Intent.ACTION_PICK);
                    mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);

                } else {

                    finish();

                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            onBackPressed();
            return;
        }
        switch (requestCode) {

            case Const.TAKE_PICTURE_ALBUM:

                mImageUri = data.getData();


            case Const.TAKE_PICTURE_CAMERA:

                mIntent = new Intent("com.android.camera.action.CROP");
                mIntent.setDataAndType(mImageUri, "image/*");
                mIntent.putExtra("scale", ImageView.ScaleType.FIT_XY);
                mIntent.putExtra("return-data", true);
                startActivityForResult(mIntent, Const.CROP_PICTURE);

                break;

            case Const.CROP_PICTURE:

                if ( resultCode != Activity.RESULT_OK) {
                    return;
                }

                final Bundle extra = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BanDiaryImage/" + System.currentTimeMillis() + ".jpg";

                if (extra != null) {
                    Bitmap photo = extra.getParcelable("data");

                    mImage1.setImageBitmap(photo);


                    break;
                }

                File f = new File(mImageUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
        }

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
