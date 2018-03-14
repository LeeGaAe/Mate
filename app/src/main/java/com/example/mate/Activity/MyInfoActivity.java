package com.example.mate.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2018-01-22.
 */

public class MyInfoActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDBRef = mDatabase.getReference("user");
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


    private Uri mImageUri;
    private Bitmap photo;

    @BindView(R.id.btn_back) LinearLayout mBtnBack;
    @BindView(R.id.top_area) LinearLayout mTopArea;
    @BindView(R.id.btn_check) LinearLayout mBtnCheck;
    @BindView(R.id.btn_disconnect) LinearLayout mBtnDisconnect;
    @BindView(R.id.modify_profile) LinearLayout mModifyProfile;

    @BindView(R.id.my_name) TextView mMyName;
    @BindView(R.id.my_email) TextView mMyEmail;
    @BindView(R.id.birth) TextView mMyBirth;
    @BindView(R.id.gender) TextView mMyGender;
    @BindView(R.id.edit_name) EditText mEditName;

    @BindView(R.id.btn_edit_name) ImageView mBtnEditName;
    @BindView(R.id.btn_complete) ImageView mBtnComplete;
    @BindView(R.id.my_pic) CircleImageView mMyPic;


    String json;
    SignUpVo java;



    String PartUid;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        ButterKnife.bind(this);
        mContext = this;

        search();
        init();

    }


    private void init() {


        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);


        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "수정되었습니다.", Toast.LENGTH_SHORT).show();

                onBackPressed();

            }
        });


        mMyName.setText(java.getNickname());
        mMyEmail.setText(java.getEmail());

        mMyName.setVisibility(View.VISIBLE);
        mEditName.setVisibility(View.GONE);

        mBtnEditName.setVisibility(View.VISIBLE);
        mBtnComplete.setVisibility(View.GONE);

        mMyBirth.setText(java.getBirth());

        if (java.getGender().equals("M")) {
            mMyGender.setText("남자");
        } else {
            mMyGender.setText("여자");
        }


        mBtnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyName.setVisibility(View.GONE);
                mEditName.setVisibility(View.VISIBLE);

                mBtnEditName.setVisibility(View.GONE);
                mBtnComplete.setVisibility(View.VISIBLE);

            }
        });


        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMyName.setText(mEditName.getText().toString());

                mDBRef.child(mUser.getUid()).child("nickname").setValue(mMyName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                java.setNickname(mMyName.getText().toString());
                                json = new Gson().toJson(java);
                                PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.MY_INFO, json);

                            }
                        });

                mDBRef.child(java.getPartnerVo().getPart_uid()).child("partnerVo").child("part_name").setValue(mMyName.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("lga", "success");
                            }
                        });

                mMyName.setVisibility(View.VISIBLE);
                mEditName.setVisibility(View.GONE);

                mBtnEditName.setVisibility(View.VISIBLE);
                mBtnComplete.setVisibility(View.GONE);

                onBackPressed();


            }
        });


        mModifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doTakePictureAlbum();

            }
        });



        mBtnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDisConnectDialog();

            }
        });

    }


    private void openDisConnectDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);

        dialog.setTitle("상대방과 연결을 끊겠습니까?");
        dialog.setMessage("모든 내용이 영구적으로 삭제되어\n복구할 수 없습니다.");
        dialog.setCancelable(false);

        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                mDBRef.child(mUser.getUid()).child("groupId").setValue(null);
                mDBRef.child(mUser.getUid()).child("partnerVo").setValue(null);

                Toast.makeText(mContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                mIntent = new Intent(mContext, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
                finish();


            }
        });

        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }




    public void doTakePictureAlbum() {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.REQUEST_PERMISSION_ALBUM);

        } else {

            intentAlbum();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case Const.REQUEST_PERMISSION_ALBUM :

                if (grantResults[0] == 0) {

                    intentAlbum();

                }

                else {

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

                Intent mIntent = new Intent("com.android.camera.action.CROP");
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

                if (extra != null) {

                    photo = extra.getParcelable("data");

                    mMyPic.setImageBitmap(photo);

                    break;

                }

                File f = new File(mImageUri.getPath());

                if (f.exists()) {

                    f.delete();

                }
        }
    }


    private void intentAlbum() {

        Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);

    }



    private void search() {

        DatabaseReference partnerUid = mDBRef.child(mUser.getUid()).child("partnerVo").child("part_uid");
        partnerUid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PartUid = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}