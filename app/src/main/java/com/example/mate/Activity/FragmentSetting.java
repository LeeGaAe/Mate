package com.example.mate.Activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentSetting extends Fragment {

    private Intent mIntent;

    @BindView (R.id.my_name) TextView mMyName;
    @BindView (R.id.my_email) TextView mMyEmail;

    @BindView (R.id.more_set) LinearLayout mBtnSet;
    @BindView (R.id.btn_pwd) LinearLayout mBtnPwd;
    @BindView (R.id.btn_theme) LinearLayout mBtnTheme;

    @BindView(R.id.my_pic) CircleImageView mMyPic;

    Uri mImageUri;

    String json;
    SignUpVo java;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_setting, container, false);

        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mIntent = getActivity().getIntent();

        json = PreferenceUtil.getInstance(getActivity()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);


        init();

    }


    private void init(){


//        if ( mImageUri != null ) {
//            mImageUri = mIntent.getParcelableExtra("ProfileUri");
//        }



        mMyName.setText(java.getNickname());
        mMyEmail.setText(java.getEmail());

        mBtnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), MyInfoActivity.class);
                startActivity(mIntent);
            }
        });

        mBtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceUtil.getInstance(v.getContext()).getString(PreferenceUtil.COMPLETE_PASSWORD, null) != null){
                    mIntent = new Intent(getActivity(),PwdActivity.class);
                    mIntent.putExtra("isIntro" , "NO_INTRO");
                    startActivity(mIntent);
                }else{
                    mIntent = new Intent(getActivity(), PwdPageActivity.class);
                    startActivity(mIntent);
                }
            }
        });

        mBtnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), ThemeActivity.class);
                getActivity().startActivityForResult(mIntent,Const.REQUEST_THEME_SET);
            }
        });



//        mMyPic.setImageBitmap(BitmapFactory.decodeFile(extras.getString("uri")));

    }
}
