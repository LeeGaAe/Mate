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

import com.example.mate.Activity.Adapter.FestivalAdapter;
import com.example.mate.Activity.Vo.FestivalVo;
import com.example.mate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-03-01.
 */

public class FragmentFestival extends Fragment {

    @BindView(R.id.list_festival) RecyclerView mFestivalList;

    private FestivalAdapter adapter;
    private ArrayList<FestivalVo> mItems = new ArrayList<>();
    private FirebaseFirestore mDBStore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_festival, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mDBStore = FirebaseFirestore.getInstance();

        setData();


    }



    private void setData(){

        mDBStore.collection("Festival")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {

                                FestivalVo vo = document.toObject(FestivalVo.class);
                                mItems.add(vo);

                                setRecyclerView();
                            }
                        }
                    }
                });
    }



    private void setRecyclerView(){

        adapter = new FestivalAdapter(mItems);
        mFestivalList.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFestivalList.setLayoutManager(layoutManager);

    }

}
