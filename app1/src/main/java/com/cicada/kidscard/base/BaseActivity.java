package com.cicada.kidscard.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.LinearLayout;

import com.cicada.kidscard.R;
import com.cicada.kidscard.app.AppManager;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.NetStateUtils;
import com.cicada.kidscard.utils.NetworkUtils;
import com.cicada.kidscard.utils.UiHelper;
import com.cicada.kidscard.view.StatusView;
import com.tamsiree.rxtool.RxTool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.LayoutRes;

/**
 * FileName: BaseActivity
 * Author: Target
 * Date: 2020/6/3 3:25 PM
 * 基类
 */
public abstract class BaseActivity extends Activity implements IBaseView, NetStateUtils.PingNetWorkListener {
    private  int BASE_LAYOUT_RES_ID = R.layout.activity_base;
    private static final LinearLayout.LayoutParams LAYOUT_PARAMS =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    private LinearLayout parentView;
    private StatusView statusView;
    protected ScheduledExecutorService netExecutorService;
    private ListenerReceiver listenerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 取消标题
        if(AppContext.isIs32Device()){
            BASE_LAYOUT_RES_ID =  R.layout.activity_base;
        }else  if(AppContext.isIsYMDevice()){
            BASE_LAYOUT_RES_ID =  R.layout.activity_base_ym;
        }
        setContentView(BASE_LAYOUT_RES_ID);
        AppManager.getInstance().addActivity(this);
        checkNet();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (BASE_LAYOUT_RES_ID == layoutResID) {
            super.setContentView(layoutResID);
            parentView = (LinearLayout) findViewById(R.id.base_parent_view);
            statusView = findViewById(R.id.status_view);
        } else {
            parentView.addView(getLayoutInflater().inflate(layoutResID, null), LAYOUT_PARAMS);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        UiHelper.hideBottomNav(this);
    }

    public void registerListenerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        listenerReceiver = new ListenerReceiver();
        registerReceiver(listenerReceiver, filter);
    }

    private void unregisterListenerReceiver() {
        if (null != listenerReceiver) {
            unregisterReceiver(listenerReceiver);
            listenerReceiver = null;
        }
    }


    private class ListenerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    if (!RxTool.isFastClick(500)) {
                        netConnectChanged(NetworkUtils.isNetworkAvailable(BaseActivity.this));
                    }
                    break;
            }
        }
    }

    private void netConnectChanged(boolean connected) {
        if (connected) {
            NetStateUtils.isOnline(this);
        } else {
            AppSharedPreferences.getInstance().setBooleanValue(Constants.NET_AVAILABLE, false);
            updateNetStatus(false);
        }
    }

    protected void netChanged(){}

    private void checkNet() {
        netExecutorService = Executors.newSingleThreadScheduledExecutor();
        netExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(Constants.UPDATE_NET_SPEED);
                mHandler.sendEmptyMessage(Constants.NET_CHECK);
            }
        }, 1, 10, TimeUnit.SECONDS);
        registerListenerReceiver();
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.UPDATE_NET_SPEED://更新网速
                    statusView.updateNetSpeed();
                    break;
                case Constants.UPDATE_NET_STATUS://更新网咯状态
                    Bundle bd = msg.getData();
                    if (null != bd)
                        statusView.setNetStatusView(bd.getBoolean("connected", false));
                    break;
                case Constants.UPDATE_UN_UPLOAD_COUNT://更新未上传记录数
                    statusView.setUploadDataCount();
                    break;
                case Constants.NET_CHECK://检查网络状态
                    NetStateUtils.isOnline(BaseActivity.this);
                    break;
            }
            handleSelfMessage(msg);
        }
    };

    protected void updateNetStatus(boolean isConnected) {
        Message msg = new Message();
        msg.what = Constants.UPDATE_NET_STATUS;
        Bundle bd = new Bundle();
        bd.putBoolean("connected", isConnected);
        msg.setData(bd);
        mHandler.removeMessages(Constants.UPDATE_NET_STATUS);
        mHandler.sendMessage(msg);
    }

    @Override
    public void onPingState(boolean isConnected) {
        LogUtils.d("====onPingState======","isConnected:" + isConnected);
        updateNetStatus(isConnected);
        netChanged();
    }

    protected void handleSelfMessage(Message msg) {
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != netExecutorService) {
            netExecutorService.shutdownNow();
        }
        unregisterListenerReceiver();
        AppManager.getInstance().removeActivity(this);

    }
}
