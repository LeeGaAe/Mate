package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mate.R;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-25.
 */

public class BackgroundActivity extends Activity {

    private Context mContext;
    private Intent mIntent;
    private Uri mImageCapture;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.btn_check)
    LinearLayout mBtnChk;

    @BindView(R.id.background)
    ImageView mBackground;

    @BindView(R.id.btn_album)
    ImageView mBtnAlbum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        ButterKnife.bind(this);
        mContext = this;

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBtnChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Toast.makeText(mContext, "완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mBtnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakePictureAlbum();
            }
        });


    }

    //갤러리 띄우기
    public void doTakePictureAlbum() {

        mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        mIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(mIntent, Const.TAKE_PICTURE_ALBUM);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.TAKE_PICTURE_ALBUM) {

            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                    mBackground.setImageBitmap(bitmap);

                } catch (Exception e) {

                }
            }

        }
    }
}
