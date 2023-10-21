package com.cicada.kidscard.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.DeviceUtils;
import com.cicada.kidscard.utils.FileUtil;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.NetworkUtils;
import com.clj.fastble.BleManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.seeku.android.BoardManager;
import com.tamsiree.rxtool.RxTool;
import com.tencent.bugly.crashreport.CrashReport;

import androidx.multidex.MultiDex;

public class MyApplication extends Application {
    private static MyApplication instance = null;

    public static int MYUID = 0;
    public long lastVerifyTime = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MYUID = android.os.Process.myUid();
        Thread.setDefaultUncaughtExceptionHandler(new AppException(this));
        AppContext.init(getApplicationContext(), DeviceUtils.getAppEnv(getApplicationContext()));

        LogUtils.d("appEnvName", DeviceUtils.getAppEnv(getApplicationContext()).getIndex() + "");

        BleManager.getInstance().init(this);

        RxTool.init(this);

        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "c9b167b88d", !AppContext.isRelease());

        checkAllAppFileDir();

        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        String appID = "546aadeb";
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        StringBuffer param = new StringBuffer();
        param.append("appid=" + appID);
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        param.append(",");
        //如需在非主进程使用语音功能
        param.append(SpeechConstant.FORCE_LOGIN + "=true");
        SpeechUtility.createUtility(MyApplication.getInstance(), param.toString());

        //初始化带宽
        NetworkUtils.initBandWidtStart();

        if (AppContext.isIsYMDevice()) {
            BoardManager.getInstance().init(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }


    /**
     * 获得当前应用的根文件存放路径
     */
    public String getAppRootDir() {
        String path = Environment.getExternalStorageDirectory().getPath()
                + "/KidsCard";
        FileUtil.checkAndMakeDir(path);
        return path;
    }

    /**
     * 获得应用错误log信息存储路径
     */
    public String getAppCrashLogDir() {
        String path = (new StringBuilder(getAppRootDir())).append("/crashlog/")
                .toString();
        FileUtil.checkAndMakeDir(path);
        return path;
    }

    /** d
     * 获得第三方应用安装包路径
     */
    public String getAppDownloadDir() {
        String path = (new StringBuilder(getAppRootDir())).append("/Download/")
                .toString();
        FileUtil.checkAndMakeDir(path);
        return path;
    }

    /**
     * 获得当前用户的图片文件存放路径
     */
    public String getAppSaveImageDir() {
        String path = (new StringBuilder(getAppRootDir())).append("/image/").append("/source/")
                .toString();
        FileUtil.checkAndMakeDir(path);
        return path;
    }

    /**
     * 获得当前用户的图片文件存放路径
     */
    public String getCompressImageDir() {
        String path = (new StringBuilder(getAppRootDir())).append("/image/").append("/compress/")
                .toString();
        FileUtil.checkAndMakeDir(path);

        return path;
    }

    /**
     * 保存拍照 图片地址路径
     *
     * @return
     */
    public String saveCameraImagePath() {
        return getAppSaveImageDir() + "cap_image_" + System.currentTimeMillis() + ".jpg";
    }

    /**
     * install apk 目录
     *
     * @return
     */
    public String getInstallApkPath(String versionName) {
        return getAppDownloadDir() + versionName + ".apk";
    }

    /**
     * 创建程序所需文件夹
     */
    public boolean checkAllAppFileDir() {
        FileUtil.checkAndMakeDir(getAppRootDir());
        FileUtil.checkAndMakeDir(getAppCrashLogDir());
        FileUtil.checkAndMakeDir(getAppDownloadDir());
        FileUtil.checkAndMakeDir(getAppSaveImageDir());
        FileUtil.checkAndMakeDir(getCompressImageDir());
        return true;
    }

    public Long getLastVerifyTime() {
        if (0 == lastVerifyTime) {
            lastVerifyTime = System.currentTimeMillis();
        }
        return lastVerifyTime;
    }

    public void updateLastVerifyTime() {
        lastVerifyTime = System.currentTimeMillis();
    }

    public long getIntervalTimes() {
        return System.currentTimeMillis() - getLastVerifyTime();
    }
}
