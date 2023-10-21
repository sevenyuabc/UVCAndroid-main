package com.cicada.kidscard.hardware.Bluetooth;

import com.alibaba.fastjson.JSON;

/**
 * TODO
 * <p>
 * Create time: 2020-02-27 17:51
 *
 * @author liuyun.
 */
public class BluetoothDeviceInfo {
    private String name;
    private String mac;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
