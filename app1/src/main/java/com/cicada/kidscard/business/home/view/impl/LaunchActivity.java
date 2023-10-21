package com.cicada.kidscard.business.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.cicada.kidscard.R;
import com.cicada.kidscard.utils.NetStateUtils;
import com.cicada.kidscard.utils.UiHelper;

/**
 * 启动加载界面
 */
public class LaunchActivity extends Activity {
    private final Handler launchHandler = new Handler();
    LinearLayout ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        setContentView(R.layout.activity_launch);
        ll_loading = findViewById(R.id.ll_loading);
    }

    private void handleLaunchUI() {
        NetStateUtils.isOnline(null);
        launchHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //检查一次网络状态，然后启动首页
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        }, 2000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UiHelper.hideBottomNav(this);
        handleLaunchUI();
    }

    @Override
    protected void onDestroy() {
        ll_loading.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }
}
