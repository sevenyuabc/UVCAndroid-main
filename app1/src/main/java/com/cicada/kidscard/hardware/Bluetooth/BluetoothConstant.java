package com.cicada.kidscard.hardware.Bluetooth;

/**
 * TODO
 * <p>
 * Create time: 2020-02-14 14:13
 *
 * @author liuyun.
 */
public class BluetoothConstant {

    /**
     * 库存体温枪（BF4030）
     */
    public static final String LOCK_NAME_BF = "BF4030";//通用设备名称（库存设备）
    public static final String SERVICE_UUID_BF = "0000fff0-0000-1000-8000-00805f9b34fb";//服务UUID（库存设备）
    public static final String NOTIFY_UUID_BF = "0000fff7-0000-1000-8000-00805f9b34fb";//通知UUID

    /**
     * Comper体温枪
     */
    public static final String LOCK_NAME_COMPER = "Comper";//Comper设备名称
    public static final String SERVICE_UUID_COMPER = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";//服务UUID
    public static final String NOTIFY_UUID_COMPER = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";//通知UUID

    /**
     * 华安测温枪
     */
    public static final String LOCK_NAME_HUAAN = "JASUN";//通用设备名称（华安测温枪）
    public static final String SERVICE_UUID_HUAAN = "0000fff0-0000-1000-8000-00805f9b34fb";//服务UUID
    public static final String NOTIFY_UUID_HUAAN = "0000fff1-0000-1000-8000-00805f9b34fb";//通知UUID

    public static final int BLUETOOTH_CONNECTED = 102; //蓝牙设备连接正常
    public static final int BLUETOOTH_CONNECT_ERROR = 103; //蓝牙设备连接异常
    public static final int BLUETOOTH_DISCONNECT = 104; //蓝牙设备断开连接
    public static final int BLUETOOTH_DATA_SUCCESS = 105;// 数据正常

    public static final String ACTION_BLUETOOTH_DEVICES_STATUS = "action_bluetooth_devices_status";

}

