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
import android.widget.Toast;

import com.example.mate.Activity.Adapter.PwdAdapter;
import com.example.mate.R;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by 가애 on 2017-12-09.
 */

public class PwdActivity extends Activity {

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

    private ArrayList<Integer> list;
    private PwdAdapter pwd_adapter;

    String mPassword = "";
    String aaa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);

        ButterKnife.bind(this);
        mContext = this;

        mIntent = getIntent();
        aaa = mIntent.getExtras().getString("isIntro");

        setPwd();

    }

    private void setPwd() {
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


                if (position != 9 && position != 11) {

                    int pwd = (Integer) parent.getAdapter().getItem(position);
                    mPassword += String.valueOf(pwd);

                    addBlackCircle();

                    if (mPassword.length() == 4) {

                        String password = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.COMPLETE_PASSWORD, "");

                        if (mPassword.equals(password)) {

                            if (aaa.equals("INTRO")) {
                                mIntent = new Intent(mContext, FragmentMain.class);
                                startActivity(mIntent);
                                finish();
                            } else {
                                mIntent = new Intent(mContext, PwdPageActivity.class);
                                startActivity(mIntent);
                                finish();
                            }

                        } else {
                            Toast.makeText(mContext, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    }
                }


                if (position == 9) {

                    finish();

                }

                if (position == 11) {

                    mPassword = mPassword.substring(0, mPassword.length() - 1);
                    deleteBlackCircle();

                }
            }
        });
    }


    public void addBlackCircle() {

        switch (mPassword.length()) {
            case 1:
                One.setImageResource(R.drawable.black_circle);
                break;

            case 2:
                Two.setImageResource(R.drawable.black_circle);
                break;

            case 3:
                Three.setImageResource(R.drawable.black_circle);
                break;

            case 4:
                Four.setImageResource(R.drawable.black_circle);
                break;

        }
    }

    public void deleteBlackCircle() {

        switch (mPassword.length()) {
            case 0:
                One.setImageResource(R.drawable.white_circle);
                break;

            case 1:
                Two.setImageResource(R.drawable.white_circle);
                break;

            case 2:
                Three.setImageResource(R.drawable.white_circle);
                break;

            case 3:
                Four.setImageResource(R.drawable.white_circle);
                break;
        }
    }

    public void reset() {
        One.setImageResource(R.drawable.white_circle);
        Two.setImageResource(R.drawable.white_circle);
        Three.setImageResource(R.drawable.white_circle);
        Four.setImageResource(R.drawable.white_circle);

        mPassword = "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch ( requestCode ) {

            case 500 :

                mIntent = new Intent(mContext, FragmentMain.class);
                startActivity(mIntent);
                finish();

                break;


        }
    }
}

