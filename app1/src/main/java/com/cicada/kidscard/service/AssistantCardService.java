package com.cicada.kidscard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.home.domain.FileInfo;
import com.cicada.kidscard.business.home.domain.event.EmsShowAD;
import com.cicada.kidscard.business.home.model.MainModel;
import com.cicada.kidscard.business.home.presenter.BannerPresenter;
import com.cicada.kidscard.business.home.presenter.CommonPresenter;
import com.cicada.kidscard.business.home.presenter.ContactPresenter;
import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.business.home.presenter.SendCardMessage;
import com.cicada.kidscard.business.home.presenter.SendTemperature;
import com.cicada.kidscard.business.home.presenter.UploadPresenter;
import com.cicada.kidscard.business.home.view.IUploadView;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.db.model.BaseKidsCardTakePhoto;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.Preconditions;
import com.tamsiree.rxtool.RxFileTool;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Target.Fan on 2021/3/3 3:05 PM
 */
public class AssistantCardService extends Service {
    private CompositeDisposable mCompositeDisposable;
    private ScheduledExecutorService executorService;
    public MyBinder binder = new MyBinder();
    private static final int BANNER_INTERVAL = 1 * 30 * 1000;
    private static final int INVALID_BANNER_INTERVAL = 3 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        initSchedule();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class MyBinder extends Binder {
        public String getServiceName() {
            return AssistantCardService.class.getSimpleName();
        }
    }


    private void initSchedule() {
        if (null == executorService) {
            executorService = Executors.newScheduledThreadPool(1 + 2 * Runtime.getRuntime().availableProcessors());
            /**
             * 每隔50秒
             * 1、是否进入banner页
             * 2、重启
             */
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (AppContext.isIs32Device()) {
                            //如果间隔时间大于3分钟，说明上次记录时间的时间 是系统默认时间，需要更新一次
                            if (MyApplication.getInstance().getIntervalTimes() > INVALID_BANNER_INTERVAL) {
                                MyApplication.getInstance().updateLastVerifyTime();
                            }
                            // 1分钟内没有刷卡操作 显示banner
                            if (MyApplication.getInstance().getIntervalTimes() > BANNER_INTERVAL) {
                                EventBus.getDefault().post(new EmsShowAD());
                            }
                        }
                        handleReboot();
                        LogUtils.d("1分钟定时任务====", "已执行");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }, 5, 50, TimeUnit.SECONDS);

            /**
             * 每隔 5 分钟
             * 1、上传刷卡记录
             * 2、上传体温记录
             */
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)) {
                            new CommonPresenter().heartBeat();
                            if (Preconditions.isNotEmpty(AppSharedPreferences.getInstance().getKidsCardSchoolId())) {
                                SendCardMessage.getInstance().send();
                                SendTemperature.getInstance().send();
                            }
                        }
                        LogUtils.d("5分钟定时任务====", "已执行");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 30, 5 * 60, TimeUnit.SECONDS);
            /**
             * 每隔 11 分钟
             * 1、增量更新通讯录
             * 2、获取banner信息
             */
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)
                                && Preconditions.isNotEmpty(AppSharedPreferences.getInstance().getKidsCardSchoolId())) {
                            ContactPresenter.getInstance().queryContactAdd();
                            if (AppContext.isIs32Device()) {
                                new BannerPresenter().getAdvInfo();
                            }
                        }
                        LogUtils.d("10分钟定时任务====", "已执行");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1, 11, TimeUnit.MINUTES);

            /**
             * 每隔 31 分钟
             * 1、上传刷卡补偿记录
             * 2、版本更新
             */
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)) {
                            //检查版本更新
                            new CommonPresenter().checkNewVersion();
                            //照片补偿
                            uploadNatCardRecordPhoto();
                        }
                        LogUtils.d("30分钟定时任务====", "已执行");
                        RxFileTool.deleteDir(MyApplication.getInstance().getAppCrashLogDir());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1, 31, TimeUnit.MINUTES);
        }
    }

    /**
     * 上传本地未上传刷卡记录图片
     */
    private void uploadNatCardRecordPhoto() {
        List<BaseKidsCardTakePhoto> baseKidsCardTakePhotos = DBKidsCardHelp.getInstance(AppContext.getContext()).findPhotoRecord();
        if (Preconditions.isNotEmpty(baseKidsCardTakePhotos)) {
            for (BaseKidsCardTakePhoto baseKidsCardTakePhoto : baseKidsCardTakePhotos) {
                uploadPostFile(baseKidsCardTakePhoto);
            }
        }
    }


    /**
     * 上传文件
     *
     * @param baseKidsCardTakePhoto
     */
    private void uploadPostFile(BaseKidsCardTakePhoto baseKidsCardTakePhoto) {
        if (RxFileTool.isFileExists(baseKidsCardTakePhoto.getPhotoPath())) {
            new UploadPresenter(new IUploadView() {
                @Override
                public void uploadSuccess(List<FileInfo> fileInfoList) {
                    baseKidsCardTakePhoto.setPhotoPath(fileInfoList.get(0).getUrl());
                    DBKidsCardHelp.getInstance(AppContext.getContext()).updateSendPhotoErrorRecord(baseKidsCardTakePhoto);
                    uploadCompensateCardRecordMessage(baseKidsCardTakePhoto);
                }

                @Override
                public void uploadFailed() {
                }
            }).uploadPostFile(baseKidsCardTakePhoto.getPhotoPath());
        } else {
            DBKidsCardHelp.getInstance(AppContext.getContext()).deleteFailePhoto(baseKidsCardTakePhoto);
        }
    }


    /**
     * 定时上传补偿刷卡记录
     *
     * @param baseKidsCardTakePhoto
     */
    public void uploadCompensateCardRecordMessage(BaseKidsCardTakePhoto baseKidsCardTakePhoto) {
        RetrofitUtils.createService(MainModel.class)
                .uploadCompensateCardRecord(AppSharedPreferences.getInstance().getKidsCardSchoolId(), new Request.Builder()
                        .withParam("userIcon", baseKidsCardTakePhoto.getPhotoPath())
                        .withParam("cardRecordId", baseKidsCardTakePhoto.getRecordId())
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(String result) {
                        DBKidsCardHelp.getInstance(AppContext.getContext()).deleteFailePhoto(baseKidsCardTakePhoto);
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                    }
                });
    }


    /**
     * 处理倒计时重启设备
     */
    private void handleReboot() {
        Calendar calendar = Calendar.getInstance();
        int calendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinute = calendar.get(Calendar.MINUTE);
        //凌晨两点重启设备
        if (2 == calendarHour && 0 == calendarMinute) {
            HomeUtils.reboot();
        }
    }

    private void stopSchedule() {
        if (null != executorService) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    public void undispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSchedule();
        undispose();
    }
}
