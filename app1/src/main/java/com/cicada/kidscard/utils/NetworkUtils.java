package com.cicada.kidscard.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class NetworkUtils {
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;

    public static final String CTWAP = "ctwap";
    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
    public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static boolean isCheckNet = false;  //检测网络状态

    private static final String TAG = "PingNetworkUtils >>> ";

    /**
     * 检测网络状态
     *
     * @param context
     * @return
     */

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    /**
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     **/
    public static int checkNetworkType(Context context) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return TYPE_OTHER_NET;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return TYPE_OTHER_NET;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！
                    final Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c.getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            if (user.startsWith(CTWAP)) {
                                return TYPE_CT_WAP;
                            }
                        }
                    }
                    c.close();
                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断
                    String netMode = mobNetInfoActivity.getExtraInfo();
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase(Locale.getDefault());
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G) || netMode.equals(UNIWAP)) {
                            return TYPE_CM_CU_WAP;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER_NET;
        }
        return TYPE_OTHER_NET;
    }

    /**
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWapInternet(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CM_CU_WAP || type == TYPE_CT_WAP;
    }

    /**
     * 移动，联通的WAP 代理：10.0.0.172
     *
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWAP_YD_LT(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CM_CU_WAP;
    }

    /**
     * 电信的WAP 代理：10.0.0.200
     *
     * @return true:是wap网络; false:不是wap网络.
     */
    public static boolean isWAP_DX(Context context) {
        int type = checkNetworkType(context);
        return type == TYPE_CT_WAP;
    }

    /**
     * 获取当前连接的wifi名称
     *
     * @param context
     * @return
     */
    public static String getWIFIName(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID().replace("\"", "") : null;
        return wifiId;
    }

    public static  final String  NET_TYPE_WIFI = "wifi";
    public static  final String  NET_TYPE_MOBILE = "mobile";
    public static  final String  NET_TYPE_ETHERNET = "ethernet";
    public static  final String  NET_TYPE_UNKNOW = "unknown";
    /**
     * 获取网络连接方式
     * WIFI、MOBILE、ETHERNET
     *
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = NET_TYPE_UNKNOW;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return strNetworkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            strNetworkType = NET_TYPE_WIFI;
        } else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            strNetworkType = NET_TYPE_MOBILE;
        } else if(networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET){
            strNetworkType = NET_TYPE_ETHERNET;
        }
        return strNetworkType;
    }
    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
    public synchronized static void initBandWidtStart() {
        ThreadPoolUtil.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                float upTraffi = Float.parseFloat(TrafficStats.getTotalTxBytes() + "");
                float downTraffi = Float.parseFloat(TrafficStats.getTotalRxBytes() + "");
                BaseSharePreference.getInstance().setCurrentNetSpeedUp(upTraffi);
                BaseSharePreference.getInstance().setCurrentNetSpeedDown(downTraffi);
            }
        });

    }
    public synchronized static String[] getEthdata() {
        final String DEV_FILE = "/proc/net/dev";// 系统流量文件
        String[] ethdata = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "0", "0", "0", "0" };
        final String ETHLINE = "  eth0";// eth是以太网信息 tiwlan0 是 Wifi rmnet0是GPRS

        FileReader fstream = null;
        try {
            fstream = new FileReader(DEV_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Could not read " + DEV_FILE);
        }
        BufferedReader in = new BufferedReader(fstream, 500);
        String line;
        String[] segs;
        String[] netdata;
        int k;
        int j;
        try {
            while ((line = in.readLine()) != null) {
                segs = line.trim().split(":");
                if (line.startsWith(ETHLINE)) {
                    netdata = segs[1].trim().split(" ");
                    for (k = 0, j = 0; k < netdata.length; k++) {
                        if (netdata[k].length() > 0) {
                            ethdata[j] = netdata[k];
                            j++;
                        }
                    }
                }
            }
            fstream.close();
            in.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return ethdata;

    }

    /**
     * 跳转到系统网络设置页面
     *
     */
    public static void gotoWifiSetting(Context context) {
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(wifiSettingsIntent);
    }
}
