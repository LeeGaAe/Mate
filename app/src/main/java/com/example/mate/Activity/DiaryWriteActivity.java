package com.example.mate.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.Activity.Vo.DiaryVo;
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
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.File;
import java.util.Calendar;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class DiaryWriteActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    private Uri uri;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference storageRef = mStorage.getReferenceFromUrl("gs://gamate-4c0ad.appspot.com");

    @BindView(R.id.top_area) LinearLayout mTopArea;
    @BindView(R.id.ll) LinearLayout mll;
    @BindView(R.id.btn_cancel) LinearLayout mBtnCancel;
    @BindView(R.id.btn_check) LinearLayout mBtnChk;
    @BindView(R.id.btn_date_dialog) LinearLayout mBtnDateDialog;
    @BindView(R.id.img_place) LinearLayout mImagePlace;

    @BindView(R.id.edit_title) EditText mEditTitle;
    @BindView(R.id.edit_content) EditText mEditContent;
    @BindView(R.id.txt_diary_date) TextView mDiaryDate;

    @BindView(R.id.img1) ImageView mImage1;
    @BindView(R.id.btn_camera_add) ImageView mBtnCameraAdd;
    @BindView(R.id.loading) ImageView mLoading;

    private int mYear, mMonth, mDay;

    private String ThemeColor;
    private String GroupID;
    private String Profile;
    private String postingId;

    String json;
    SignUpVo java;

    private InputMethodManager imm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        postingId = mDBRef.push().getKey();

        search();

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        //오늘 날짜 출력
        mDiaryDate.setText(String.format("%d. %d. %d", mYear, (mMonth + 1), mDay));

        mll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryVo vo = new DiaryVo();

                vo.setPostingId(postingId);
                vo.setTitle(mEditTitle.getText().toString());
                vo.setContent(mEditContent.getText().toString());
                vo.setDate(mDiaryDate.getText().toString());
                vo.setWriterId(java.getNickname());
                vo.setGroupID(GroupID);
                vo.setWriterProfileUri(Profile);

                if (uri == null) {
                    vo.setPhotoUri("");
                } else {
                    vo.setPhotoUri(uri.toString());
                }

                mDBRef.child("diary").child(postingId).setValue(vo);
                Toast.makeText(mContext, "완료되었습니다.", Toast.LENGTH_SHORT).show();

                onBackPressed();

            }
        });

        mBtnDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        mBtnCameraAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraDialog();
            }
        });

        mImagePlace.setVisibility(View.GONE);
        mImage1.setVisibility(View.GONE);

    }

    public void hideKeyBoard() {
        imm.hideSoftInputFromWindow(mEditTitle.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mEditContent.getWindowToken(), 0);
    }

    private void openDatePickerDialog() {

        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDiaryDate.setText(String.format("%d. %d. %d", year, (monthOfYear + 1), dayOfMonth));
            }
        }, mYear, mMonth, mDay);
        dialog.show();
    }

    private void openCameraDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);

        dialog.setTitle("사진첨부");
        dialog.setCancelable(false);

        dialog.setPositiveButton("갤러리", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePictureAlbum();
            }
        });

        dialog.setNegativeButton("카메라", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePictureCamera();
            }
        });

        dialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }


    /*
    카메라 호출하기
     */
    public void doTakePictureCamera() {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(DiaryWriteActivity.this,
                    new String[]{android.Manifest.permission.CAMERA}, Const.REQUEST_PERMISSION_CAMERA);

        } else {

            intentCamera();

        }

    }

    /*
    앨범 호출하기
    */
    public void doTakePictureAlbum() {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DiaryWriteActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.REQUEST_PERMISSION_ALBUM);

        } else {

            intentAlbum();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Const.REQUEST_PERMISSION_CAMERA :

                if (grantResults[0] == 0) {

                    intentCamera();

                } else {

                    finish();

                }

                break;

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

                mImagePlace.setVisibility(View.VISIBLE);
                mImage1.setVisibility(View.VISIBLE);

                final String path = getPath(data.getData());

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Glide.with(mContext).load(R.raw.loading_black).into(mLoading);

                final Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = storageRef.child("diary/").child(GroupID + "/" + file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(mContext).load(path).into(mImage1);
                        uri = taskSnapshot.getDownloadUrl();

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Glide.with(mContext).clear(mLoading);

                    }
                });

                break;


            case Const.TAKE_PICTURE_CAMERA:

                mImagePlace.setVisibility(View.VISIBLE);
                mImage1.setVisibility(View.VISIBLE);

                final String cameraPath = getPath(data.getData());

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Glide.with(mContext).load(R.raw.loading_black).into(mLoading);

                final Uri file2 = Uri.fromFile(new File(cameraPath));
                StorageReference riversRef2 = storageRef.child("diary/").child(GroupID + "/" + file2.getLastPathSegment());
                UploadTask uploadTask2 = riversRef2.putFile(file2);

                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(mContext).load(cameraPath).into(mImage1);

                        uri = taskSnapshot.getDownloadUrl();

                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Glide.with(mContext).clear(mLoading);

                    }
                });

                break;
        }
    }

        public String getPath(Uri uri){

        String [] proj = { MediaStore.Images.Media.DATA };
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground ();
        int index = cursor.getColumnIndexOrThrow (MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }


    private void intentCamera() {

        mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(mIntent,Const.TAKE_PICTURE_CAMERA);

    }

    private void intentAlbum() {

        mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);

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

        DatabaseReference profile = mDBRef.child("user").child(mUser.getUid()).child("profile");
        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Profile = "";
                } else {
                    Profile = dataSnapshot.getValue().toString();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}