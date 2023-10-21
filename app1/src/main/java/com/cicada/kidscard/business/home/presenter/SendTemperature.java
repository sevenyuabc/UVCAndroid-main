package com.cicada.kidscard.business.home.presenter;

import android.content.Context;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.home.model.MainModel;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.db.model.BaseTemperatureInfo;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.tamsiree.rxtool.RxTool;
import com.tamsiree.rxtool.interfaces.OnSimpleListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName: SendTemperature
 * @Description: 上传测温记录
 * @Author: liuyun
 * @CreateDate: 2021/9/15 09:14
 * @UpdateUser: liuyun
 * @UpdateDate: 2021/9/15 09:14
 */
public class SendTemperature {
    private static SendTemperature instance = null;
    private final Context mContext;
    private boolean sending = false;

    public static SendTemperature getInstance() {
        if (instance == null) {
            synchronized (SendTemperature.class) {
                instance = new SendTemperature();
            }
        }
        return instance;
    }

    private SendTemperature() {
        mContext = MyApplication.getInstance().getApplicationContext();
    }

    public void send() {
        if (sending) {
            return;
        }
        try {
            BaseTemperatureInfo item = DBKidsCardHelp.getInstance(mContext).findFirstTemperatureInfo();
            if (null != item) {
                sendTemperature(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送体温记录
     *
     * @param temperatureInfo
     */
    private void sendTemperature(BaseTemperatureInfo temperatureInfo) {
        sending = true;
        temperatureInfo.setDevice_sn(BaseSharePreference.getInstance().getSn());
        temperatureInfo.setSourceType(2);
        RetrofitUtils.createService(MainModel.class)
                .uploadTemperatureRecord(temperatureInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onSuccess(String result) {
                        DBKidsCardHelp.getInstance(mContext).deleteTemperatureInfo(temperatureInfo.getLocalUniqueId());
                        sendNext();
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        sendNext();
                    }
                });
    }

    private void sendNext() {
        sending = false;
        RxTool.delayToDo(3000, new OnSimpleListener() {
            @Override
            public void doSomething() {
                send();
            }
        });
    }

}
