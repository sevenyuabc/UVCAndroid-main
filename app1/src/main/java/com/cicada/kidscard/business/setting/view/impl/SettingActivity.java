package com.cicada.kidscard.business.setting.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cicada.kidscard.R;
import com.cicada.kidscard.base.BaseActivity;
import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.business.setting.domain.SchoolInfo;
import com.cicada.kidscard.business.setting.presenter.SettingPresenter;
import com.cicada.kidscard.business.setting.view.ISettingView;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.hardware.Bluetooth.BluetoothConstant;
import com.cicada.kidscard.hardware.Bluetooth.BluetoothDeviceInfo;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.DeviceUtils;
import com.cicada.kidscard.utils.NetworkUtils;
import com.cicada.kidscard.utils.ToastUtils;
import com.cicada.kidscard.utils.UiHelper;
import com.cicada.kidscard.view.dialog.CustomDialog;
import com.clj.fastble.BleManager;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxTool;

public class SettingActivity extends BaseActivity implements ISettingView {
    private Context mContext;
    private boolean boolVoiceStatus = false, boolVoiceNameStatus = false;
    private String schoolId = "";
    private SchoolInfo mSchoolInfo;
    private Button imageViewBack, buttonSubmit, buttonFindSchool, buttonNetworkSetting, system_setting, btn_unbind;
    private TextView textViewSchoolID, textViewSchoolName, textViewSchoolCity, textViewVersionName, textViewNetworkStatus, textviewShowMachineId, tv_bluetooth_device_status, tv_bluetooth_device_mac;
    private EditText editTextSchoolID;
    private RadioGroup radioGroupVoiceStatus, radioGroupVoiceNameStatus;
    private LinearLayout llVoiceName;
    /**
     * 是否已经成功设置学校信息
     */
    private boolean boolSettingSuccess = false;
    private SettingPresenter presenter;
    private SettingReceiver settingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        presenter = new SettingPresenter(this, this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothConstant.ACTION_BLUETOOTH_DEVICES_STATUS);
        settingReceiver = new SettingReceiver();
        registerReceiver(settingReceiver, intentFilter);
        if (AppContext.isIs32Device()) {
            setContentView(R.layout.activity_setting);
        } else if (AppContext.isIsYMDevice()) {
            setContentView(R.layout.activity_setting_ym);
        }
        findView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void findView() {
        boolVoiceStatus = AppSharedPreferences.getInstance().getKidsCardVoiceStatus();
        boolVoiceNameStatus = AppSharedPreferences.getInstance().getKidsCardVoiceNameStatus();
        mSchoolInfo = AppSharedPreferences.getInstance().getKidsCardSchoolInfo();
        if (mSchoolInfo != null) {
            schoolId = mSchoolInfo.getSchoolId();
        }
        initView();
        initSchoolInfo();
    }

    @Override
    protected void onResume() {
        if (textViewNetworkStatus != null) {
            textViewNetworkStatus.setText(NetworkUtils.isNetworkAvailable(mContext) ? "已连接" : "未连接");
        }
        super.onResume();
    }

