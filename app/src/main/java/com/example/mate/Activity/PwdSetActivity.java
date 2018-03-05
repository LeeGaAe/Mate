package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Adapter.PwdAdapter;
import com.example.mate.R;

import java.util.ArrayList;

import Util.Const;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-09.
 */

public class PwdSetActivity extends Activity {

    private Context mContext;
    private Intent mIntent;

    @BindView(R.id.Gv_Pwd)
    GridView mGv_Pwd;

    @BindView(R.id.one)
    ImageView One;

    @BindView(R.id.two)
    ImageView Two;

    @BindView(R.id.three)
    ImageView Three;

    @BindView(R.id.four)
    ImageView Four;

    @BindView(R.id.txt_pwd)
    TextView mTxtPwd;

    private ArrayList<Integer> list;
    private PwdAdapter pwd_adapter;

    int cnt = 0;
//    int del = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pwd);

        ButterKnife.bind(this);
        mContext = this;

        list = new ArrayList<>();

        for (int Num_Pwd : Const.APP_PW_SETTING) {
            list.add(Num_Pwd);
        }

        pwd_adapter = new PwdAdapter(getApplicationContext(), list);
        mGv_Pwd.setAdapter(pwd_adapter);
        mGv_Pwd.setVerticalScrollBarEnabled(false);
        mGv_Pwd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int tv = (Integer) parent.getAdapter().getItem(position);
                mTxtPwd.setText("" + tv);


                if (position == 9) {

                    mIntent = new Intent(mContext, PwdPageActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();

                }

                if (position == 11) {

                    String data = mTxtPwd.getText().toString();
                    data = data.substring(0, data.lastIndexOf(" "));

                }

//                switch (cnt) {
//                    case 1:
//                        One.setImageResource(R.drawable.black_circle);
//                        break;
//
//                    case 2:
//                        Two.setImageResource(R.drawable.black_circle);
//                        break;
//
//                    case 3:
//                        Three.setImageResource(R.drawable.black_circle);
//                        break;
//
//                    case 4:
//                        Four.setImageResource(R.drawable.black_circle);
//                        Toast.makeText(mContext, "비밀번호가 설정되었습니다.", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//                        break;
//                }

//                switch (del) {
//
//                    case 2:
//                        Three.setImageResource(R.drawable.white_circle);
//                        break;
//
//                    case 1:
//                        Two.setImageResource(R.drawable.white_circle);
//                        break;
//
//                    case 0:
//                        One.setImageResource(R.drawable.white_circle);
//                        break;
//
//                }

            }
        });

    }
}

