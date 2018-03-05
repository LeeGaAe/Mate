package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-28.
 */

public class SearchIDActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    @BindView(R.id.btn_back)
    LinearLayout mBtnBack;

    @BindView(R.id.btn_search_id)
    ImageButton mBtnId;

    @BindView(R.id.find_user_id)
    LinearLayout mAreaUserId;

    @BindView(R.id.txt_find_user_id)
    TextView mTxtUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);

        ButterKnife.bind(this);

        mContext = this;

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                mAreaUserId.setVisibility(view.VISIBLE);
                mTxtUserId.setText("gaae_9411@naver.com");
            }
        });

    }
}
