package com.cicada.kidscard.business.home.presenter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;

import com.cicada.kidscard.R;
import com.cicada.kidscard.base.BasePresenter;
import com.cicada.kidscard.business.home.domain.event.EmsHasCardCount;
import com.cicada.kidscard.business.home.model.ContactModel;
import com.cicada.kidscard.business.home.view.IHomeView;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.hardware.Bluetooth.CicadaBleBluetooth;
import com.cicada.kidscard.hardware.camera.CameraTakeManager;
import com.cicada.kidscard.hardware.serialport.CardSerialPort;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.service.AssistantCardService;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsCardRecord;
import com.cicada.kidscard.storage.db.model.BaseTemperatureInfo;
import com.cicada.kidscard.storage.db.model.ChildInfo;
import com.cicada.kidscard.storage.db.model.TeacherInfo;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.ParseTemperature;
import com.cicada.kidscard.utils.Preconditions;
import com.cicada.kidscard.utils.RxTimer;
import com.cicada.kidscard.utils.ToastUtils;
import com.cicada.kidscard.voice.IflytekVoice;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName: MainPresenter
 * @Description:
 * @Author: liuyun
 * @CreateDate: 2021/9/10 10:29
 * @UpdateUser: liuyun
 * @UpdateDate: 2021/9/10 10:29
 */
public class MainPresenter extends BasePresenter {
    private final Context mContext;
    private IHomeView homeView;
    private boolean isQueryingCardInfo = false;
    private long lastCardTime = 0;
    private final long SAME_CARD_INTERVAL_TIME = 5 * 1000;
    private String lastCardNumber = "";
    private ServiceConnection serviceConnection;

    public MainPresenter(Context mContext, IHomeView homeView) {
        this.mContext = mContext;
        this.homeView = homeView;
    }


