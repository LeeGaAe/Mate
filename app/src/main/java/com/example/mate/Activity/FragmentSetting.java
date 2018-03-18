package com.example.mate.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.File;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentSetting extends Fragment {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDBRef = mDatabase.getReference("user");
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private StorageReference mStorageRef = mStorage.getReferenceFromUrl("gs://gamate-4c0ad.appspot.com");


    private Context mContext;
    private Intent mIntent;

    @BindView (R.id.my_name) TextView mMyName;
    @BindView (R.id.my_email) TextView mMyEmail;

    @BindView (R.id.more_set) LinearLayout mBtnSet;
    @BindView (R.id.btn_pwd) LinearLayout mBtnPwd;
    @BindView (R.id.btn_theme) LinearLayout mBtnTheme;
    @BindView (R.id.notice) LinearLayout mBtnNotice;

    @BindView(R.id.my_pic) CircleImageView mMyPic;


    String json;
    SignUpVo java;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);


        json = PreferenceUtil.getInstance(getActivity()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);


        init();

    }


    private void init(){

        mDBRef.child(mUser.getUid()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    Glide.with(getContext()).load(R.mipmap.ic_launcher_ban).into(mMyPic);
                } else {
                    Glide.with(getContext()).load(dataSnapshot.getValue().toString()).into(mMyPic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

        mBtnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(mIntent);
            }
        });


    }
}
