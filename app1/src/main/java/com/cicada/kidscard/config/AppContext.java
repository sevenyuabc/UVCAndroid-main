package com.cicada.kidscard.config;

import android.content.Context;
import android.text.TextUtils;

import com.cicada.kidscard.utils.DeviceUtils;


public class AppContext {
    public static Context instance;
    public static AppEnvConfig appEnvConfig;
    private static boolean isYMDevice;
    private static boolean is32Device;

    public static void init(Context context, AppEnvConfig envConfig) {
        if (null == instance) {
            setDevicesType();
            instance = context;
            appEnvConfig = envConfig;
        }
    }

    /**
     * 设置设备类型
     */
    public static void setDevicesType() {
        String devicesModel = DeviceUtils.getDeviceModel();
        if (!TextUtils.isEmpty(devicesModel)) {
            if (devicesModel.toLowerCase().contains("rk3288")) {//育米设备
                isYMDevice = true;
            } else if (devicesModel.toLowerCase().contains("3280")) {//32寸设备
                is32Device = true;
            }
        }
    }

    public static boolean isRelease() {
        return AppEnvConfig.RELEASE.getIndex() == appEnvConfig.getIndex();
    }

    public static Context getContext() {
        return instance;
    }

    public static AppEnvConfig getAppEnv() {
        return appEnvConfig;
    }

    public static boolean isIsYMDevice() {
        return isYMDevice;
    }

    public static boolean isIs32Device() {
        return is32Device;
    }
}
