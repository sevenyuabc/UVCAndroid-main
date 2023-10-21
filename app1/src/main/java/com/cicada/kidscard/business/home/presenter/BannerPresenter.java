package com.cicada.kidscard.business.home.presenter;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.base.BasePresenter;
import com.cicada.kidscard.business.home.domain.event.EmsBannerDataUpdated;
import com.cicada.kidscard.business.home.model.BannerModel;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.Preconditions;
import com.cicada.kidscard.view.banner.BannerInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BannerPresenter extends BasePresenter {

    public BannerPresenter() {
    }

    /**
     * 轮播广告
     */
    public void getAdvInfo() {
        if (!AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)) {
            return;
        }
        RetrofitUtils.createService(BannerModel.class)
                .getAttendancePictureList(AppSharedPreferences.getInstance().getKidsCardSchoolId(), "",
                        new Request.Builder()
                                .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<BannerInfo>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(List<BannerInfo> result) {
                        String lastBannerDataStr = AppSharedPreferences.getInstance().getStringValue(Constants.ADV_INFO, "");
                        if (Preconditions.isNotEmpty(result)) {
                            AppSharedPreferences.getInstance().setStringValue(Constants.ADV_INFO, JSON.toJSONString(result));
                        } else {
                            AppSharedPreferences.getInstance().setStringValue(Constants.ADV_INFO, "");
                        }
                        if (!JSON.toJSONString(result).equals(lastBannerDataStr)) {
                            EventBus.getDefault().post(new EmsBannerDataUpdated());
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                    }
                });
    }
}
