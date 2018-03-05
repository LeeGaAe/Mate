package com.example.mate.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mate.R;

import Util.Const;
import Util.PreferenceUtil;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentSetting extends Fragment {

    private Intent mIntent;


    private LinearLayout mBtnSet;
    private LinearLayout mBtnPwd;
    private LinearLayout mBtnTheme;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_setting, container, false);


        mBtnSet = (LinearLayout) v.findViewById(R.id.more_set);
        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(mIntent);
            }
        });

        mBtnPwd = (LinearLayout) v.findViewById(R.id.btn_pwd);
        mBtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), PwdPageActivity.class);
                startActivity(mIntent);
            }
        });


        mBtnTheme = (LinearLayout) v.findViewById(R.id.btn_theme);
        mBtnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), ThemeActivity.class);
                getActivity().startActivityForResult(mIntent,Const.REQUEST_THEME_SET);
//                startActivity(mIntent);
            }
        });


        return v;
    }

}
