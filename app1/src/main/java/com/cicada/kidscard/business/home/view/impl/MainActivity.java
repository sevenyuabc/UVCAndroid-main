package com.cicada.kidscard.business.home.view.impl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cicada.kidscard.R;
import com.cicada.kidscard.app.AppManager;
import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.base.BaseActivity;
import com.cicada.kidscard.business.home.domain.event.EmsBannerDataUpdated;
import com.cicada.kidscard.business.home.domain.event.EmsHasCardCount;
import com.cicada.kidscard.business.home.domain.event.EmsHasSchool;
import com.cicada.kidscard.business.home.domain.event.EmsShowAD;
import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.business.home.presenter.MainPresenter;
import com.cicada.kidscard.business.home.view.IHomeView;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.hardware.Bluetooth.BluetoothConstant;
import com.cicada.kidscard.hardware.Bluetooth.CicadaBleBluetooth;
import com.cicada.kidscard.hardware.Bluetooth.IBluetoothView;
import com.cicada.kidscard.hardware.camera.CameraPreviewCallBack;
import com.cicada.kidscard.hardware.camera.CameraTakeManager;
import com.cicada.kidscard.hardware.camera.CameraTakePhotoCallBack;
import com.cicada.kidscard.hardware.serialport.CardSerialPort;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.Preconditions;
import com.cicada.kidscard.utils.UiHelper;
import com.cicada.kidscard.utils.XjjUtils;
import com.cicada.kidscard.view.CardInfoView;
import com.cicada.kidscard.view.TemperatureView;
import com.cicada.kidscard.view.banner.BannerView;
import com.tamsiree.rxtool.RxTool;
import com.tamsiree.rxtool.interfaces.OnSimpleListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;

/**
 * 首页识别业务逻辑
 */
public class MainActivity extends BaseActivity implements IHomeView, CameraTakePhotoCallBack, IBluetoothView, CardSerialPort.ReadSerialDataCallBack {

