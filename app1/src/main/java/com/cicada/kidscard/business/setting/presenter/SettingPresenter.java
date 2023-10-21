package com.cicada.kidscard.business.setting.presenter;

import android.content.Context;

import com.cicada.kidscard.R;
import com.cicada.kidscard.app.AppException;
import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.base.BasePresenter;
import com.cicada.kidscard.business.home.domain.event.EmsHasSchool;
import com.cicada.kidscard.business.home.presenter.BannerPresenter;
import com.cicada.kidscard.business.home.presenter.ContactPresenter;
import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.business.setting.domain.DeviceInfo;
import com.cicada.kidscard.business.setting.domain.SchoolInfo;
import com.cicada.kidscard.business.setting.model.SettingModel;
import com.cicada.kidscard.business.setting.view.ISettingView;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.db.DAOHelperSchool;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsFaceInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsVerifyLog;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * TODO
 * <p>
 * Create time: 2019/11/11 10:12
 *
 * @author liuyun.
 */
public class SettingPresenter extends BasePresenter {
    private final Context mContext;
    private final ISettingView iSettingView;

    public SettingPresenter(Context mContext, ISettingView settingView) {
        this.mContext = mContext;
        this.iSettingView = settingView;
    }


    public void findSchoolInfoById(String schoolId, final boolean isShowDialog) {
        if (isShowDialog) {
            ToastUtils.showToastImage(mContext, mContext.getString(R.string.dialog_title_waiting), 0);
        }
        RetrofitUtils.createService(SettingModel.class)
                .findschoolbyid(new Request.Builder()
                        .withParam("schoolId", schoolId)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<SchoolInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(SchoolInfo info) {
                        if (null != iSettingView) {
                            iSettingView.findSchoolByIdResult(info);
                        }
                        ToastUtils.cancel();
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if (null != iSettingView) {
                            iSettingView.findSchoolByIdResult(null);
                        }
                        AppException.handleException(mContext, errorCode, errorMessage);
                    }
                });
    }

    /**
     * 绑定设备
     */
    public void saveOrUpdateDevice(SchoolInfo schoolInfo) {
        ToastUtils.showToastImage(mContext, mContext.getString(R.string.dialog_title_waiting), 0);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setSchoolId(schoolInfo.getSchoolId());
        deviceInfo.setSchoolName(schoolInfo.getSchoolName());
        deviceInfo.setDeviceName(schoolInfo.getSchoolName() + HomeUtils.getSn(mContext));
        RetrofitUtils.createService(SettingModel.class)
                .saveOrUpdateDevice(deviceInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(String info) {
                        //绑定学校后：删除本地通讯录，重新拉取通讯录数据
                        try {
                            DAOHelperSchool.getInstance(mContext).mDBUtils.deleteAll(BaseKidsCardChildInfo.class);
                            DAOHelperSchool.getInstance(mContext).mDBUtils.deleteAll(BaseKidsFaceInfo.class);
                            DAOHelperSchool.getInstance(mContext).mDBUtils.deleteAll(BaseKidsVerifyLog.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        AppSharedPreferences.getInstance().setKidsCardSchoolId(schoolInfo.getSchoolId());
                        AppSharedPreferences.getInstance().setKidsCardSchoolName(schoolInfo.getSchoolName());
                        AppSharedPreferences.getInstance().setKidsCardSchoolInfo(schoolInfo);
                        MyApplication.getInstance().updateLastVerifyTime();
                        ContactPresenter.getInstance().queryContactAdd();
                        new BannerPresenter().getAdvInfo();
                        ToastUtils.cancel();
                        EventBus.getDefault().post(new EmsHasSchool());
                        if (null != iSettingView) {
                            iSettingView.bindSchoolSuccess();
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        AppException.handleException(mContext, errorCode, errorMessage);
                    }
                });
    }
}
