package com.cicada.kidscard.utils;

import android.os.Handler;

import com.cicada.kidscard.config.AppContext;
import com.seeku.android.BoardManager;

/**
 * @ClassName: XjjUtils
 * @Description: TODO
 * @Author: liuyun
 * @CreateDate: 2021/10/11 13:49
 * @UpdateUser: liuyun
 * @UpdateDate: 2021/10/11 13:49
 */
public class XjjUtils {
    /**
     * 开闸
     *
     */
    public static void openDoor() {
        if (AppContext.isIsYMDevice()) {
            BoardManager.getInstance().setGate(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BoardManager.getInstance().setGate(false);
                }
            }, 300);
        }
    }
}
