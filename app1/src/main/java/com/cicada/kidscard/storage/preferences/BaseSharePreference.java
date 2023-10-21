package com.cicada.kidscard.storage.preferences;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.setting.domain.KidsCardSchoolSetting;
import com.tamsiree.rxtool.RxDeviceTool;

import java.util.Set;

public class BaseSharePreference {
    /**
     * 共享数据名
     */
    public static final String PREFERENCE = "cicadaApp";
    private static BaseSharePreference instance = null;

    private SharedPreferences mPreference;

    public static BaseSharePreference getInstance() {
        if (instance == null) {
            instance = new BaseSharePreference();
        }
        return instance;
    }

    public BaseSharePreference() {
        if (MyApplication.getInstance() != null) {
            mPreference = MyApplication.getInstance().getSharedPreferences(PREFERENCE, 0);
        }
    }

    /**
     * 设置文件数据
     *
     * @param key
     * @param value
     */
    private void setValue(String key, Object value) {
        if (value instanceof String) {
            mPreference.edit().putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            mPreference.edit().putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            mPreference.edit().putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            mPreference.edit().putLong(key, (Long) value).commit();
        } else if (value instanceof Float) {
            mPreference.edit().putFloat(key, (Float) value).commit();
        } else if (value instanceof Set) {
            mPreference.edit().putStringSet(key, (Set<String>) value).commit();
        }
    }

    /**
     * @param key
     * @param value 初始化默认数据反馈
     * @return
     */
    private boolean getBooleanValue(String key, boolean... value) {
        if (value != null && value.length > 0) {
            boolean result = value[0];
            return mPreference.getBoolean(key, result);
        }
        return mPreference.getBoolean(key, false);
    }

    private float getFloatValue(String key, Float... value) {
        if (value != null && value.length > 0) {
            float result = value[0];
            return mPreference.getFloat(key, result);
        }
        return mPreference.getFloat(key, 0f);
    }

    private int getIntValue(String key, int... value) {
        if (value != null && value.length > 0) {
            int result = value[0];
            return mPreference.getInt(key, result);
        }
        return mPreference.getInt(key, 0);
    }

    private long getLognValue(String key, Long... value) {
        if (value != null && value.length > 0) {
            long result = value[0];
            return mPreference.getLong(key, result);
        }
        return mPreference.getLong(key, 0l);
    }

    private String getStringValue(String key, String... value) {
        if (value != null && value.length > 0) {
            String result = value[0];
            return mPreference.getString(key, result);
        }
        return mPreference.getString(key, "");
    }

    /**
     * 设置刷卡学校设置信息
     *
     * @param kidsCardSchoolSetting
     * @author hwp
     * @since v0.0.1
     */
    public void setKidsCardSchoolSetting(KidsCardSchoolSetting kidsCardSchoolSetting) {
        try {
            if (kidsCardSchoolSetting != null) {
                setValue("kidscardSchoolsetting", kidsCardSchoolSetting.toString());
            } else {
                setValue("kidscardSchoolsetting", null);
            }
            AppSharedPreferences.getInstance().setKidsCardSchoolSetting(kidsCardSchoolSetting);
        } catch (Exception e) {
            e.printStackTrace();
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
            String json = getStringValue("kidscardSchoolsetting");
            if (!TextUtils.isEmpty(json)) {
                return JSON.parseObject(json, KidsCardSchoolSetting.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取网络上行数据
     *
     * @param up
     */
    public void setCurrentNetSpeedUp(float up) {
        setValue("netspeed_up", up);
    }

    public float getCurrentNetSpeedUp() {
        return getFloatValue("netspeed_up", 0.0f);
    }

    /**
     * 获取网络下行数据
     *
     * @param down
     */
    public void setCurrentNetSpeedDown(float down) {
        setValue("netspeed_down", down);
    }

    public float getCurrentNetSpeedDown() {
        return getFloatValue("netspeed_down", 0.0f);
    }

    public void setSn(String sn) {
        setValue("cicada_sn", sn);
    }

    public String getSn() {
        String sn = getStringValue("cicada_sn", "");
        if (TextUtils.isEmpty(sn)) {
            sn = RxDeviceTool.getSerialNumber();
            setSn(sn);
        }
        return sn;
    }

}
