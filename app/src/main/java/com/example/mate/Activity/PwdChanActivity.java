package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PwdChanActivity extends Activity {

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

    @BindView(R.id.re_one)
    ImageView re_One;
    @BindView(R.id.re_two)
    ImageView re_Two;
    @BindView(R.id.re_three)
    ImageView re_Three;
    @BindView(R.id.re_four)
    ImageView re_Four;

    @BindView(R.id.layout_set)
    LinearLayout mLaySet;
    @BindView(R.id.layout_more)
    LinearLayout mLayMore;

    private ArrayList<Integer> list;
    private PwdAdapter pwd_adapter;

    String mPassword = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chan_pwd);

        ButterKnife.bind(this);
        mContext = this;

        mLaySet.setVisibility(View.VISIBLE);
        mLayMore.setVisibility(View.GONE);

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

                        PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.PASSWORD, mPassword); //1차 비밀번호

                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLaySet.setVisibility(View.GONE);
                                mLayMore.setVisibility(View.VISIBLE);

                                mPassword = "";
                                morePwd();

                            }
                        },300);
                    }
                }


                if (position == 9) {

                    mIntent = new Intent(mContext, PwdPageActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();

                }

                if (position == 11) {

                    mPassword = mPassword.substring(0, mPassword.length() - 1);
                    deleteBlackCircle();

                }
            }
        });
    }

    private void morePwd() {
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


                    reAddBlackCircle();

                    if (mPassword.length() == 4) {

                        String password = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.PASSWORD,"");


                        //2차비밀번호
                        if (password.equals(mPassword)) {

                            PreferenceUtil.getInstance(getApplicationContext()).setString(PreferenceUtil.COMPLETE_PASSWORD, mPassword);
                            String complete_password = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.COMPLETE_PASSWORD, "");

                            mIntent = new Intent(mContext, PwdPageActivity.class);
                            mIntent.putExtra("complete_password", complete_password);
                            setResult(Activity.RESULT_OK, mIntent);
                            finish();

                        }

                        else {
                            Toast.makeText(mContext,"비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                            reset();
                        }
                    }
                }


                if (position == 9) {

                    mIntent = new Intent(mContext, PwdPageActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();

                }

                if (position == 11) {

                    mPassword = mPassword.substring(0, mPassword.length() - 1);
                    redeleteBlackCircle();

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

    public void reAddBlackCircle() {

        switch (mPassword.length()) {
            case 1:
                re_One.setImageResource(R.drawable.black_circle);
                break;

            case 2:
                re_Two.setImageResource(R.drawable.black_circle);
                break;

            case 3:
                re_Three.setImageResource(R.drawable.black_circle);
                break;

            case 4:
                re_Four.setImageResource(R.drawable.black_circle);
                break;

        }
    }

    public void redeleteBlackCircle() {

        switch (mPassword.length()) {
            case 0:
                re_One.setImageResource(R.drawable.white_circle);
                break;

            case 1:
                re_Two.setImageResource(R.drawable.white_circle);
                break;

            case 2:
                re_Three.setImageResource(R.drawable.white_circle);
                break;

            case 3:
                re_Four.setImageResource(R.drawable.white_circle);
                break;
        }
    }

    public void reset() {
        re_One.setImageResource(R.drawable.white_circle);
        re_Two.setImageResource(R.drawable.white_circle);
        re_Three.setImageResource(R.drawable.white_circle);
        re_Four.setImageResource(R.drawable.white_circle);

        mPassword="";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mIntent = new Intent(mContext, PwdPageActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
        finish();

    }
}

