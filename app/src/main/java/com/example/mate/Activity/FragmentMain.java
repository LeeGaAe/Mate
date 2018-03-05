package com.example.mate.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.mate.R;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-03-01.
 */

//하단바 고정
public class FragmentMain extends FragmentActivity {

    private Intent mIntent;


    @BindView(R.id.bottom_area)
    LinearLayout mBottomArea;

    @BindView(R.id.btn_home)
    LinearLayout mBtnHome;

    @BindView(R.id.btn_map)
    LinearLayout mBtnMap;

    @BindView(R.id.btn_chat)
    LinearLayout mBtnChat;

    @BindView(R.id.btn_more)
    LinearLayout mBtnMore;


    @Override
    public void onBackPressed() {
        setResult(Const.RESULT_SETTING_THEME);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_place, new FragmentHome())
                .commit();

        changeView();

    }

    private void changeView() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout frame = (FrameLayout) findViewById(R.id.fragment_place);

        if (frame.getChildCount() > 0) { // FrameLayout에서 뷰 삭제.
            frame.removeViewAt(0);
        }

        View view = null;

        mBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentHome())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentFestival())
                        .addToBackStack(null)
                        .commit();
            }
        });

        mBtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(mIntent);
            }
        });

        mBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_place, new FragmentSetting())
                        .addToBackStack(null)
                        .commit();
            }
        });

        if (view != null) {
            frame.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case Const.REQUEST_THEME_SET:

                String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
                mBottomArea.setBackgroundColor(Color.parseColor(ThemeColor));

                break;
        }
    }
}