    private ImageView ivSetting;
    private TextView titleTv;
    private SurfaceView cameraSurfaceView;
    private MainPresenter mainPresenter;
    private String schoolId = "", temperature = "";
    private BaseKidsCardChildInfo baseKidsCardChildInfo;
    private BannerView bannerView;
    private FrameLayout flSurfaceView;
    private boolean cameraPreviewInitFinished = false;
    private CardInfoView cardInfoView;
    private TemperatureView temperatureView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppContext.isIs32Device()) {
            setContentView(R.layout.activity_main_layout);
        } else if (AppContext.isIsYMDevice()) {
            setContentView(R.layout.activity_main_layout_ym);
        }
        bindView();
        initData();
        EventBus.getDefault().register(this);
    }

    private void bindView() {
        titleTv = findViewById(R.id.tvTitle);
        cameraSurfaceView = findViewById(R.id.surfaceView);
        flSurfaceView = findViewById(R.id.fl_surface_view);
        temperatureView = findViewById(R.id.temperatureView);
        cardInfoView = findViewById(R.id.card_info_view);
        bannerView = findViewById(R.id.bannerView);
        ivSetting = findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!RxTool.isFastClick(1000)) {
                    mainPresenter.setFocus(titleTv);
                    HomeUtils.gotoSettingActivity(MainActivity.this);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiHelper.hideBottomNav(this);
        mHandler.sendEmptyMessage(Constants.CLOSE_BANNER);
        startCameraPreview();
        HomeUtils.setVoiceMax(MainActivity.this);
    }


    private void initData() {
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.bindService();
        if (AppContext.isIs32Device()) {
            CicadaBleBluetooth.getInstance().setIBluetoothTemperatureListener(this);
            CicadaBleBluetooth.getInstance().init();
        }
        CardSerialPort.getInstance().openSerialPort();
        CardSerialPort.getInstance().setReadSerialDataCallBack(this);
        showSchoolInfo();
    }


    /**
     * 打开摄像预览
     */
    private void startCameraPreview() {
        cameraPreviewInitFinished = false;
        CameraTakeManager.getInstance().releaseCamera();
        CameraTakeManager.getInstance().startPreview(MainActivity.this, cameraSurfaceView, this, new CameraPreviewCallBack() {
            @Override
            public void previewInitFinished() {
                cameraPreviewInitFinished = true;
            }
        });
        mHandler.removeMessages(Constants.CHECK_CAMERA_INIT_STATUS);
        mHandler.sendEmptyMessageDelayed(Constants.CHECK_CAMERA_INIT_STATUS, 6000);
    }

    @Override
    public void getCardInfoSuccess(BaseKidsCardChildInfo cardChildInfo) {
        if (Preconditions.isNotEmpty(cardChildInfo)) {
            Message msg = new Message();
            msg.what = Constants.SHOW_CARD_USER_INFO;
            msg.obj = cardChildInfo;
            mHandler.sendMessage(msg);
            mHandler.sendEmptyMessage(Constants.SHOW_CARD_USER_INFO);
            CameraTakeManager.getInstance().takePhoto();
        } else {
            mHandler.removeMessages(Constants.CLEAR_CARD_USER_INFO);
            mHandler.sendEmptyMessageDelayed(Constants.CLEAR_CARD_USER_INFO, 5000);
        }
    }

    @Override
    public void showCardUserInfo(String userName, String className, String userIcon) {
        mHandler.removeMessages(Constants.CLEAR_CARD_USER_INFO);
        cardInfoView.showUserInfo(userName, className, userIcon, this);
        XjjUtils.openDoor();
        mHandler.sendEmptyMessageDelayed(Constants.CLEAR_CARD_USER_INFO, 5000);
    }

    private void showSchoolInfo() {
        schoolId = AppSharedPreferences.getInstance().getKidsCardSchoolId();
        String schoolName = AppSharedPreferences.getInstance().getKidsCardSchoolName();
        titleTv.setText(schoolName);
        if (Preconditions.isNotEmpty(schoolName)) {
            mainPresenter.setFocus(titleTv);
            mHandler.sendEmptyMessage(Constants.UPDATE_UN_UPLOAD_COUNT);
        } else {
            titleTv.setText("");
        }
        if (Preconditions.isEmpty(schoolId)) {
            RxTool.delayToDo(2000, new OnSimpleListener() {
                @Override
                public void doSomething() {
                    HomeUtils.gotoSettingActivity(MainActivity.this);
                }
            });
        }
    }

    @Override
    public void onTakePictureComplete(Bitmap bitmap, String filePath) {
        if (Preconditions.isNotEmpty(baseKidsCardChildInfo)) {
            mainPresenter.saveCardRecord(baseKidsCardChildInfo, schoolId, filePath, temperature);
            temperature = "";
        }
    }

    /**
     * 显示广告
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasEmsShowADNotify(EmsShowAD msg) {
        if (Preconditions.isNotEmpty(schoolId)) {
            mHandler.sendEmptyMessage(Constants.SHOW_BANNER);
        }
    }

    @Override
    public void onReadSerialData(String data) {
        if (!(AppManager.getInstance().currentActivity() instanceof MainActivity)) {
            return;
        }
        String curCardNumber = data;
        //如果banner正在显示，先关闭banner
        mHandler.sendEmptyMessage(Constants.CLOSE_BANNER);
        if (Preconditions.isNotEmpty(schoolId) && mainPresenter.handleCurCardMsg(curCardNumber)) {
            Message message = new Message();
            message.what = Constants.SHOW_CARD_NO;
            message.obj = curCardNumber;
            mHandler.sendMessage(message);
            mainPresenter.getCardInfo(schoolId, curCardNumber, this);
        }
    }

    /**
     * 学校绑定/解绑
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void schoolBindNotify(EmsHasSchool msg) {
        showSchoolInfo();
    }

    /**
     * 本地刷卡记录变化
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void hasEmsHasCardCountNotify(EmsHasCardCount msg) {
        mHandler.sendEmptyMessage(Constants.UPDATE_UN_UPLOAD_COUNT);
    }

    /**
     * 轮播图更新
     *
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBannerNotify(EmsBannerDataUpdated msg) {
        if (null != bannerView) {
            if (bannerView.getVisibility() == View.VISIBLE) {
                bannerView.refreshData();
            }
        }
    }

    /**
     * banner隐藏（未绑定学校时不会显示banner）
     */
    private void closeBanner() {
        if (null != bannerView) {
            MyApplication.getInstance().updateLastVerifyTime();
            if (bannerView.getVisibility() == View.VISIBLE) {
                bannerView.stopBanner();
                if (Preconditions.isNotEmpty(schoolId)) {
                    flSurfaceView.setVisibility(View.VISIBLE);
                    CameraTakeManager.getInstance().onResume();
                }
                bannerView.setVisibility(View.GONE);
            }
            UiHelper.hideBottomNav(this);
        }
    }

    /**
     * banner显示（未绑定学校时不会显示banner）
     */
    private void showBanner() {
        if (null != bannerView) {
            temperature = "";
            if (bannerView.getVisibility() == View.GONE) {
                CameraTakeManager.getInstance().onPause();
                flSurfaceView.setVisibility(View.INVISIBLE);
                bannerView.setVisibility(View.VISIBLE);
                bannerView.startBanner();
            }
        }
    }

    @Override
    protected void handleSelfMessage(Message msg) {
        super.handleSelfMessage(msg);
        switch (msg.what) {
            case Constants.SHOW_CARD_NO://显示卡号
                String cardNumber = msg.obj != null ? msg.obj.toString() : "";
                if (Preconditions.isNotEmpty(cardNumber)) {
                    cardInfoView.showCardNo(cardNumber);
                }
                break;
            case Constants.SHOW_CARD_USER_INFO://显示卡对应的用户信息
                final BaseKidsCardChildInfo childInfo = (BaseKidsCardChildInfo) msg.obj;
                if (Preconditions.isNotEmpty(childInfo)) {
                    mainPresenter.handleCardInfo(childInfo);
                    baseKidsCardChildInfo = childInfo;
                }
                break;
            case Constants.CLEAR_CARD_USER_INFO://清空用户信息
                cardInfoView.clearCardInfo(MainActivity.this);
                break;
            case Constants.SHOW_BANNER://显示广告
                showBanner();
                break;
            case Constants.CLOSE_BANNER://关闭广告
                closeBanner();
                break;
            case Constants.CHECK_CAMERA_INIT_STATUS://6秒后检查摄像头初始化状态：初始化失败-重启应用
                if (!cameraPreviewInitFinished) {
                    HomeUtils.relaunch(MainActivity.this);
                }
                break;
            case Constants.TEMPERATURE_VIEW_SHOW:
                //显示体温数据以及同步数据到后台
                String temper = msg.obj != null ? msg.obj.toString() : "";
                if (Preconditions.isNotEmpty(temper)) {
                    closeBanner();
                    temperature = temper;
                    MyApplication.getInstance().updateLastVerifyTime();
                    HomeUtils.playMessage(mainPresenter.getTemperaturePlayMsg(temper));
                    temperatureView.setVisibility(View.VISIBLE);
                    temperatureView.setTemperature(temper);
                    mHandler.sendEmptyMessageDelayed(Constants.TEMPERATURE_VIEW_HIDE, 3000);
                }
                break;
            case Constants.TEMPERATURE_VIEW_HIDE:
                temperatureView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBluetoothDataType(int code, String temperature) {
        switch (code) {
            case BluetoothConstant.BLUETOOTH_DISCONNECT:
                HomeUtils.playMessage(getResources().getString(R.string.templeature_device_disconnect));
                break;
            case BluetoothConstant.BLUETOOTH_CONNECT_ERROR:
                break;
            case BluetoothConstant.BLUETOOTH_CONNECTED:
                HomeUtils.playMessage(getResources().getString(R.string.templeature_device_connect));
                break;
            case BluetoothConstant.BLUETOOTH_DATA_SUCCESS:
                if ((AppManager.getInstance().currentActivity() instanceof MainActivity)) {
                    //显示体温数据
                    mHandler.removeMessages(Constants.TEMPERATURE_VIEW_HIDE);
                    mHandler.removeMessages(Constants.TEMPERATURE_VIEW_SHOW);
                    Message msg = new Message();
                    msg.what = Constants.TEMPERATURE_VIEW_SHOW;
                    msg.obj = temperature;
                    mHandler.sendMessage(msg);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (null != bannerView && bannerView.getVisibility() == View.VISIBLE) {
            mHandler.sendEmptyMessage(Constants.CLOSE_BANNER);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mainPresenter.beforeDestroy(mHandler);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
