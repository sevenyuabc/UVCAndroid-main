package com.cicada.kidscard.net;


import com.cicada.kidscard.config.AppContext;

public class BaseURL {

    /**
     * 服务器数据解析错误、以及任何未定义的错误
     */
    public static final String APP_EXCEPTION_HTTP_OTHER = "-100";
    /**
     * 业务成功代码：1
     */
    public static final String APP_BUSINESS_SUCCESS = "0000000";

    /**
     * 服务器网络连接(超时)错误
     */
    public static final String APP_EXCEPTION_HTTP_TIMEOUT = "0";
    /**
     * 服务器连接错误404（找不到页面错误）
     */
    public static final String APP_EXCEPTION_HTTP_404 = "404";
    /**
     * 服务器连接错误500（内部服务错误）
     */
    public static final String APP_EXCEPTION_HTTP_500 = "500";

    /**
     * 获取当前环境－服务器域名、地址
     */
    public static String getBaseURL() {
        return AppContext.appEnvConfig.getApiUrl();
    }

    /**
     * 获取天气
     */
    public static final String BASE_URL_WEATHER = "https://restapi.amap.com/";


}
