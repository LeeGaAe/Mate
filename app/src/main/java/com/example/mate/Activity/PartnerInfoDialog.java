package com.example.mate.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;

import com.example.mate.R;

import butterknife.ButterKnife;

/**
 * Created by 가애 on 2018-01-22.
 */

public class PartnerInfoDialog extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_partner_info);

        ButterKnife.bind(this);

        mContext = this;
    }

}
