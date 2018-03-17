package com.example.mate.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Adapter.DiaryPageAdapter;
import com.example.mate.Activity.Vo.DiaryVo;
import com.example.mate.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Util.Const;
import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-22.
 */

public class DiaryMoreDialog extends Activity {

    private Context mContext;

    private Intent mIntent;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDBRef;

    @BindView(R.id.modify) LinearLayout mModify;
    @BindView(R.id.delete) LinearLayout mDelete;

    @BindView(R.id.postingId) TextView mPostingId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_diary_more);

        ButterKnife.bind(this);

        mContext = this;
        mIntent = getIntent();


        mDatabase = FirebaseDatabase.getInstance();
        mDBRef = mDatabase.getReference().child("diary");

        mPostingId.setText(mIntent.getStringExtra("postingId"));

        mModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mContext, DiaryModifyActivity.class);
                mIntent.putExtra("postingId", mPostingId.getText().toString());
                startActivity(mIntent);
                finish();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog();
            }
        });

    }

    private void openDeleteDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);

        dialog.setMessage("정말로 삭제하시겠습니까?");
        dialog.setCancelable(false);

        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String key = mPostingId.getText().toString();
                mDBRef.child(key).removeValue();

                Toast.makeText(mContext, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                mIntent = new Intent(mContext, DiaryPageActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                finish();

            }
        });

        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

}