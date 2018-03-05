//package com.example.mate.Activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.example.mate.R;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class MainActivity extends Activity {
//
//    private Context mContext;
//    private Intent mIntent;
//
////    @BindView(R.id.btn_home)
////    LinearLayout mBtnHome;
////
////    @BindView(R.id.btn_map)
////    LinearLayout mBtnMap;
////
////    @BindView(R.id.btn_chat)
////    LinearLayout mBtnChat;
////
////    @BindView(R.id.btn_more)
////    LinearLayout mBtnMore;
//
//    @BindView(R.id.btn_diary)
//    ImageView mBtnDiary;
//
////    @BindView(R.id.bottom_area)
////    RelativeLayout mBottomArea;
//
//    @BindView(R.id.my_profile)
//    ImageView mMyProfile;
//
//    @BindView(R.id.partner_profile)
//    ImageView mPartnerProfile;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//
//        mContext = this;
//
//        FirebaseMessaging.getInstance().subscribeToTopic("notice");
//
////        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
////        mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));
////
////        mBtnHome.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIntent = new Intent(mContext, MenuActivity.class);
////                startActivity(mIntent);
////            }
////        });
////
////        mBtnMap.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIntent = new Intent(mContext, FestivalActivity.class);
////                startActivity(mIntent);
////            }
////        });
////
////        mBtnChat.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIntent = new Intent(mContext, ChatActivity.class);
////                startActivity(mIntent);
////            }
////        });
////
////        mBtnMore.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                mIntent = new Intent(mContext, SettingActivity.class);
////                startActivityForResult(mIntent, Const.REQUEST_THEME_SET);
////            }
////        });
//
//        mBtnDiary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext, DiaryPageActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
//        mMyProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIntent = new Intent(mContext, MyInfoActivity.class);
//                startActivity(mIntent);
//            }
//        });
//
//        mPartnerProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                PartnerInfoDialog();
//            }
//        });
//    }
//
////    //이벤트 처리해주는 곳
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        switch (resultCode) {
////            case Const.RESULT_SETTING_THEME:
////
////                String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
////                mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));
////
////                break;
////        }
////    }
//
//    private void PartnerInfoDialog() {
//
//        PartnerInfoDialog dialog = new PartnerInfoDialog(this);
//        dialog.show();
//    }
//}
