//package com.example.mate.Activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.example.mate.Activity.Vo.PartnerVo;
//import com.example.mate.Activity.Vo.SignUpVo;
//import com.example.mate.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.gson.Gson;
//
//import Util.PreferenceUtil;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by 가애 on 2017-12-28.
// */
//
//public class ConnectWaitActivity extends Activity {
//
//    private Context mContext;
//    private Intent mIntent;
//
//    private FirebaseDatabase mDatabase;
//    private DatabaseReference mDBRef;
//
//    @BindView(R.id.loading_connect)
//    ImageView mLoadingConnect;
//
//    @BindView(R.id.partner_num)
//    TextView mPartnerNum;
//
//    @BindView(R.id.partner_uid)
//    TextView mPartnerUid;
//
//    String groupID;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_connect_wait);
//        ButterKnife.bind(this);
//        mContext = this;
//        mIntent = getIntent();
//
//        mDatabase = FirebaseDatabase.getInstance();
//        mDBRef = mDatabase.getReference();
//
//        Glide.with(this).load(R.raw.loading_connect).into(mLoadingConnect);
//
//        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
//        SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
//
//        mDBRef.child("wait").child(java.getPhone_num()).child(java.getGroupID()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getChildrenCount() == 2) {
//                    Toast.makeText(mContext, "너도 이동", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
////        mPartnerNum.setText(mIntent.getStringExtra("partnerNum"));
////        mDBRef.child("user").addValueEventListener(isConnect);
//
////        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
////        SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
//
////        mPartnerNum.setText(mIntent.getStringExtra("partnerNum"));
////        mDBRef.child("user").addValueEventListener(isConnect);
////
////        mDBRef.child("wait").child(java.getGroupID()).addListenerForSingleValueEvent(isConnect);
//
////        mDBRef.child("wait").child(java.getPhone_num()).child(java.getGroupID()).addValueEventListener(isConnect);
//
//    }
////
////    ValueEventListener isConnect = new ValueEventListener() {
////        @Override
////        public void onDataChange(DataSnapshot dataSnapshot) {
////
////            if ( dataSnapshot.getChildrenCount() == 2 ){
////                Toast.makeText(mContext,"너도 이동",Toast.LENGTH_SHORT).show();
////            }
////
////        }
////
////        @Override
////        public void onCancelled(DatabaseError databaseError) {
////
////        }
////    };
//
////    ValueEventListener isConnect = new ValueEventListener() {
////        @Override
////        public void onDataChange(DataSnapshot dataSnapshot) {
////
////            if (dataSnapshot.getChildrenCount() == 1) {
////                Toast.makeText(mContext, "대기중", Toast.LENGTH_SHORT).show();
////            } else {
////                Toast.makeText(mContext, "연결", Toast.LENGTH_SHORT).show();
////
////            }
////
////        }
////
////        @Override
////        public void onCancelled(DatabaseError databaseError) {
////
////        }
////    };
//
//
////    ValueEventListener isConnect = new ValueEventListener() {
////
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        String me = user.getUid();
////
////
////        @Override
////        public void onDataChange(DataSnapshot dataSnapshot) {
////
////            String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
////            SignUpVo java = new Gson().fromJson(json, SignUpVo.class);
////
////
////            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                SignUpVo vo = snapshot.getValue(SignUpVo.class);
////
////                if (mPartnerNum.getText().toString().equals(vo.getPhone_num())) { // 상대방 번호찾기
////                    if (vo.getGroupID() == null) {
////                        Toast.makeText(mContext, "대기중", Toast.LENGTH_SHORT).show();
////
////
////                    } else {
////                        Toast.makeText(mContext, "연결되었습니다.", Toast.LENGTH_SHORT).show();
////
////                        String key = vo.getGroupID();
////                        mDBRef.child("wait").child(key).removeValue();
////                        mDBRef.child("user").child(me).child(key).removeValue();
////
////                        PartnerVo partnerVo = new PartnerVo();
////
////                        partnerVo.setPart_name(vo.getNickname());
////                        partnerVo.setPart_birth(vo.getBirth());
////                        partnerVo.setPart_phone_num(vo.getPhone_num());
////                        partnerVo.setPart_email(vo.getEmail());
////
////                        java.setPartnerVo(partnerVo);
////
////                        mDBRef.child("couple").child(java.groupID).child(me).setValue(java);
////
////
////                        mIntent = new Intent(mContext, FragmentMain.class);
////                        startActivity(mIntent);
////                        finish();
////
////                        return;
////
////                    }
////                }
////            }
////        }
////
////        @Override
////        public void onCancelled(DatabaseError databaseError) {
////
////        }
////    };
//}
