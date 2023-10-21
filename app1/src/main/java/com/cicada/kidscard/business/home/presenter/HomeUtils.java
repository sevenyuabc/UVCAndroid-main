package com.cicada.kidscard.business.home.presenter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.TextUtils;

import com.cicada.kidscard.business.setting.view.impl.SettingActivity;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.hardware.camera.CameraTakeManager;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.voice.IflytekVoice;
import com.seeku.android.BoardManager;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxShellTool;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * <p>
 * Create time: 2019/9/20 10:42
 *
 * @author liuyun.
 */
public class HomeUtils {

    /**
     * 去掉字符串中的字母
     *
     * @param name
     * @return
     */
    public static String getStringWithoutAlpha(String name) {
        // 为了把名字里带有的拼音去掉
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        String[] names = new String[name.length()];
        for (int i = 0, n = names.length; i < n; i++) {
            names[i] = name.substring(i, i + 1);
        }
        String digists = "abcdefghijklmnopqrstuvwxyz12345[]=";
        StringBuilder sb = new StringBuilder();
        boolean ok = false;
        for (String n : names) {
            if (n.equals("[")) {
                ok = true;
            }
            if (n.equals("]")) {
                ok = false;
            }
            if (digists.contains(n) && ok) {
                continue;
            }
            sb.append(n);
        }
        String result = sb.toString();
        result = result.replace("]", "");
        return result;
    }

    /**
     * 播放语音（文字转语音）
     *
     * @param obj
     */
    public static void playMessage(Object obj) {
        if (AppSharedPreferences.getInstance().getKidsCardVoiceStatus()) {
            IflytekVoice.getInstance().playMessage(obj.toString());
        }
    }

    /**
     * 获取SN
     *
     * @param context
     */
    public static String getSn(Context context) {
        return BaseSharePreference.getInstance().getSn();
    }


    public static void gotoSettingActivity(Context mContext) {
        CameraTakeManager.getInstance().destroy();
        Intent intent = new Intent(mContext, SettingActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 音量调到最大
     *
     * @param context
     */
    public static void setVoiceMax(Context context) {
        // 音量控制,初始化定义
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // 最大音量
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // tempVolume:音量绝对值
    }

    public static void installApk(String apkPath) {
        if (AppContext.isIsYMDevice()) {
            BoardManager.getInstance().appSilentInstall(apkPath);
        } else {
            String cmd1 = "chmod 777 " + apkPath + " \n";
            String cmd2 = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + apkPath + " \n";
            List<String> commands = new ArrayList<>();
            commands.add(cmd1);
            commands.add(cmd2);
            RxShellTool.execCmd(commands, RxAppTool.isAppRoot(), false);
        }
    }

    public static void reboot() {
        if (AppContext.isIsYMDevice()) {
            BoardManager.getInstance().reboot();
        } else {
            String cmd = "LD_LIBRARY_PATH=/vendor/lib:/system/lib su -c reboot \n";
            RxShellTool.execCmd(cmd, RxAppTool.isAppRoot(), false);
        }
    }

    public static void relaunch(Activity activity) {
        LogUtils.e("==========", "relaunch");
        Intent intent = activity.getPackageManager()
                .getLaunchIntentForPackage(activity.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

}
