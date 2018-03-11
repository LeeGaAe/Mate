package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2017-12-05.
 */

public class SignUpOneActivity extends Activity {

    private Context mContext;
    private Intent mIntent;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.btn_cancel)
    LinearLayout mBtnCancel;

    @BindView(R.id.btn_access)
    ImageView mBtnAccess;

    @BindView(R.id.btn_privacy)
    ImageView mBtnPrivacy;

    @BindView(R.id.access)
    TextView mAccess;

    @BindView(R.id.privacy)
    TextView mPrivacy;

    @BindView(R.id.btn_agree)
    ImageView mBtnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);

        ButterKnife.bind(this);
        mContext = this;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference();

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String access = getResources().getString(R.string.access);
        mAccess.setText(Html.fromHtml(access));

        String privacy = getResources().getString(R.string.privacy2);
        mPrivacy.setText(Html.fromHtml(privacy));


        mBtnAgree.setEnabled(false);

        mBtnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnAccess.setSelected(!mBtnAccess.isSelected());

                if (mBtnAccess.isSelected() == false || mBtnPrivacy.isSelected()==false) {
                    mBtnAgree.setEnabled(false);
                } else {
                    mBtnAgree.setEnabled(true);
                }
            }
        });

        mBtnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnPrivacy.setSelected(!mBtnPrivacy.isSelected());

                if (mBtnPrivacy.isSelected() == false || mBtnAccess.isSelected() == false) {
                    mBtnAgree.setEnabled(false);
                } else {
                    mBtnAgree.setEnabled(true);
                }
            }
        });

        mBtnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBtnAgree.setEnabled(true);

                mIntent = new Intent(mContext,SignUpActivity.class);
                startActivity(mIntent);
                finish();

            }
        });
    }

}
