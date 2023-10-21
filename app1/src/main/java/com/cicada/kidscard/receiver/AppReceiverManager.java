package com.cicada.kidscard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cicada.kidscard.app.AppManager;
import com.cicada.kidscard.business.home.view.impl.LaunchActivity;
import com.cicada.kidscard.utils.LogUtils;

/**
 * @author hwp
 * 广播监听中心
 */
public class AppReceiverManager extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            LogUtils.d("==yy==action:", "==" + action);
            if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                Intent startMainProess = new Intent();
                startMainProess.setClass(context, LaunchActivity.class);
                startMainProess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMainProess);
            }  else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
                LogUtils.d("==yy==action:", "==ACTION_PACKAGE_REPLACED==");
                AppManager.getInstance().restartApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}