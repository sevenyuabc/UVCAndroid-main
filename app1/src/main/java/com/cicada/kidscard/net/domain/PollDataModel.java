package com.cicada.kidscard.net.domain;

import android.content.Context;

import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.cicada.kidscard.utils.NetworkUtils;
import com.tamsiree.rxtool.RxAppTool;
import com.tamsiree.rxtool.RxDeviceTool;
import com.cicada.kidscard.utils.DeviceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * <p>
 * Create time: 2021/5/18 17:45
 *
 * @author liuyun.
 */
public class PollDataModel {

    private AppInfo appInfo;
    private DeviceInfo deviceInfo;

    public PollDataModel() {
        this.appInfo = new AppInfo(AppContext.getContext());
        this.deviceInfo = new DeviceInfo(AppContext.getContext());
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    private class AppInfo {
        private String version; // app版本号/
        private String schoolId; // 学校id
        private String schoolName; // 学校name
        private int deviceUsed; // 设备是否正在使用1,正在使用，0被后台
        private long contactCount = 0; // 通讯录数量
        private long delayPushCount = 0; // 刷卡记录当前队列数量
        private String province;// 设备所在省份
        private String city; // 设备所在城市
        // 网络信息
        private DeviceNetWork deviceNetwork = new DeviceNetWork();

        public AppInfo() {

        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public DeviceNetWork getDeviceNetwork() {
            return deviceNetwork;
        }

        public void setDeviceNetwork(DeviceNetWork deviceNetwork) {
            this.deviceNetwork = deviceNetwork;
        }

        public AppInfo(Context mContext) {
            version = RxAppTool.getAppVersionName(mContext);
            schoolId = AppSharedPreferences.getInstance().getKidsCardSchoolId();
            schoolName = AppSharedPreferences.getInstance().getKidsCardSchoolName();
            deviceUsed = 0;
            // 在这里处理轮询
            try {
                String nettype = DeviceUtils.getNetworkTypeWifiOrBrand(mContext);

                if (nettype.equals("WIFI")) {// wifi网络
                    deviceNetwork.setAvailable(NetworkUtils.isNetworkAvailable(mContext) ? 1 : 0);
                    deviceNetwork.setType(nettype);
                } else if (nettype.equals("ETHERNET")) {// 以太网
                    deviceNetwork.setAvailable(NetworkUtils.isNetworkAvailable(mContext) ? 1 : 0);
                    deviceNetwork.setType(nettype);
                } else if (nettype.equals("MOBILE")) {
                    deviceNetwork.setAvailable(2);
                    deviceNetwork.setType("MOBILE");
                } else {
                    deviceNetwork.setAvailable(0);
                    deviceNetwork.setType("neterror");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

        public int getDeviceUsed() {
            return deviceUsed;
        }

        public void setDeviceUsed(int deviceUsed) {
            this.deviceUsed = deviceUsed;
        }

        public long getContactCount() {
            return contactCount;
        }

        public void setContactCount(long contactCount) {
            this.contactCount = contactCount;
        }

        public long getDelayPushCount() {
            return delayPushCount;
        }

        public void setDelayPushCount(long delayPushCount) {
            this.delayPushCount = delayPushCount;
        }


        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public class DeviceNetWork {
            private int available;// 当前网络是否可用 1可用，0不可用
            private String type; // 当前网络联网方式 "WIFI"，"ETHERNET","MOBILE","neterror"
            private long upBandWidth; // 网络上行带宽,单位KB
            private long downBandWidth; // 网络下行带宽,单位KB
            private long mobileUpBand;//流量上行
            private long mobileDownband;//流量下行

            public int getAvailable() {
                return available;
            }

            public void setAvailable(int available) {
                this.available = available;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public long getUpBandWidth() {
                if (upBandWidth < 0) {
                    upBandWidth = 0;
                }
                return upBandWidth;
            }

            public void setUpBandWidth(long upBandWidth) {
                this.upBandWidth = upBandWidth;
            }

            public long getDownBandWidth() {
                if (downBandWidth < 0) {
                    downBandWidth = 0;
                }
                return downBandWidth;
            }

            public void setDownBandWidth(long downBandWidth) {
                this.downBandWidth = downBandWidth;
            }

            public long getMobileUpBand() {
                return mobileUpBand;
            }

            public void setMobileUpBand(long mobileUpBand) {
                this.mobileUpBand = mobileUpBand;
            }

            public long getMobileDownband() {
                return mobileDownband;
            }

            public void setMobileDownband(long mobileDownband) {
                this.mobileDownband = mobileDownband;
            }

        }
    }

    public class DeviceInfo {

        private String deviceModel; // 客户端机型
        private String deviceName; // 客户端机型
        private String deviceOs; // 客户端操作系统
        private String deviceIMEI; // IMEI
        private String deviceIMSI; // imsi
        private String deviceUUID; // uuid
        private String deviceNet; // 网络类型
        private String deviceCode; // deviceid
        private String deviceMac; // mac
        private String deviceType;// 客户端类型，android
        private String deviceCpu; // cpu
        private String deviceLoTID;//物联网id
        private String deviceNetSignal;//网络信号类型
        private String networkOperatorName;//移动运营商名称

        Map<String, Object> deviceInfoMap = new HashMap<>();

        public DeviceInfo(Context context) {
            deviceModel = DeviceUtils.getDeviceModel();
            deviceOs = DeviceUtils.getOS();
            deviceIMEI = "DeviceUtils.getIMEI(context)";
            deviceIMSI ="";
            deviceUUID = DeviceUtils.getUUID(context);
            deviceNet = DeviceUtils.getNetworkTypeWifiOrBrand(context);
            deviceCode = BaseSharePreference.getInstance().getSn();
            deviceMac = DeviceUtils.getMacAddress(context);
            deviceType = "android";
//            String inner = new java.text.DecimalFormat("#.0").format(DeviceUtils.getMaxCpuFreq() / 1000 / 1000.0);
//            deviceCpu = DeviceUtils.getNumCores() + "核"
//                    + inner + "GHz";
            deviceName = RxDeviceTool.getBuildBrand();
            deviceLoTID = "";

            deviceInfoMap.put("deviceModel", deviceModel);
            deviceInfoMap.put("deviceOs", deviceOs);
            deviceInfoMap.put("deviceIMEI", deviceIMEI);
            deviceInfoMap.put("deviceIMSI", deviceIMSI);
            deviceInfoMap.put("deviceUUID", deviceUUID);
            deviceInfoMap.put("deviceNet", deviceNet);
            deviceInfoMap.put("deviceCode", deviceCode);
            deviceInfoMap.put("deviceMac", deviceMac);
            deviceInfoMap.put("deviceType", deviceType);
            deviceInfoMap.put("deviceCpu", deviceCpu);
            deviceInfoMap.put("deviceName", deviceName);
            deviceInfoMap.put("deviceLoTID", deviceLoTID);
        }

        public DeviceInfo() {

        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceOs() {
            return deviceOs;
        }

        public void setDeviceOs(String deviceOs) {
            this.deviceOs = deviceOs;
        }

        public String getDeviceIMEI() {
            return deviceIMEI;
        }

        public void setDeviceIMEI(String deviceIMEI) {
            this.deviceIMEI = deviceIMEI;
        }

        public String getDeviceIMSI() {
            return deviceIMSI;
        }

        public void setDeviceIMSI(String deviceIMSI) {
            this.deviceIMSI = deviceIMSI;
        }

        public String getDeviceUUID() {
            return deviceUUID;
        }

        public void setDeviceUUID(String deviceUUID) {
            this.deviceUUID = deviceUUID;
        }

        public String getDeviceNet() {
            return deviceNet;
        }

        public void setDeviceNet(String deviceNet) {
            this.deviceNet = deviceNet;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public String getDeviceMac() {
            return deviceMac;
        }

        public void setDeviceMac(String deviceMac) {
            this.deviceMac = deviceMac;
        }


        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceCpu() {
            return deviceCpu;
        }

        public void setDeviceCpu(String deviceCpu) {
            this.deviceCpu = deviceCpu;
        }

        public String getDeviceLoTID() {
            return deviceLoTID;
        }

        public void setDeviceLoTID(String deviceLoTID) {
            this.deviceLoTID = deviceLoTID;
        }

        public String getDeviceNetSignal() {
            return deviceNetSignal;
        }

        public void setDeviceNetSignal(String deviceNetSignal) {
            this.deviceNetSignal = deviceNetSignal;
        }

        public String getNetworkOperatorName() {
            return networkOperatorName;
        }

        public void setNetworkOperatorName(String networkOperatorName) {
            this.networkOperatorName = networkOperatorName;
        }

        public Map<String, Object> getDeviceInfoMap() {
            return deviceInfoMap;
        }

        public void setDeviceInfoMap(Map<String, Object> deviceInfoMap) {
            this.deviceInfoMap = deviceInfoMap;
        }
    }
}
