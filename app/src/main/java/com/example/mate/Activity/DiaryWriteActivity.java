package com.example.mate.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private Uri mImageUri;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.ll)
    LinearLayout mll;

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

    @BindView(R.id.img)
    ImageView mImage;

    @BindView(R.id.btn_camera_add)
    ImageView mBtnCameraAdd;

    private int mYear, mMonth, mDay;

    private String ThemeColor;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        ButterKnife.bind(this);
        mContext = this;

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();

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

                String postingId = mDBRef.push().getKey();

                String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
                SignUpVo java = new Gson().fromJson(json, SignUpVo.class);

                DiaryVo vo = new DiaryVo();
                vo.setPostingId(postingId);
                vo.setTitle(mEditTitle.getText().toString());
                vo.setContent(mEditContent.getText().toString());
                vo.setDate(mDiaryDate.getText().toString());
                vo.setWriterId(java.getNickname());

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

//        mBtnCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doTakePictureCamera();
//            }
//        });
//
//        mBtnAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doTakePictureAlbum();
//            }
//        });
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
            ActivityCompat.requestPermissions(DiaryWriteActivity.this, new String[]{android.Manifest.permission.CAMERA}, Const.REQUEST_PERMISSION_CAMERA);

        } else {

            mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
            mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(mIntent, Const.TAKE_PICTURE_CAMERA);

//            mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(mIntent, Const.TAKE_PICTURE_CAMERA);
        }

//        mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//        mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
//        startActivityForResult(mIntent, Const.TAKE_PICTURE_CAMERA);

    }

    /*
    앨범 호출하기
    */
    public void doTakePictureAlbum() {

        int permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(DiaryWriteActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Const.REQUEST_PERMISSION_ALBUM);

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
            case Const.REQUEST_PERMISSION_CAMERA:
                if (grantResults[0] == 0) {

                    mIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    mImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

                    mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(mIntent, Const.TAKE_PICTURE_CAMERA);

                } else {
                    finish();
                }
                break;

            case Const.REQUEST_PERMISSION_ALBUM:
                if (grantResults[0] == 0) {

                    mIntent = new Intent(Intent.ACTION_PICK);
                    mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);
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

                mImage.setVisibility(View.VISIBLE);
                mImageUri = data.getData();

            case Const.TAKE_PICTURE_CAMERA:

                mImage.setVisibility(View.VISIBLE);

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
                    mImage.setImageBitmap(photo);


                    break;
                }

                File f = new File(mImageUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
        }
    }
}