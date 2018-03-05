package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.mate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class SearchInfoActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    @BindView(R.id.btn_cancel)
    LinearLayout mBtnCancel;

    @BindView(R.id.btn_search_id)
    ImageButton mBtnId;

    @BindView(R.id.btn_search_pwd)
    ImageButton mBtnPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_info);

        ButterKnife.bind(this);

        mContext = this;

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        mBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mIntent = new Intent(mContext,SearchIDActivity.class);
                startActivity(mIntent);
                finish();

            }
        });

        mBtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(mContext,SearchPwdActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}
