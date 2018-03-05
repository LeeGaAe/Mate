package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mate.R;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-08.
 */

public class PwdPageActivity extends Activity {

    private Context mContext;

    private Intent mIntent;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.change_pwd)
    LinearLayout mChanLock;

    @BindView(R.id.btn_lock_pwd)
    ImageButton mBtnLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_pwd);

        ButterKnife.bind(this);

        mContext = this;

        mIntent = getIntent();

        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));

        mBtnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnLock.setSelected(!mBtnLock.isSelected());

                if (mBtnLock.isSelected() == true) {

                    mIntent = new Intent(mContext, PwdSetActivity.class);
                    startActivity(mIntent);

                    mChanLock.setVisibility(View.VISIBLE);

                } else {
                    mChanLock.setVisibility(View.GONE);
                }

            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
