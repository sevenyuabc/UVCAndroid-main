package com.cicada.kidscard.utils;

import android.util.Log;

import com.cicada.kidscard.constant.Constants;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * FileName: NetStateUtils
 * Author: Target
 * Date: 2020-08-06 16:30
 * 网络状态工具类
 */
public class NetStateUtils {

    private static final String TAG = "PingNetworkUtils >>> ";
    private static PingNetWorkListener pingNetWorkListener;

    /**
     * ping监听器接口定义
     */
    public interface PingNetWorkListener {
        void onPingState(boolean isConnected);
    }

    /**
     * 设置ping结果反馈监听器
     *
     * @param listener 监听器
     */
    public static void setPingListener(PingNetWorkListener listener) {
        pingNetWorkListener = listener;
    }

    /**
     * 开始ping www.baidu.com， 异步处理
     */
    public static void startPing() {
        ThreadPoolUtil.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                if (pingNetWorkListener != null) {
                    int state = checkNetworkByHttp();
//                    if (state == 2) {
//                        boolean ret = checkNetworkByPing();
//                        pingNetWorkListener.onPingState(ret);
//                    } else
                    pingNetWorkListener.onPingState(state == 0);
                }
            }
        });
    }

    /**
     * 检测网络是否能连接外网，通过http方式
     *
     * @return 0 连通  1 不连通 2 异常
     */
    public static int checkNetworkByHttp() {
        LogUtils.d(TAG, "start to connect www.baidu.com by http....");
        int isConnected;
        try {
            URL url = new URL("http://www.baidu.com/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection == null) {
                LogUtils.d(TAG, "[checkNetworkByHttp] httpURLConnection is null!");
                return 1;
            }
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                return 1;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (bufferedReader.readLine().length() > 1) {
                isConnected = 0;
                LogUtils.d(TAG, "connected baidu by http success!");
            } else {
                isConnected = 1;
                LogUtils.d(TAG, "connected baidu by http failed!");
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            isConnected = 2;
            LogUtils.d(TAG, "connected baidu wlan by http exception!");
            e.printStackTrace();
        }
        return isConnected;
    }

    /**
     * 检测网络是否能连接外网，通过ping方式
     *
     * @return true/false
     */
    public static boolean checkNetworkByPing() {
        LogUtils.d(TAG, "start to connect www.baidu.com by ping....");
        try {
            // ping 10次 每次超时1秒，阻塞处理
            Process process = Runtime.getRuntime().exec("ping -c 10 -W 1 www.baidu.com");
            if (process == null) {
                LogUtils.d(TAG, " ping process is null.");
                return false;
            }

            // 读取ping的内容
            InputStream input = process.getInputStream();
            if (input == null) {
                LogUtils.d(TAG, " process InputStream is null.");
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuffer = new StringBuilder();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content).append("\n");
            }
            if (stringBuffer.length() > 0 && !stringBuffer.toString().contains("100% packet loss")) {
                return true;
            }

//            /* ping的状态, 不调用waitFor接口，会导致卡住异常 */
//            int status;
//            try {
//                status = process.waitFor();
//                if (status == 0) {
//                    isConnected = true;
//                   LogUtils.d(TAG, "connected baidu by ping success.");
//                } else {
//                    isConnected = false;
//                   LogUtils.d(TAG, "connected baidu by ping failed.");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } catch (IOException e) {
            Log.e(TAG, "connected baidu by ping failed because of IOException.");
            e.printStackTrace();
        }
        return false;
    }

    public static void netPing() {
        ThreadPoolUtil.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                if (pingNetWorkListener != null) {
                    URL url;
                    InputStream stream = null;
                    try {
                        url = new URL("https://www.baidu.com");
                        stream = url.openStream();
                        pingNetWorkListener.onPingState(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        pingNetWorkListener.onPingState(false);
                    } finally {
                        try {
                            if (null != stream) {
                                stream.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.gc();
            }
        });
    }

    /***
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     *
     * @return
     */

    public static final boolean ping() {
        String result = null;
        try {
            String ip = "https://www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            result = "IOException";
        } catch (InterruptedException e) {
            result = "InterruptedException";
        } finally {
            LogUtils.d("--ping--result---", result);
        }
        return false;

    }

    public static void isOnline(final PingNetWorkListener netWorkView) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                URL url;
                InputStream stream = null;
                try {
                    url = new URL(Constants.BAIDU);
                    stream = url.openStream();
                    AppSharedPreferences.getInstance().setBooleanValue(Constants.NET_AVAILABLE, true);
                    if (null != netWorkView) {
                        netWorkView.onPingState(true);
                    }
                } catch (Exception e) {
                    AppSharedPreferences.getInstance().setBooleanValue(Constants.NET_AVAILABLE, false);
                    e.printStackTrace();
                    if (null != netWorkView) {
                        netWorkView.onPingState(false);
                    }
                } finally {
                    try {
                        if (null != stream) {
                            stream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
