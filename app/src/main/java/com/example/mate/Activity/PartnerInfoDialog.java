package com.example.mate.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.gson.Gson;

import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-22.
 */

public class PartnerInfoDialog extends Activity {

    private Context mContext;

    @BindView(R.id.partner_name)
    TextView mTxtPartName;
    @BindView(R.id.partner_email)
    TextView mTxtPartEmail;
    @BindView(R.id.partner_phone)
    TextView mTxtPartPhone;
    @BindView(R.id.call)
    LinearLayout mCall;

    SignUpVo java;
    String tel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partner_info);

        ButterKnife.bind(this);
        mContext = this;

        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        tel = java.getPartnerVo().getPart_phone_num();
        init();

    }


    private void init() {

        mTxtPartName.setText(java.getPartnerVo().getPart_name());
        mTxtPartEmail.setText(java.getPartnerVo().getPart_email());
        mTxtPartPhone.setText(java.getPartnerVo().getPart_phone_num());

        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCallDialog();
            }
        });

    }

    private void openCallDialog() {

        /**
         * 사용자의 OS Version이 마시멜로우 이상인지 체크한다.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 현재 버전 == 마시멜로우(M) 버전보다 높은지 확인
            /**
             * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 체크한다.
             */
            int permissionResult = checkSelfPermission(Manifest.permission.CALL_PHONE);
            // Manifest 와 Permission 은 Android 로 Import 한다.

            // CALL_PHONE 의 권한이 없을때
            if (permissionResult == PackageManager.PERMISSION_DENIED) { // 현재 App 에서 권한에 대해서 DENIED 되어 있는지 알아보기
                /**
                 * 사용자가 CALL_PHONE 권한을 한번이라도 "거부" 한 적이 있는지 조사한다.
                 * 거부한 적이 한번이라도 있다면, true 를 리턴한다.
                 * 거부한 이력이 없다면, False 를 리턴한다.
                 */
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }).create().show();
                }
                // 최초로 권한을 요청 할 때
                else {
                    // CALL_PHONE 권한을 Android OS 에 요청한다.
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                }
            }
            // CALL_PHONE 의 권한이 있을때
            else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel : " + tel));
                startActivity(intent);
            }
        } else { // 사용자의 OS Version이 마시멜로우 이하일때
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel : " + tel));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1000) {
            // 요청한 권한을 사용자가 "허용" 했다면...
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel : " + tel));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(mContext, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}