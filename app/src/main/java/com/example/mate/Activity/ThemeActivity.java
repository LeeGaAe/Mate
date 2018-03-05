package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.mate.Activity.Adapter.ThemeAdapter;
import com.example.mate.R;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-11.
 */

public class ThemeActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    @BindView(R.id.Gv_ThemeColor)
    GridView mGv_Theme;

    @BindView(R.id.top_area)
    LinearLayout mTopArea;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    private ThemeAdapter theme_adapter;
    private ArrayList<String> list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        ButterKnife.bind(this);

        mContext = this;

        mGv_Theme.setVerticalScrollBarEnabled(false);

        list = new ArrayList<>();

        for (String color : Const.APP_THEME_COLORS) {
            list.add(color);
        }

        theme_adapter = new ThemeAdapter(getApplicationContext(), list);
        mGv_Theme.setAdapter(theme_adapter);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mGv_Theme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[position]);

                mTopArea.setBackgroundColor(Color.parseColor(Const.APP_THEME_COLORS[position]));

            }
        });

        //첫 구동시 테마색 기본설정
        String FirstThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, null);

        if (TextUtils.isEmpty(FirstThemeColor)) {
            mTopArea.setBackgroundColor(Color.parseColor(Const.APP_THEME_COLORS[0]));
        }

        else {
            mTopArea.setBackgroundColor(Color.parseColor(FirstThemeColor));
        }
    }
}