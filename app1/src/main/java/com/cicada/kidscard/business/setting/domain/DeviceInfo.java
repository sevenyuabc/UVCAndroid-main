package com.cicada.kidscard.business.setting.domain;

import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.DeviceUtils;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxDeviceTool;

/**
 * FileName: DeviceInfo
 * Author: Target
 * Date: 2020/6/15 4:01 PM
 */
public class DeviceInfo {


    /**
     * deviceSn : 11011013123
     * deviceName : 11011013123
     * deviceBrand : 百度
     * deviceModel : 32寸大屏
     * onlineStatus : 1
     * productType : 2
     * configType : 5
     * schoolId : 6542010001
     * schoolName : 测试学校
     * macAddress : ABDDASDDWDWD:WSD
     * algorithmType : 4
     * softwareVersion : 软件版本
     */

    private String deviceSn;
    private String deviceName;
    private String deviceBrand;
    private String deviceModel;
    private int onlineStatus;
    private String productType;
    private String configType;
    private String schoolId;
    private String schoolName;
    private String macAddress;
    private String algorithmType;
    private String softwareVersion;

    public DeviceInfo() {
        this.deviceBrand = RxDeviceTool.getBuildBrandModel();
        this.deviceModel = "32寸刷卡机";
        this.deviceSn = HomeUtils.getSn(AppContext.getContext());
        this.productType = "2";
        this.configType = "16";
        this.onlineStatus = 1;
        this.algorithmType = "";
        this.softwareVersion = RxAppTool.getAppVersionName(AppContext.getContext());
        this.macAddress = DeviceUtils.getMacAddress(AppContext.getContext());
    }

    public String getDeviceSn() {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn) {
        this.deviceSn = deviceSn;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(int onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }
}
