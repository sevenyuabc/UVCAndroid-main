package com.cicada.kidscard.business.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.base.BasePresenter;
import com.cicada.kidscard.business.home.domain.VersionInfo;
import com.cicada.kidscard.business.home.model.CommonModel;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.net.domain.HeatRequest;
import com.cicada.kidscard.net.domain.PollDataModel;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.service.DownLoadAppBackService;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxDeviceTool;
import com.tamsiree.rxtool.RxFileTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommonPresenter extends BasePresenter {
    private final Context mContext;

    public CommonPresenter() {
        mContext = AppContext.getContext();
    }

    /**
     * 检测新版本
     */
    public void checkNewVersion() {
        RetrofitUtils.createService(CommonModel.class)
                .checkVersion(new Request.Builder()
                        .withParam("packageName", RxDeviceTool.getAppPackageName())
                        .withParam("versionCode", RxDeviceTool.getAppVersionNo(AppContext.getContext()))
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<VersionInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(VersionInfo info) {
                        if (null != info
                                && (1 == info.getUpdateType() || 2 == info.getUpdateType())
                                && !TextUtils.isEmpty(info.getDownLoadUrl())
                                && info.getVersionCode() > RxAppTool.getAppVersionCode(mContext)) {
                            RxFileTool.deleteDir(MyApplication.getInstance().getAppDownloadDir());
                            DownLoadAppBackService.startDownLoadAppService(mContext, info.getVersion(), info.getDownLoadUrl());
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                    }
                });
    }

    /**
     * 心跳接口
     */
    public void heartBeat() {
        RetrofitUtils.createService(CommonModel.class)
                .heatBeat(new HeatRequest.Builder()
                        .withData(new PollDataModel())
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(String info) {

                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                    }
                });
    }


}