    public void bindService() {
        if (null == serviceConnection) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    AssistantCardService.MyBinder myBinder = ((AssistantCardService.MyBinder) binder);
                    LogUtils.d("service已连接====", myBinder.getServiceName());
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            };
            mContext.bindService(new Intent(mContext, AssistantCardService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void beforeDestroy(Handler mHandler) {
        mHandler.removeMessages(Constants.SHOW_CARD_NO);
        mHandler.removeMessages(Constants.SHOW_CARD_USER_INFO);
        mHandler.removeMessages(Constants.CLEAR_CARD_USER_INFO);
        mHandler.removeMessages(Constants.UPDATE_NET_SPEED);
        mHandler.removeMessages(Constants.UPDATE_NET_STATUS);
        mHandler.removeMessages(Constants.SHOW_BANNER);
        mHandler.removeMessages(Constants.CLOSE_BANNER);
        mHandler.removeMessages(Constants.CHECK_CAMERA_INIT_STATUS);
        mHandler.removeMessages(Constants.UPDATE_UN_UPLOAD_COUNT);
        mHandler.removeMessages(Constants.NET_CHECK);
        mHandler.removeMessages(Constants.TEMPERATURE_VIEW_SHOW);
        mHandler.removeMessages(Constants.TEMPERATURE_VIEW_HIDE);
        if (serviceConnection != null) {
            mContext.unbindService(serviceConnection);
            serviceConnection = null;
        }
        undispose();

        CameraTakeManager.getInstance().destroy();
        CicadaBleBluetooth.getInstance().onDestroy();
        CardSerialPort.getInstance().destroyCardSerialPort();
        RxTimer.getInstance().cancelTimer();
        IflytekVoice.getInstance().stopVoice();
    }

    public void handleCardInfo(final BaseKidsCardChildInfo baseKidsCardChildInfo) {
        String userName = "", className = "", userIcon = "";
        if (Preconditions.isNotEmpty(baseKidsCardChildInfo)) {
            if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getChildInfo())) {
                ChildInfo childInfo = baseKidsCardChildInfo.getChildInfo();
                if (AppSharedPreferences.getInstance().getKidsCardVoiceNameStatus()) {
                    StringBuilder sb = new StringBuilder();
                    if (Preconditions.isNotEmpty(childInfo.getChildNamePinyin())) {
                        sb.append(childInfo.getChildClassName()).append(childInfo.getChildNamePinyin());
                    } else {
                        sb.append(childInfo.getChildClassName()).append(childInfo.getChildName());
                    }
                    HomeUtils.playMessage(sb.toString());
                } else {
                    HomeUtils.playMessage(mContext.getResources().getString(R.string.card_success));
                }
                userName = childInfo.getChildName();
                className = childInfo.getChildClassName();
                userIcon = childInfo.getChildIcon();
            } else if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getTeacherInfo())) {
                TeacherInfo teacherInfo = baseKidsCardChildInfo.getTeacherInfo();
                if (AppSharedPreferences.getInstance().getKidsCardVoiceNameStatus()) {
                    HomeUtils.playMessage(teacherInfo.getUserName());
                } else {
                    HomeUtils.playMessage(mContext.getResources().getString(R.string.card_success));
                }
                userName = teacherInfo.getUserName();
                userIcon = teacherInfo.getUserIcon();
            }
        }
        if (null != homeView) {
            homeView.showCardUserInfo(userName, className, userIcon);
        }
    }


    /**
     * 保存刷卡记录
     *
     * @param schoolId
     */
    public void saveCardRecord(final BaseKidsCardChildInfo baseKidsCardChildInfo, String schoolId, String filePath, final String temperature) {
        if (Preconditions.isNotEmpty(baseKidsCardChildInfo)) {
            BaseKidsCardRecord baseKidsCardRecord = new BaseKidsCardRecord();
            baseKidsCardRecord.setCardNumber(baseKidsCardChildInfo.getCardNumber());
            if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getChildInfo())) {  //孩子刷卡
                baseKidsCardRecord.setTargetId(baseKidsCardChildInfo.getChildInfo().getChildId());
                baseKidsCardRecord.setTargetName(baseKidsCardChildInfo.getChildInfo().getChildName());
                baseKidsCardRecord.setClassId(baseKidsCardChildInfo.getChildInfo().getChildClassId());
            } else if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getTeacherInfo())) {//老师刷卡
                baseKidsCardRecord.setTargetId(baseKidsCardChildInfo.getTeacherInfo().getUserId());
                baseKidsCardRecord.setTargetName(baseKidsCardChildInfo.getTeacherInfo().getUserName());
            }
            //0-正常 1-高温 -1-异常
            int tempStatus = ParseTemperature.getTemperatureStatus(temperature);
            if (-1 != tempStatus) {
                baseKidsCardRecord.setTemperature(Float.valueOf(temperature));
            }
            baseKidsCardRecord.setSchoolId(schoolId);
            baseKidsCardRecord.setSchoolState("");
            baseKidsCardRecord.setRequestDate(AppSharedPreferences.getInstance().getLocalReal());
            baseKidsCardRecord.setLocalId(String.valueOf(System.currentTimeMillis()));
            baseKidsCardRecord.setIsTeacherCard(baseKidsCardChildInfo.getIsTeacherCard());
            baseKidsCardRecord.setAreaId("");
            baseKidsCardRecord.setUserIcon(filePath);
            DBKidsCardHelp.getInstance(mContext).saveKidsCardRecord(baseKidsCardRecord);
            if (!AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)) {
                EventBus.getDefault().post(new EmsHasCardCount());
            }
            if (-1 != tempStatus) {// 异常体温数据不上传
                handleTemperatureInfo(baseKidsCardChildInfo, temperature);
            }
        }
    }

    /**
     * 查询卡信息
     *
     * @param schoolId
     * @param cardNumber
     * @param activity
     */
    public void getCardInfo(String schoolId, String cardNumber, Activity activity) {
        BaseKidsCardChildInfo baseKidsCardChildInfo = DBKidsCardHelp.getInstance(mContext).findKidsCardChildInfo(cardNumber);
        if (Preconditions.isEmpty(baseKidsCardChildInfo)) {
            if (AppSharedPreferences.getInstance().getBooleanValue(Constants.NET_AVAILABLE, false)) {
                getSingleChildInfo(schoolId, cardNumber, activity);
            } else {
                if (null != homeView) {
                    homeView.getCardInfoSuccess(null);
                }
                ToastUtils.showToastImage(activity, activity.getString(R.string.app_exception_network_no), 0);
            }
        } else {
            if (null != homeView) {
                homeView.getCardInfoSuccess(baseKidsCardChildInfo);
            }
        }
    }


    /**
     * 获取单个刷卡孩子信息
     *
     * @param schoolId
     * @param activity
     */
    public void getSingleChildInfo(String schoolId, String cardNumber, Activity activity) {
        isQueryingCardInfo = true;
        RetrofitUtils.createService(ContactModel.class)
                .getSingleChildInfo(schoolId, new Request.Builder()
                        .withParam("schoolId", schoolId)
                        .withParam("cardNumber", cardNumber)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<BaseKidsCardChildInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToastImage(activity, mContext.getString(R.string.dialog_title_waiting), 0);
                            }
                        });
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(BaseKidsCardChildInfo baseKidsCardChildInfo) {
                        ToastUtils.cancel();
                        isQueryingCardInfo = false;
                        if (Preconditions.isNotEmpty(baseKidsCardChildInfo)) {
                            //childClassName拼接了年级，如果有班级别名（childCustomName）把别名赋值给班级名称
                            if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getChildInfo()) && Preconditions.isNotEmpty(baseKidsCardChildInfo.getChildInfo().getChildCustomName())) {
                                ChildInfo childInfo = baseKidsCardChildInfo.getChildInfo();
                                childInfo.setChildClassName(childInfo.getChildCustomName());
                                baseKidsCardChildInfo.setChildInfo(childInfo);
                            }
                            if (null != homeView) {
                                homeView.getCardInfoSuccess(baseKidsCardChildInfo);
                            }
                            DBKidsCardHelp.getInstance(mContext).saveKidsCardChildInfo(baseKidsCardChildInfo);
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        isQueryingCardInfo = false;
                        if (!TextUtils.isEmpty(errorCode) && errorCode.length() == 8) {
                            HomeUtils.playMessage(errorMessage);
                            ToastUtils.showToastImage(activity, errorMessage, 0);
                        } else {
                            ToastUtils.cancel();
                        }
                        if (null != homeView) {
                            homeView.getCardInfoSuccess(null);
                        }
                    }
                });
    }

    /**
     * 是否处理本次刷卡信号
     *
     * @param curCardNumber
     * @return
     */
    public boolean handleCurCardMsg(String curCardNumber) {
        //同一张卡5秒重复刷卡-不处理
        if (lastCardNumber.equals(curCardNumber) && ((System.currentTimeMillis() - lastCardTime) <= SAME_CARD_INTERVAL_TIME)) {
            return false;
        }

        //正在查询卡信息-不处理
        if (isQueryingCardInfo) {
            return false;
        }
        //重置最后一次刷卡卡号和时间
        lastCardNumber = curCardNumber;
        lastCardTime = System.currentTimeMillis();
        return true;
    }


    /**
     * 体温语音播报内容
     *
     * @param temperature
     * @return
     */
    public String getTemperaturePlayMsg(String temperature) {
        String playMsg = "";
        //0-正常 1-高温 -1-异常
        int tempStatus = ParseTemperature.getTemperatureStatus(temperature);
        if (1 == tempStatus) {
            playMsg = mContext.getString(R.string.playmsg_temp_high);
        } else if (-1 == tempStatus) {
            playMsg = mContext.getString(R.string.playmsg_temp_exception1);
        } else {
            playMsg = mContext.getString(R.string.playmsg_temp_normal);
        }

        return playMsg;
    }

    private BaseTemperatureInfo getTemperatureInfo(final BaseKidsCardChildInfo baseKidsCardChildInfo, final String temperature) {
        BaseTemperatureInfo baseTemperatureInfo = null;
        if (Preconditions.isNotEmpty(baseKidsCardChildInfo)) {
            baseTemperatureInfo = new BaseTemperatureInfo();
            if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getChildInfo())) {  //孩子刷卡
                baseTemperatureInfo.setChildNo(baseKidsCardChildInfo.getChildInfo().getChildId());
                baseTemperatureInfo.setChildName(baseKidsCardChildInfo.getChildInfo().getChildName());
                baseTemperatureInfo.setClassName(baseKidsCardChildInfo.getChildInfo().getChildClassName());
                baseTemperatureInfo.setClassNo(baseKidsCardChildInfo.getChildInfo().getChildClassId());
                baseTemperatureInfo.setUserType(0);
            } else if (Preconditions.isNotEmpty(baseKidsCardChildInfo.getTeacherInfo())) {//老师刷卡
                baseTemperatureInfo.setUserNo(baseKidsCardChildInfo.getTeacherInfo().getUserId());
                baseTemperatureInfo.setUserName(baseKidsCardChildInfo.getTeacherInfo().getUserName());
                baseTemperatureInfo.setUserType(1);
            }
            baseTemperatureInfo.setMorningTemperature(temperature);
            baseTemperatureInfo.setCheckDateStr(AppSharedPreferences.getInstance().getLocalReal());
            baseTemperatureInfo.setLocalUniqueId(AppSharedPreferences.getInstance().getLocalReal() + "");
            baseTemperatureInfo.setSchoolNo(AppSharedPreferences.getInstance().getKidsCardSchoolId());
        }
        return baseTemperatureInfo;
    }

    public void handleTemperatureInfo(final BaseKidsCardChildInfo baseKidsCardChildInfo, final String temperature) {
        BaseTemperatureInfo temperatureInfo = getTemperatureInfo(baseKidsCardChildInfo, temperature);
        if (null != temperatureInfo) {
            DBKidsCardHelp.getInstance(mContext).saveTemperatureRecord(mContext, temperatureInfo);
        }
    }


    public void setFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

}
