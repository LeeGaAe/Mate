package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mate.Activity.Adapter.FestivalAdapter;
import com.example.mate.Activity.Vo.FestivalVo;
import com.example.mate.R;

import java.util.ArrayList;
import java.util.List;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-25.
 */

//public class FestivalActivity extends Activity {
//
//    private Context mContext;
//
//    @BindView(R.id.btn_back)
//    LinearLayout mBtnBack;
//
//    @BindView(R.id.top_area)
//    LinearLayout mTopArea;
//
//    @BindView(R.id.list_festival)
//    RecyclerView mFestivalList;
//
//    LinearLayoutManager mLayoutManager;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_festival);
//
//        ButterKnife.bind(this);
//        mContext = this;
//
//        String ThemeColor = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.APP_THEME_COLOR, Const.APP_THEME_COLORS[0]);
//        mTopArea.setBackgroundColor(Color.parseColor(ThemeColor));
//
//        mBtnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        init();
//
//    }
//
//    public void init() {
//
//        List<FestivalVo> Items = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//
//            FestivalVo title = new FestivalVo();
//            title.setTitle("축제제목");
//            Items.add(title);
//
//        }
//
//
//        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//        mFestivalList.setLayoutManager(mLayoutManager);
//
//        mFestivalList.setAdapter(new FestivalAdapter((ArrayList<FestivalVo>) Items, R.layout.row_festival));
//
//    }
//}
