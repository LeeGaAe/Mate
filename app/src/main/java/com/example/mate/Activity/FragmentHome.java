package com.example.mate.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

import Util.DateUtils;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentHome extends Fragment {

    private Intent mIntent;

    @BindView(R.id.my_name) TextView mMyName;
    @BindView(R.id.partner_name) TextView mPartName;
    @BindView(R.id.txt_select_start_day) TextView mSelectStartDay;

    @BindView(R.id.btn_diary) ImageView mDiary;

    @BindView(R.id.partner_profile) LinearLayout mPartProfile;
    @BindView(R.id.btn_date_picker) LinearLayout mBtnDatePicker;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    String json;
    SignUpVo java;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        json = PreferenceUtil.getInstance(getActivity()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("user");


        init();

    }

    private void init() {

        if(!TextUtils.isEmpty(java.getStartDate())){

            mSelectStartDay.setText("우리" + "\n" + getDatingPeriod(java.getStartDate()) + "\n" + "일째 사랑중");
        }

        mMyName.setText(java.getNickname());
        mPartName.setText(java.getPartnerVo().getPart_name());

        mDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), DiaryPageActivity.class);
                startActivity(mIntent);
            }
        });

        mPartProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), PartnerInfoDialog.class);
                startActivity(mIntent);
            }
        });

        /* 선택 날짜 구하기 */
        mBtnDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    private String getDatingPeriod(String startDate) {

        Date startDay = DateUtils.stringToDate(startDate);
        Date today = new Date();

        long gap = today.getTime() - startDay.getTime();
        int period = (int) (gap / (24 * 60 * 60 * 1000)) + 1;

        return String.valueOf(period);
    }


    private void openDatePicker() {

        final Calendar current = Calendar.getInstance();

        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int dayOfMonth = current.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year1, int month1, int dayOfMonth1) {

                Calendar select = Calendar.getInstance();
                select.set(year1, month1, dayOfMonth1);

                if (current.getTime().compareTo(select.getTime()) < 0) {

                    Toast.makeText(getContext(), R.string.message_start_date_re_select, Toast.LENGTH_SHORT).show();

                } else {

                    String selectDay = year1 + "." + (month1 + 1) + "." + dayOfMonth1;

                    updateInfo(selectDay);

                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, dayOfMonth);
        datePickerDialog.show();

    }

    private void updateInfo(final String startDay) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String me = user.getUid();

        // 여기부터 수정하면 돼 디비에 저장하는 부분
        mDBRef.child(me).child("startDay").setValue(startDay)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        java.setStartDate(startDay);

                        json = new Gson().toJson(java);
                        PreferenceUtil.getInstance(getContext()).setString(PreferenceUtil.MY_INFO, json);


                        mSelectStartDay.setText("우리" + "\n" + getDatingPeriod(startDay) + "\n" + "일째 사랑중");

                    }

                });

    }
}