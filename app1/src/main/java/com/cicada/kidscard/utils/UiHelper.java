package com.cicada.kidscard.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * ui相关帮助工具类
 * <p/>
 * @version
 * @since v0.0.1
 */
public class UiHelper {
	private static final String TAG = "UiHelper";

	/**
	 * 隐藏软键盘，界面无焦点
	 */
	public static void hideSoftInput(Context activity, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (view.getWindowToken() != null) {
				boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				LogUtils.d(TAG, "hideSoftInput:" + b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 隐藏软键盘，界面有焦点
	 */
	public static void hideSoftInput(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (activity.getCurrentFocus() != null) {
				if (activity.getCurrentFocus().getWindowToken() != null) {
					boolean b = imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					LogUtils.d(TAG, "hideSoftInput:" + b);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 前往设置页面
	 * @param context
	 */
	public static void gotoSetting(Context context){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings");
		intent.setComponent(cn);
		context.startActivity(intent);
	}
	/**
	 * 隐藏虚拟按键，并且全屏
	 */
	public static void hideBottomNav(Activity activity) {
		View decorView = activity.getWindow().getDecorView();
		decorView.setSystemUiVisibility(0);
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				;
		decorView.setSystemUiVisibility(uiOptions);
	}

}
