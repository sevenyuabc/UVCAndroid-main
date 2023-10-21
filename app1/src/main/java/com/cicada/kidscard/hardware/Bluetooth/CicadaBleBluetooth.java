package com.cicada.kidscard.hardware.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.ParcelUuid;
import android.text.TextUtils;

import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.ByteUtils;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.ParseTemperature;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ble蓝牙连接体温枪
 * <p>
 * Create time: 2020-02-14 14:11
 *
 * @author liuyun.
 */
public class CicadaBleBluetooth {
    private IBluetoothView listener = null;
    private static CicadaBleBluetooth instance;
    private static final String TAG = "==yy== temperature";

    private BluetoothDeviceInfo blueToothDeviceInfo;
    private int connectStatus;
    private BleDevice mBleDevice;
    private ScheduledExecutorService executorService;

    public static CicadaBleBluetooth getInstance() {
        if (instance == null) {
            instance = new CicadaBleBluetooth();
        }
        return instance;
    }

    public void init() {
        BleManager.getInstance()
                .enableLog(!AppContext.isRelease())
                .setReConnectCount(1, 5000)//连接失败重连次数、重连间隔
                .setConnectOverTime(10000)//连接超时时间 10s
                .setOperateTimeout(5000);//操作超时时间 5s
        initSchedule();
    }

    /**
     * 设置扫描规则
     */
    private void setScanRule() {
        String[] uuids;
        String str_uuid = "";
        if (TextUtils.isEmpty(str_uuid)) {
            uuids = null;
        } else {
            uuids = str_uuid.split(",");
        }
        UUID[] serviceUuids = null;
        if (uuids != null && uuids.length > 0) {
            serviceUuids = new UUID[uuids.length];
            for (int i = 0; i < uuids.length; i++) {
                String name = uuids[i];
                String[] components = name.split("-");
                if (components.length != 5) {
                    serviceUuids[i] = null;
                } else {
                    serviceUuids[i] = UUID.fromString(uuids[i]);
                }
            }
        }

        String[] names = {BluetoothConstant.LOCK_NAME_BF, BluetoothConstant.LOCK_NAME_COMPER, BluetoothConstant.LOCK_NAME_HUAAN};

        String mac = "";
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选  多个uuid搜索规则是与的关系，所以暂时不能使用该规则去匹配多种设备
                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
                .setAutoConnect(true)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }


    /**
     * 心跳检测蓝牙设备状态
     */
    public void heartBeatHandle() {
        //目前只有魔点G2设备支持蓝牙枪
        if (null == instance) {
            return;
        }

        //如果蓝牙未打开：打开蓝牙，等待下次心跳重新连接
        if (!BleManager.getInstance().isBlueEnable()) {
            BleManager.getInstance().enableBluetooth();
            return;
        }

        blueToothDeviceInfo = AppSharedPreferences.getInstance().getBluetoothDeviceInfo();
        // 连接过设备: 设备状态为未连接-连接
        //未连接过设备：扫描
        if (null != blueToothDeviceInfo) {
            LogUtils.d(TAG, "isConnected：" + BleManager.getInstance().isConnected(blueToothDeviceInfo.getMac()) + " customState:" + connectStatus);
            if (BluetoothProfile.STATE_DISCONNECTED == connectStatus) {
                if (!TextUtils.isEmpty(blueToothDeviceInfo.getMac())){
                    connect(blueToothDeviceInfo.getMac());
                }
            }
        } else {
            startScan();
        }
    }

