package com.gc.tapCompute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.lxj.xpopup.XPopup;

/**
 * author gc
 * company enjoyPartyTime
 * date 2024/9/23
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        XPopup.setIsLightStatusBar(true);

        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            overridePendingTransition(0, R.anim.fade_out);
            finish();
        }, 300);
    }
}
