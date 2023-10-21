package com.cicada.kidscard.hardware.Bluetooth;

/**
 * 蓝牙数据回调
 * <p>
 * Create time: 2020/2/10 11:14
 *
 * @author liuyun.
 */
public interface IBluetoothView {
    /**
     * @param code 1 :数据正常     2：数据异常   3：没有数据   4：体温枪连接正常    5：体温枪连接异常
     * @param obj  数据
     */
    void onBluetoothDataType(int code, String obj);
}