    /**
     * 扫描设备
     */
    public void startScan() {
        if (!AppSharedPreferences.getInstance().getBlueOpen() && BleScanState.STATE_SCANNING == BleManager.getInstance().getScanSate()) {
            return;
        }
        setScanRule();
        LogUtils.d(TAG, "startScan");
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                connectStatus = BluetoothProfile.STATE_CONNECTING;
                LogUtils.d(TAG, "onScanStarted " + success);
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                LogUtils.d(TAG, "onScanning " + bleDevice.getName());
                if (isTargetServiceUUIDDevice(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice.getMac());
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                LogUtils.d(TAG, "onScanFinished" + scanResultList);
            }
        });
    }

    /**
     * 连接设备
     *
     * @param mac
     */
    private void connect(String mac) {
        LogUtils.d(TAG, "connect ing");
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                connectStatus = BluetoothProfile.STATE_CONNECTING;
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                LogUtils.d(TAG, "onConnectFail " + exception.getDescription());
                connectStatus = BluetoothProfile.STATE_DISCONNECTED;
                distributeState(BluetoothConstant.BLUETOOTH_CONNECT_ERROR, "");
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                LogUtils.d(TAG, "onConnectSuccess status " + status);
                connectStatus = BluetoothProfile.STATE_CONNECTED;
                startNotify(bleDevice);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                distributeState(BluetoothConstant.BLUETOOTH_DISCONNECT, "");
                connectStatus = BluetoothProfile.STATE_DISCONNECTED;
                LogUtils.d(TAG, "onDisConnected status:" + status);
            }
        });
    }

    /**
     * 开启通知
     *
     * @param bleDevice
     */
    private void startNotify(final BleDevice bleDevice) {
        String serviceUUID = "", characteristicNotifyUUID = "";
        String name = bleDevice.getName();
        if (TextUtils.isEmpty(name)) {
            name = blueToothDeviceInfo.getName();
            LogUtils.d(TAG, "bleDevice name empty:" + name);
        }
        if (name.contains(BluetoothConstant.LOCK_NAME_BF)) {
            serviceUUID = BluetoothConstant.SERVICE_UUID_BF;
            characteristicNotifyUUID = BluetoothConstant.NOTIFY_UUID_BF;
        } else if (name.contains(BluetoothConstant.LOCK_NAME_COMPER)) {
            serviceUUID = BluetoothConstant.SERVICE_UUID_COMPER;
            characteristicNotifyUUID = BluetoothConstant.NOTIFY_UUID_COMPER;
        } else if (name.contains(BluetoothConstant.LOCK_NAME_HUAAN)) {
            serviceUUID = BluetoothConstant.SERVICE_UUID_HUAAN;
            characteristicNotifyUUID = BluetoothConstant.NOTIFY_UUID_HUAAN;
        }
        mBleDevice = bleDevice;
        BleManager.getInstance().notify(
                bleDevice, serviceUUID, characteristicNotifyUUID,
                new BleNotifyCallback() {

                    @Override
                    public void onNotifySuccess() {
                        LogUtils.d(TAG, "onNotifySuccess");
                        distributeState(BluetoothConstant.BLUETOOTH_CONNECTED, "");
                        connectStatus = BluetoothProfile.STATE_CONNECTED;
                        if (!TextUtils.isEmpty(bleDevice.getName())) {
                            if (null == blueToothDeviceInfo) {
                                blueToothDeviceInfo = new BluetoothDeviceInfo();
                            }
                            blueToothDeviceInfo.setMac(bleDevice.getMac());
                            blueToothDeviceInfo.setName(bleDevice.getName());
                            AppSharedPreferences.getInstance().setBluetoothDeviceInfo(blueToothDeviceInfo);
                        }
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        LogUtils.d(TAG, "onNotifyFailure");
                        connectStatus = BluetoothProfile.STATE_DISCONNECTED;
                        distributeState(BluetoothConstant.BLUETOOTH_CONNECT_ERROR, "");
                    }

                    @Override
                    public void onCharacteristicChanged(final byte[] data) {
                        String temp = "";
                        LogUtils.d(TAG, "收到数据啦：" + ByteUtils.Bytes2HexString(data));
                        if (blueToothDeviceInfo.getName().contains(BluetoothConstant.LOCK_NAME_COMPER)) {
                            temp = ParseTemperature.getComperTemperature(data);
                        } else if (blueToothDeviceInfo.getName().contains(BluetoothConstant.LOCK_NAME_BF)) {
                            temp = ParseTemperature.getTemperature(data);
                        } else if (blueToothDeviceInfo.getName().contains(BluetoothConstant.LOCK_NAME_HUAAN)) {
                            temp = String.valueOf(ParseTemperature.getHuaAnTemperature(data));
                        }
                        LogUtils.d(TAG, "收到数据啦：" + temp);
                        connectStatus = BluetoothProfile.STATE_CONNECTED;
                        distributeState(BluetoothConstant.BLUETOOTH_DATA_SUCCESS, temp);
                    }
                });
    }


    /**
     * 分发蓝牙设备信息
     *
     * @param state
     * @param data
     */
    private void distributeState(int state, String data) {
        AppContext.getContext().sendBroadcast(new Intent(BluetoothConstant.ACTION_BLUETOOTH_DEVICES_STATUS));
        if (null != listener) {
            listener.onBluetoothDataType(state, data);
        }
    }

    /**
     * 设备是否是目标设备
     *
     * @param device
     * @return
     */
    private boolean isTargetServiceUUIDDevice(BleDevice device) {
        if (null == device) {
            return false;
        }
        boolean isTargetServiceUUIDDevice = false;
        ScanRecordUtils scanRecordUtils = ScanRecordUtils.parseFromBytes(device.getScanRecord());
        List<ParcelUuid> serviceUuids = scanRecordUtils.getServiceUuids();
        if (null == serviceUuids) {
            return false;
        }
        for (ParcelUuid uuid : serviceUuids) {
            String targetDeviceUUIDStr = uuid.getUuid().toString();
            LogUtils.d(TAG, "targetDeviceUUIDStr:" + targetDeviceUUIDStr);
            if (BluetoothConstant.SERVICE_UUID_COMPER.equals(targetDeviceUUIDStr) ||
                    BluetoothConstant.SERVICE_UUID_BF.equals(targetDeviceUUIDStr)) {
                isTargetServiceUUIDDevice = true;
                break;
            }
        }
        return isTargetServiceUUIDDevice;
    }


    public BleDevice getBleDevice(String mac) {
        BluetoothDevice bluetoothDevice = BleManager.getInstance().getBluetoothAdapter().getRemoteDevice(mac);
        BleDevice bleDevice = new BleDevice(bluetoothDevice);
        return bleDevice;
    }

    public CicadaBleBluetooth setIBluetoothTemperatureListener(
            IBluetoothView listener) {
        this.listener = listener;
        return this;
    }


    public void onDestroy() {
        connectStatus = BluetoothProfile.STATE_DISCONNECTED;
        stopSchedule();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }


    private void writeData(byte[] data) {

        if (null != mBleDevice) {
            BleManager.getInstance().write(mBleDevice, BluetoothConstant.SERVICE_UUID_COMPER, "6e400002-b5a3-f393-e0a9-e50e24dcca9e", data, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {
                    LogUtils.d(TAG, "write success current " + current + " total " + total + " justWrite " + ByteUtils.Bytes2HexString(justWrite));
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    LogUtils.d(TAG, "onWriteFailure " + exception.toString());
                }
            });
        }
    }

    /**
     * 获取Comper写入的数据
     *
     * @param featureCode 5A:APP收到数据后应答
     *                    1A:APP设置蓝牙温度计的显示单位为℃
     *                    15:APP设置蓝牙温度计的显示单位为℉
     *                    8C:APP获取历史数据
     * @return
     */
    private byte[] getComperWriteData(String featureCode) {
        Calendar c = Calendar.getInstance();//
        int year = c.get(Calendar.YEAR); // 获取当前年份
        int month = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int hour = c.get(Calendar.HOUR_OF_DAY);//时
        int minute = c.get(Calendar.MINUTE);//分
        int second = c.get(Calendar.SECOND);//秒

        byte[] bytes = new byte[11];
        //头码为0xFE,0xFD;尾码为0x0D,0x0A。
        bytes[0] = (byte) Integer.parseInt("FE", 16);
        bytes[1] = (byte) Integer.parseInt("FD", 16);

        //年 月 日 时 分 秒
        bytes[2] = (byte) Integer.parseInt(String.valueOf(year).substring(2, 4), 16);
        bytes[3] = (byte) Integer.parseInt(String.format("%02d", month), 16);
        bytes[4] = (byte) Integer.parseInt(String.format("%02d", day), 16);
        bytes[5] = (byte) Integer.parseInt(String.format("%02d", hour), 16);
        bytes[6] = (byte) Integer.parseInt(String.format("%02d", minute), 16);
        bytes[7] = (byte) Integer.parseInt(String.format("%02d", second), 16);

        /**
         * 特征码:
         * 0x5A:APP收到数据后应答
         * 0x1A:APP设置蓝牙温度计的显示单位为℃
         * 0x15:APP设置蓝牙温度计的显示单位为℉
         * 0x8C:APP获取历史数据
         */

        bytes[8] = (byte) Integer.parseInt("8C", 16);

        //尾码为0x0D,0x0A。
        bytes[9] = (byte) Integer.parseInt("0d", 16);
        bytes[10] = (byte) Integer.parseInt("0a", 16);

        return bytes;
    }

    private void initSchedule() {
        if (null == executorService) {
            executorService = Executors.newScheduledThreadPool(5);
            //初始化后每隔5秒执行一次
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        CicadaBleBluetooth.getInstance().heartBeatHandle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 7, TimeUnit.SECONDS);
        }
    }

    public void stopSchedule() {
        if (null != executorService) {
            executorService.shutdown();
        }
    }
}


