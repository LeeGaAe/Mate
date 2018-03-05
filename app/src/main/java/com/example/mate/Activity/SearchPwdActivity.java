package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class SearchPwdActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.chan_user_pwd)
    LinearLayout mAreaUserPwd;

    @BindView(R.id.btn_search_pwd)
    ImageButton mBtnSearchPwd;

    @BindView(R.id.btn_chan_pwd)
    ImageButton mBtnChanPwd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pwd);

        ButterKnife.bind(this);

        mContext = this;

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnSearchPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAreaUserPwd.setVisibility(View.VISIBLE);
            }
        });

        mBtnChanPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext,LoginActivity.class);
                startActivity(mIntent);
                finish();

                Toast.makeText(mContext, "비밀번호가 변경 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
