package com.cicada.kidscard.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.setting.domain.KidsCardSchoolSetting;
import com.cicada.kidscard.business.setting.domain.SchoolInfo;
import com.cicada.kidscard.hardware.Bluetooth.BluetoothDeviceInfo;

/**
 * 保存公共信息的 Preferences
 *
 * @author chenxuanxi
 */
public class AppSharedPreferences extends ConstantSharedPreferences {

    private static AppSharedPreferences instance = null;

    private SharedPreferences app = null;

    public AppSharedPreferences() {
        app = MyApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE);
    }

    public static AppSharedPreferences getInstance() {
        if (instance == null) {
            instance = new AppSharedPreferences();
        }
        return instance;
    }

    /********************************* 缓存公共方法 **************************************/
    // TODO:缓存公共方法

    /**
     * 存储字符串的信息
     *
     * @param key
     * @param Value

     */
    public void setStringValue(String key, String Value) {
        app.edit().putString(key, Value).commit();
    }

    /**
     * 获取字符串的信息
     *
     * @param key
     * @return

     */
    public String getStringValue(String key, String defaultString) {
        return app.getString(key, defaultString);
    }

    /**
     * 存储布尔值的信息
     *
     * @param key
     */
    public void setBooleanValue(String key, boolean value) {
        app.edit().putBoolean(key, value).commit();
    }

    /**
     * 获取布尔值的信息
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key, boolean defaultBoolean) {
        return app.getBoolean(key, defaultBoolean);
    }

    /**
     * 获取 登录服务器的token
     *
     * @return

     */
    public String getLoginToken() {
        return app.getString(LOGIN_TOKEN, "");
    }


    /**
     * 获取服务器当前时间
     */
    public long getServerTimeStamp() {
        return app.getLong(SERVER_TIME, System.currentTimeMillis());
    }

    /**
     * 设置服务器当前时间
     */
    public void setServerTimeStamp(long lngTimeStamp) {
        app.edit().putLong(SERVER_TIME, lngTimeStamp).commit();
        if (lngTimeStamp < 1) {
            setServerTimeStampDiffLocal(0);
        } else {
            long diff = lngTimeStamp - System.currentTimeMillis();
            if (diff < 0) {
                diff = 0;
            }
            setServerTimeStampDiffLocal(diff);
        }
    }

    /**
     * 获取服务器当前时间和本地当前时间的差值
     */
    public long getServerTimeStampDiffLocal() {
        return app.getLong(SERVER_TIME_DIFF_LOCAL, 0l);
    }

    /**
     * 获取服务器当前时间和本地当前时间的差值
     */
    public void setServerTimeStampDiffLocal(long lngTimeStamp) {
        app.edit().putLong(SERVER_TIME_DIFF_LOCAL, lngTimeStamp).commit();
    }

    /**
     * 获取服务器时间,本地时间进行校验过的时间戳
     */
    public long getLocalReal() {
        return System.currentTimeMillis() + getServerTimeStampDiffLocal();
    }


    /**
     * 获取是否有系统消息
     */
    public boolean getHasSystemMessage() {
        return app.getBoolean("has_system_message", false);
    }

    /**
     * 设置是否有系统消息
     */
    public void setHasSystemMessage(boolean hasSysMsg) {
        app.edit().putBoolean("has_system_message", hasSysMsg).commit();
    }

    // TODO:用户信息缓存

    /**
     * 清除用户缓存信息
     *

     */
    public void clearUserInfo() {
        setHasSystemMessage(false);
    }


    /********************************** 刷卡信息考勤相关变量存储 ***********************************************/
    // TODO:刷卡信息考勤相关变量存储

    /**
     * 设置刷卡学校Id
     *
     * @param schoolId

     */
    public void setKidsCardSchoolId(String schoolId) {
        app.edit().putString(KIDSCARDSCHOOLID, schoolId).commit();
    }

    /**
     * 获取刷卡学校Id
     *
     * @return

     */
    public String getKidsCardSchoolId() {
        return app.getString(KIDSCARDSCHOOLID, "");
    }

    public void setKidsCardSchoolName(String schoolName) {
        app.edit().putString(KIDSCARDSCHOOLNAME, schoolName).commit();
    }

    public String getKidsCardSchoolName() {
        return app.getString(KIDSCARDSCHOOLNAME, "");
    }


    /**
     * 设置刷卡学校对象
     *
     * @param schoolInfo

     */
    public void setKidsCardSchoolInfo(SchoolInfo schoolInfo) {
        if (schoolInfo != null) {
            app.edit().putString(KIDSCARDSCHOOLINFO, schoolInfo.toString()).commit();
        } else {
            app.edit().putString(KIDSCARDSCHOOLINFO, null).commit();
        }
    }

    /**
     * 获取刷卡学校对象
     *
     * @return
     * @since v0.0.1
     */
    public SchoolInfo getKidsCardSchoolInfo() {
        try {
            String json = app.getString(KIDSCARDSCHOOLINFO, null);
            if (!TextUtils.isEmpty(json)) {
                return JSON.parseObject(json, SchoolInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置刷卡学校设置信息
     *
     * @param kidsCardSchoolSetting

     */
    public void setKidsCardSchoolSetting(KidsCardSchoolSetting kidsCardSchoolSetting) {
        if (kidsCardSchoolSetting != null) {
            app.edit().putString(KIDSCARDSCHOOLSETTING, kidsCardSchoolSetting.toString()).commit();
        } else {
            app.edit().putString(KIDSCARDSCHOOLSETTING, "").commit();
        }
    }

    /**
     * 获取刷卡学校设置信息
     *
     * @return
     * @author hwp
     * @since v0.1
     */
    public KidsCardSchoolSetting getKidsCardSchoolSetting() {
        try {
            String json = app.getString(KIDSCARDSCHOOLSETTING, "");
            if (!TextUtils.isEmpty(json)) {
                return JSON.parseObject(json, KidsCardSchoolSetting.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置刷卡语音播报开关
     *
     * @param voiceStatus

     */
    public void setKidsCardVoiceStatus(boolean voiceStatus) {
        app.edit().putBoolean(KIDSCARDSCHOOLVOICESTATUS, voiceStatus).commit();
    }

    /**
     * 获取刷卡语音播报开关(默认开启)
     *
     * @return

     */
    public boolean getKidsCardVoiceStatus() {
        return app.getBoolean(KIDSCARDSCHOOLVOICESTATUS, true);
    }


    /**
     * 设置刷卡语音播报姓名开关
     *
     * @param voiceStatus

     */
    public void setKidsCardVoiceNameStatus(boolean voiceStatus) {
        app.edit().putBoolean(KIDSCARDSCHOOLVOICENAMESTATUS, voiceStatus).commit();
    }

    /**
     * 获取刷卡语音播报姓名开关(默认开启)
     *
     * @return

     */
    public boolean getKidsCardVoiceNameStatus() {
        return app.getBoolean(KIDSCARDSCHOOLVOICENAMESTATUS, true);
    }



    public String getUUID() {
        return app.getString("UUID2", null);
    }

    public void setUUID2(String uuid) {
        app.edit().putString("UUID2", uuid).commit();
    }

    /**
     * 设置蓝牙开关
     */
    public void setBlueOpen(boolean open) {
        setBooleanValue("blueOpen", open);
    }

    public boolean getBlueOpen() {
        return getBooleanValue("blueOpen", true);
    }


    /**
     * 蓝牙设备信息
     *
     * @param deviceInfo
     */
    public void setBluetoothDeviceInfo(BluetoothDeviceInfo deviceInfo) {
        if (null != deviceInfo) {
            setStringValue("ble_device_info", deviceInfo.toString());
        } else {
            setStringValue("ble_device_info", "");
        }
    }

    public BluetoothDeviceInfo getBluetoothDeviceInfo() {
        String value = getStringValue("ble_device_info", "");
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return JSON.parseObject(value, BluetoothDeviceInfo.class);
    }
}
