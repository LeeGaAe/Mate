package com.example.mate.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mate.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Util.Const;
import Util.PreferenceUtil;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentHome extends Fragment {
    private Intent mIntent;

    private ImageView mDiary;

    private TextView mDday; // D-DAY날짜 출력
    private TextView mToday; // 오늘 날짜
    private TextView mday; // 기념일 날짜

    private LinearLayout mPartProfile;
    private LinearLayout mBtnDatePicker; //버튼

    int tYear;
    int tMonth;
    int tDay;

    int dYear = 0;
    int dMonth = 0;
    int dDay = 0;

    long dday;
    long today;
    long result;

    int resultValue = 0;
    Calendar calendar;  //Today
    Calendar calendar2;  //D-Day


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, container, false);

        mDiary = (ImageView) v.findViewById(R.id.btn_diary);
        mDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), DiaryPageActivity.class);
                startActivity(mIntent);
            }
        });

        mPartProfile = (LinearLayout) v.findViewById(R.id.partner_profile);
        mPartProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), PartnerInfoDialog.class);
                startActivity(mIntent);
            }
        });


        mToday = (TextView) v.findViewById(R.id.today);

        mday = (TextView) v.findViewById(R.id.d_day);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("day", Context.MODE_PRIVATE);
        String day = preferences.getString("dday", "");

        mBtnDatePicker = (LinearLayout) v.findViewById(R.id.btn_date_picker);

        mDday = (TextView) v.findViewById(R.id.dday);
        SharedPreferences pref = this.getActivity().getSharedPreferences("mDay", Context.MODE_PRIVATE);
        String dday = pref.getString("mDay", "");

        calendar = Calendar.getInstance();
        tYear = calendar.get(Calendar.YEAR);
        tMonth = calendar.get(Calendar.MONTH);
        tDay = calendar.get(Calendar.DAY_OF_MONTH);

        mToday.setText(String.format("%d.%d.%d", tYear, tMonth + 1, tDay));  //오늘 날짜 출력
        mday.setText(day);

        if (mDday == null) {
            mDday.setText("1");
        } else {
            mDday.setText(dday);
        }


        /* 선택 날짜 구하기 */
        calendar2 = Calendar.getInstance();
        dYear = calendar2.get(Calendar.YEAR);
        dMonth = calendar2.get(Calendar.MONTH);
        dDay = calendar2.get(Calendar.DAY_OF_MONTH);


        /* 선택 날짜 구하기 */
        mBtnDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), mDateSetListener, dYear, dMonth, dDay).show();
                mday.setText(String.format("%d.%d.%d", dYear, dMonth + 1, dDay));  //선택 날짜 출력
            }
        });
        return v;
    }


    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dYear = year;
            dMonth = monthOfYear;
            dDay = dayOfMonth;


            calendar2.set(Calendar.YEAR, dYear);
            calendar2.set(Calendar.MONTH, dMonth);
            calendar2.set(Calendar.DATE, dDay);

            today = calendar.getTimeInMillis() / (24 * 60 * 60 * 1000);
            dday = calendar2.getTimeInMillis() / (24 * 60 * 60 * 1000);
            result = today - dday;
            resultValue = (int) result + 1;


            UpdateDday();

        }
    };

    void UpdateDday() {

        mday.setText(String.format("%d.%d.%d", dYear, dMonth + 1, dDay));  //선택 날짜 출력

        if (resultValue > 0) {
            int absR = Math.abs(resultValue);

            mDday.setText(String.format("%d", absR));

            mday.setText(String.format("%d. %d. %d", dYear, (dMonth + 1), dDay));

            SharedPreferences preferences = this.getActivity().getSharedPreferences("day", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("dday", mday.getText().toString());
            editor.commit();

        } else if (resultValue == 0) {
            mDday.setText("1");
        } else {
            mDday.setText(String.format("D%d", resultValue));

        }

        SharedPreferences pref = this.getActivity().getSharedPreferences("mDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mDay", mDday.getText().toString());
        editor.commit();
    }
}
