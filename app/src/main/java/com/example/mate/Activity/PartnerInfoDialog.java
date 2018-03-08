package com.example.mate.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import com.example.mate.Activity.Vo.SignUpVo;
import com.example.mate.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import Util.PreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-22.
 */

public class PartnerInfoDialog extends Activity {

    private Context mContext;

    @BindView(R.id.partner_name) TextView mTxtPartName;
    @BindView(R.id.partner_email) TextView mTxtPartEmail;
    @BindView(R.id.partner_phone) TextView mTxtPartPhone;

    SignUpVo java;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partner_info);

        ButterKnife.bind(this);
        mContext = this;

        String json = PreferenceUtil.getInstance(getApplicationContext()).getString(PreferenceUtil.MY_INFO, "");
        java = new Gson().fromJson(json, SignUpVo.class);

        init();

    }


    private void init() {

        mTxtPartName.setText(java.getPartnerVo().getPart_name());
        mTxtPartEmail.setText(java.getPartnerVo().getPart_email());
        mTxtPartPhone.setText(java.getPartnerVo().getPart_phone_num());

    }

}
