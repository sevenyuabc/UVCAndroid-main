package com.cicada.kidscard.net.domain;


import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;
import com.cicada.kidscard.utils.NetworkUtils;
import com.tamsiree.rxtool.RxAppTool;
import com.cicada.kidscard.utils.DeviceUtils;

/**
 * 心跳接口请求数据
 */
public class HeatRequest {


    private ClientInfo clientInfo;
    private String style;
    private PollDataModel data;


    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public PollDataModel getData() {
        return data;
    }

    public void setData(PollDataModel data) {
        this.data = data;
    }

    public static class Builder {
        private ClientInfo clientInfo = new ClientInfo();
        private String style = "black";
        private PollDataModel data;

        public Builder() {

        }

        public Builder withClientInfo(ClientInfo clientInfo) {
            this.clientInfo = clientInfo;
            return this;
        }

        public Builder withStyle(String style) {
            this.style = style;
            return this;
        }

        public Builder withData(PollDataModel data) {
            if (data != null) {
                this.data = data;
            }
            return this;
        }



        public HeatRequest build() {
            HeatRequest request = new HeatRequest();
            request.setStyle(this.style);
            request.setClientInfo(this.clientInfo);
            request.setData(this.data);
            return request;
        }
    }

    public static class ClientInfo {
        private String version = RxAppTool.getAppVersionName(AppContext.getContext());
        private String clientType = "android";
        private String clientModel = DeviceUtils.getDeviceModel();
        private String clientOs = DeviceUtils.getOS();
        private String deviceId = BaseSharePreference.getInstance().getSn();
        private String cNet = NetworkUtils.getNetworkType(AppContext.getContext());

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public String getClientModel() {
            return clientModel;
        }

        public void setClientModel(String clientModel) {
            this.clientModel = clientModel;
        }

        public String getClientOs() {
            return clientOs;
        }

        public void setClientOs(String clientOs) {
            this.clientOs = clientOs;
        }

        public String getcNet() {
            return cNet;
        }

        public void setcNet(String cNet) {
            this.cNet = cNet;
        }
    }
}
