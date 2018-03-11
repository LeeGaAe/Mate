//package com.example.mate.Activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//
//import com.example.mate.R;
//
//import Util.Const;
//import Util.PreferenceUtil;
//
///**
// * Created by 가애 on 2017-11-08.
// */
//
//public class MenuActivity extends Activity {
//
//    private Context mContext;
//    private Intent mIntent;
//
//    private ImageButton mBtnDiary;
//    private ImageButton mBtnFestival;
//
//    private LinearLayout mTopArea;
//    private LinearLayout mBtnBack;
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu);
//
//        mContext = this;
//
//        mTopArea = (LinearLayout) findViewById(R.id.top_area);
//
//        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
//        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));
//
//
//        mBtnBack = (LinearLayout) findViewById(R.id.btn_back);
//        mBtnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//
//        mBtnDiary = (ImageButton) findViewById(R.id.btn_diary);
//        mBtnDiary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext, DiaryPageActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
////        mBtnFestival = (ImageButton) findViewById(R.id.btn_festival);
////        mBtnFestival.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIntent = new Intent(mContext, FestivalActivity.class);
////                startActivity(mIntent);
//////                Toast.makeText(getApplicationContext(), "축제일정", Toast.LENGTH_SHORT).show();
////            }
////        });
//    }
//
//}
