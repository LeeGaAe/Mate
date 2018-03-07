package com.example.mate.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mate.Activity.Adapter.DiaryPageAdapter;
import com.example.mate.Activity.Adapter.FestivalAdapter;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.Activity.Vo.FestivalVo;
import com.example.mate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentFestival extends Fragment {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;


    private RecyclerView mFestivalList;

    private FestivalAdapter adapter;
    private ArrayList<FestivalVo> mItems = new ArrayList<>();

    LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(getActivity());

        View v = inflater.inflate(R.layout.activity_festival, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("festival");

        mFestivalList = (RecyclerView) v.findViewById(R.id.list_festival);

        ValueEventListener festival = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i =0; i<10; i++) {

                }
                FestivalVo vo = dataSnapshot.getValue(FestivalVo.class);
                mItems.add(vo);

                adapter = new FestivalAdapter(mItems);

                mFestivalList.setAdapter(adapter);
                mFestivalList.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mFestivalList.setLayoutManager(mLayoutManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        mDBRef.addValueEventListener(festival);


        return v;
    }


}
