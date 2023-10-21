package com.cicada.kidscard.utils;

import android.text.TextUtils;
import android.util.Log;

import com.cicada.kidscard.config.AppContext;

/**
 * 日志工具类
 * <p/>
 * @since v0.0.1
 */
public class LogUtils {
    private static final boolean DEBUG = !AppContext.isRelease();

    public static void d(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.w(tag, message, tr);
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable tr) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d(tag, message, tr);
        }
    }

    /**
     * http log method
     */
    public static void http(String className, String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            Log.d("httpMessage", className + " : " + message);
        }
    }

}