    @Override
    protected void netChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textViewNetworkStatus != null) {
                    textViewNetworkStatus.setText(NetworkUtils.isNetworkAvailable(mContext) ? "已连接" : "未连接");
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        gotoBack();
    }

    private void gotoBack() {
        if (boolSettingSuccess) {
            SettingActivity.this.finish();
        } else {
            CustomDialog dialog = new CustomDialog.Builder(mContext).setMessage("您还没有绑定学校，刷卡功能将无法正常使用，确认返回？").setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            }).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SettingActivity.this.finish();
                }
            }).create();

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(settingReceiver);
    }

    private int titleClickCount = 0;

    private void initView() {
        (findViewById(R.id.buttonTitle)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (10 == titleClickCount) {
                    titleClickCount = 0;
                }
                titleClickCount++;
            }
        });

        tv_bluetooth_device_status = findViewById(R.id.tv_bluetooth_device_status);
        tv_bluetooth_device_mac = findViewById(R.id.tv_bluetooth_device_mac);
        btn_unbind = findViewById(R.id.btn_unbind);

        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBack.setOnClickListener(new ButtonClickEvent());

        buttonFindSchool = findViewById(R.id.buttonFindSchool);
        buttonFindSchool.setOnClickListener(new ButtonClickEvent());
        buttonNetworkSetting = findViewById(R.id.buttonNetworkSetting);
        buttonNetworkSetting.setOnClickListener(new ButtonClickEvent());
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new ButtonClickEvent());

        editTextSchoolID = findViewById(R.id.editTextSchoolID);
        textViewSchoolID = findViewById(R.id.textViewSchoolID);
        textViewSchoolName = findViewById(R.id.textViewSchoolName);
        textViewSchoolCity = findViewById(R.id.textViewSchoolCity);
        textViewVersionName = findViewById(R.id.textViewVersionName);
        String versonName = RxAppTool.getAppVersionName(mContext) + "（" + RxAppTool.getAppVersionCode(mContext) + "）";
        if (!AppContext.isRelease()) {
            versonName += "测试服务";
        }
        textViewVersionName.setText(versonName);

        textViewNetworkStatus = findViewById(R.id.textViewNetworkStatus);
        textViewNetworkStatus.setText(NetworkUtils.isNetworkAvailable(mContext) ? "已连接" : "未连接");

        system_setting = findViewById(R.id.system_setting);
        system_setting.setOnClickListener(new ButtonClickEvent());
        llVoiceName = findViewById(R.id.llVoiceName);
        textViewSchoolID.setText(schoolId);
        if (AppContext.isIs32Device()) {
            TextView tv_machine_mac = findViewById(R.id.show_machine_mac);
            tv_machine_mac.setText(DeviceUtils.getMacAddress(mContext));
        }

        radioGroupVoiceStatus = findViewById(R.id.radioGroupVoiceStatus);
        radioGroupVoiceStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (R.id.voice_open == arg1) {
                    boolVoiceStatus = true;
                    llVoiceName.setVisibility(View.VISIBLE);
                } else if (R.id.voice_close == arg1) {
                    boolVoiceStatus = false;
                    llVoiceName.setVisibility(View.GONE);
                }
                AppSharedPreferences.getInstance().setKidsCardVoiceStatus(boolVoiceStatus);
            }
        });
        if (boolVoiceStatus) {
            llVoiceName.setVisibility(View.VISIBLE);
            radioGroupVoiceStatus.check(R.id.voice_open);
        } else {
            llVoiceName.setVisibility(View.GONE);
            radioGroupVoiceStatus.check(R.id.voice_close);
        }


        radioGroupVoiceNameStatus = findViewById(R.id.radioGroupVoiceNameStatus);
        radioGroupVoiceNameStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (R.id.nameOpen == arg1) {
                    boolVoiceNameStatus = true;
                } else if (R.id.nameClose == arg1) {
                    boolVoiceNameStatus = false;
                }
                AppSharedPreferences.getInstance().setKidsCardVoiceNameStatus(boolVoiceNameStatus);
            }
        });
        if (boolVoiceNameStatus) {
            radioGroupVoiceNameStatus.check(R.id.nameOpen);
        } else {
            radioGroupVoiceNameStatus.check(R.id.nameClose);
        }
        if (null != btn_unbind) {
            //解绑蓝牙
            btn_unbind.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    BleManager.getInstance().disconnectAllDevice();
                    btn_unbind.setVisibility(View.GONE);
                    AppSharedPreferences.getInstance().setBluetoothDeviceInfo(null);
                    updateBluetoothDeviceInfo();
                }
            });
        }

        updateBluetoothDeviceInfo();
        clearAllData();

        textviewShowMachineId = findViewById(R.id.show_machine_id);
        showMachineId();
    }

    public class SettingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothConstant.ACTION_BLUETOOTH_DEVICES_STATUS.equals(action)) {
                updateBluetoothDeviceInfo();
            }
        }
    }

    /**
     * 蓝牙设备信息
     */
    private void updateBluetoothDeviceInfo() {
        if (AppContext.isIs32Device()) {
            BluetoothDeviceInfo deviceInfo = AppSharedPreferences.getInstance().getBluetoothDeviceInfo();
            if (null == deviceInfo) {
                btn_unbind.setVisibility(View.GONE);
                tv_bluetooth_device_mac.setText("");
                tv_bluetooth_device_status.setText(R.string.un_bind);
            } else {
                tv_bluetooth_device_mac.setText(deviceInfo.getMac());
                tv_bluetooth_device_status.setText(BleManager.getInstance().isConnected(deviceInfo.getMac()) ? R.string.connected : R.string.disconnected);
                btn_unbind.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showMachineId() {
        textviewShowMachineId.setText(HomeUtils.getSn(mContext));
    }


    private class ButtonClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (RxTool.isFastClick(1000)) {
                return;
            }
            if (v == imageViewBack) {
                gotoBack();
            } else if (v == buttonFindSchool) {
                String strSchoolId = editTextSchoolID.getText().toString().trim();
                if (!TextUtils.isEmpty(strSchoolId)) {
                    try {
                        schoolId = strSchoolId;
                        presenter.findSchoolInfoById(schoolId, true);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtils.showToastImage(mContext, "请输入正确的学校编号!", 0);
                    }
                } else {
                    ToastUtils.showToastImage(mContext, "请输入正确的学校编号!", 0);
                }
            } else if (v == buttonNetworkSetting) {
                NetworkUtils.gotoWifiSetting(mContext);
            } else if (v == buttonSubmit) {
                bindSchool();
            } else if (v == system_setting) {
                UiHelper.gotoSetting(mContext);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        buttonFindSchool.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    buttonFindSchool.setTextColor(SettingActivity.this.getResources().getColor(R.color.white));
                } else {
                    buttonFindSchool.setTextColor(SettingActivity.this.getResources().getColor(R.color.text_color_blue));
                }
            }
        });
        system_setting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    system_setting.setTextColor(SettingActivity.this.getResources().getColor(R.color.white));
                } else {
                    system_setting.setTextColor(SettingActivity.this.getResources().getColor(R.color.text_color_blue));
                }
            }
        });
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存相关设置
     */
    private void bindSchool() {
        if (TextUtils.isEmpty(textViewSchoolID.getText().toString())) {
            ToastUtils.showToastImage(mContext, "请先查询学校信息!", 0);
            return;
        }
        presenter.saveOrUpdateDevice(mSchoolInfo);
    }


    private void clearAllData() {
        textViewSchoolID.setText("");
        textViewSchoolName.setText("");
        textViewSchoolCity.setText("");
    }


    private void initSchoolInfo() {
        if (mSchoolInfo != null) {
            loadSchoolInfo();
            if (TextUtils.isEmpty(mSchoolInfo.getSource())) {
                presenter.findSchoolInfoById(schoolId, true);
            }
            boolSettingSuccess = true;
        } else {
            clearSchoolInfo();
        }
    }

    private void loadSchoolInfo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                schoolId = mSchoolInfo.getSchoolId();
                textViewSchoolID.setText("" + mSchoolInfo.getSchoolId());
                textViewSchoolName.setText(mSchoolInfo.getSchoolName());
                textViewSchoolCity.setText(mSchoolInfo.getCityName());
            }
        });
    }

    private void clearSchoolInfo() {
        boolSettingSuccess = false;
        schoolId = "";
        textViewSchoolID.setText("");
        textViewSchoolName.setText("");
        textViewSchoolCity.setText("");
    }

    /**
     * 加载学校的相关信息
     */
    private void loadSchoolData() {
        if (mSchoolInfo != null) {
            loadSchoolInfo();
        } else {
            clearSchoolInfo();
        }
    }

    @Override
    public void findSchoolByIdResult(SchoolInfo result) {
        mSchoolInfo = result;
        loadSchoolData();
    }

    @Override
    public void bindSchoolSuccess() {
        boolSettingSuccess = true;
        SettingActivity.this.finish();
    }
}
