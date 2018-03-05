//package com.example.mate.Activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.example.mate.R;
//
//import Util.Const;
//import Util.PreferenceUtil;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by 가애 on 2017-11-06.
// */
//
//public class SettingActivity extends Activity {
//
//    private Context mContext;
//    private Intent mIntent;
//
//    @BindView(R.id.btn_back)
//    LinearLayout mBtnBack;
//
//    @BindView(R.id.top_area)
//    LinearLayout mTopArea;
//
//    @BindView(R.id.lock_pwd)
//    LinearLayout mBtnLockPwd;
//
//    @BindView(R.id.background_setting)
//    LinearLayout mBtnBackColor;
//
//    @BindView(R.id.backscreen_setting)
//    LinearLayout mBtnBackScreen;
//
//    @BindView(R.id.my_info)
//    LinearLayout mBtbMyInfo;
//
//
//    @Override
//    public void onBackPressed() {
//        setResult(Const.RESULT_SETTING_THEME);
//        finish();
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//
//
//        ButterKnife.bind(this);
//        mContext = this;
//
//        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
//        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));
//
//        mBtnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        mBtnLockPwd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext, PwdPageActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
//        mBtnBackColor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext, ThemeActivity.class);
//                startActivityForResult(mIntent,Const.REQUEST_THEME_SET);
//            }
//        });
//
//        mBtnBackScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext,BackgroundActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
//        mBtbMyInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext,MyInfoActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//
//            case Const.REQUEST_THEME_SET:
//
//                String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
//                mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));
//
//                break;
//        }
//    }
//}
