package com.cicada.kidscard.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.config.AppEnvConfig;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.tamsiree.rxtool.RxAppTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 设备工具类
 * <p/>
 * 创建时间: 2014-8-19 下午2:10:48 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public abstract class DeviceUtils {
    private final static String TAG = DeviceUtils.class.getSimpleName();


   /* protected void hideBottomUIMenu(Activity context) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = context.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.这种方式虽然是官方推荐，但是根本达不到效果
            View decorView = context.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
*/
    /**
     * 当前手机时区名称。如：GMT+0800
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeZoneName(Context context) {
        String timeZoneDisplayName = "GMT+0800";
        try {
            // GMT+0800
            timeZoneDisplayName = TimeZone.getDefault().getDisplayName(true,
                    TimeZone.SHORT);
            long offset = TimeZone.getDefault().getRawOffset();
            int zone = (int) (offset / (60 * 60 * 1000));
            zone = zone * 100;
            if (zone >= 0) {
                timeZoneDisplayName = String.format("GMT+%04d", Math.abs(zone));
            } else {
                timeZoneDisplayName = String.format("GMT-%04d", Math.abs(zone));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneDisplayName;
    }

    /**
     * 当前手机时区编号。如：Asia/Shanghai
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getTimeZoneID(Context context) {
        String timeZoneID = "Asia/Shanghai";
        try {
            // Asia/Shanghai
            timeZoneID = TimeZone.getDefault().getID();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeZoneID;
    }

    /**
     * 获取手机语言信息(例如：en、zh) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLanguage(Context context) {
        // 获取系统当前使用的语言
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    /**
     * 获取手机国家信息(例如：EN、CN) <br>
     * (设置成简体中文的时候，getLanguage()返回的是zh,getCountry()返回的是cn)
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getCountry(Context context) {
        // 获取区域
        String country = Locale.getDefault().getCountry();
        return country;
    }

    /**
     * 获取当前设备的唯一标识字符串，自己组装的
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getUUID(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String DeviceId, SimSerialNumber, AndroidId;
        DeviceId = "" + tm.getDeviceId();
        SimSerialNumber = "" + tm.getSimSerialNumber();
        AndroidId = ""
                + android.provider.Settings.Secure.getString(
                context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(AndroidId.hashCode(),
                ((long) DeviceId.hashCode() << 32) | SimSerialNumber.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 获取系统唯一标识码(IMEI)<br>
     * 获取机串IMEI: 仅仅只对Android手机有效
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceId = tm.getDeviceId();
        if (DeviceId == null) {
            return "";
        } else {
            return DeviceId;
        }
    }

    /**
     * 获取系统的ANDROID_ID
     */
    public static String getANDROID_ID(Context context) {
        String ANDROID_ID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        if (ANDROID_ID == null) {
            return "";
        } else {
            return ANDROID_ID;
        }
    }

    /**
     * @param context
     * @return String
     * @Description: 获取卡串IMSI
     */
    public static String getIMSI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String id = tm.getSubscriberId();
        if (id == null) {
            return "";
        } else {
            return id;
        }
    }

    /**
     * @return String
     * @Description: 获取手机型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * @return String
     * @Description: 获取系统版本
     */
    public static String getOS() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取SIM卡的可用状态
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean isSimActive() {
        TelephonyManager tm = (TelephonyManager) MyApplication.getInstance()
                .getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        return TelephonyManager.SIM_STATE_READY == tm.getSimState();
    }

    /**
     * 获取设备是否是横屏(AndroidPad)设备
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean getLandscapeDevice(Context context) {
        Activity activity = (Activity) context;
        int orientation = activity.getResources().getConfiguration().orientation;
        int displayRotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        return (orientation == Configuration.ORIENTATION_PORTRAIT && displayRotation % 2 != 0)
                || (orientation == Configuration.ORIENTATION_LANDSCAPE && displayRotation % 2 == 0);
    }

    /**
     * @Description: 获取手机屏幕宽像素
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * @Description: 获取手机屏幕高像素
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 状态栏的高度
     *
     * @param activity 必须传入Activity
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static int getScreenStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);// /取得整个视图部分,注意，如果你要设置标题样式，这个必须出现在标题样式之后，否则会出错
        int screenWidth = frame.width();
        int screenHeight = frame.height();
        // 状态栏的高度，所以frame.height,frame.width分别是系统的高度的宽度
        int statusBarHeight = frame.top;

        View v = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);// /获得根视图
        // 状态栏标题栏的总高度/ statusBarHeight是上面所求的状态栏的高度
        int contentTop = v.getTop();
        // 所以标题栏的高度为contentTop-statusBarHeight
        int titleBarHeight = contentTop - statusBarHeight;
        // 视图区域可以使用的宽度
        int realViewWidth = v.getWidth();
        // 视图的高度，不包括状态栏和标题栏
        int realViewHeight = v.getHeight();
        String strDeviceSizeInfo = "屏幕宽度" + screenWidth + "\n" + "屏幕高度"
                + screenHeight + "\n" + "状态栏高度" + statusBarHeight + "\n"
                + "标题栏高度" + titleBarHeight + "\n" + "可用宽度" + realViewWidth
                + "\n" + "可用高度（不含状态栏和标题栏）" + realViewHeight;

        LogUtils.i("DeviceSizeInfo", strDeviceSizeInfo);
        return statusBarHeight;
    }

    /**
     * 获取设备MAC地址
     * 需添加权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * @param context 上下文
     * @return
     */
    public static String getMacAddress(Context context) {
        String macAddress = "";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress())) {
                    macAddress = info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;
    }



    private static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];

        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, StandardCharsets.UTF_8);// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        return s;
    }

    private static void write_key(String filename, String str) {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(str.getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String read_key(String filename) {
        File file = new File(filename);
        char[] str = new char[50];
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String macAddrhex = in.readLine();
            String macAddr = toStringHex(macAddrhex);
            in.close();
            return macAddr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取以太网的mac地址（设备自带网线接口时）
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getMacAddressOfEthernet() {
        try {
            String macFileString = "/sys/class/net/eth0/address";
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader(
                    macFileString));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            String macAddress = fileData.toString();
            return macAddress.toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前网络下的IP地址 <a
     * href="http://www.cnblogs.com/android100/p/Android-get-ip.html" >参考资料</a>
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getIPAddress(Context context) {
        String strIPAddress = "";
        switch (NetworkUtils.getNetworkType()) {
            case 0:
                strIPAddress = "0.0.0.0";
                break;
            case 1:
                strIPAddress = getLocalIpAddressByWifi(context);
                break;
            default:
                strIPAddress = getLocalIpAddressByGPRS();
                break;
        }
        return strIPAddress;
    }

    /**
     * 获取Wifi网络下的IP地址
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLocalIpAddressByWifi(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
                    + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
        } catch (Exception ex) {
            LogUtils.e(TAG, "getLocalIpAddressByWifi" + ex.toString());
        }
        return "0.0.0.0";
    }

    /**
     * 获取GPRS移动网络或以太网下的IP地址
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static String getLocalIpAddressByGPRS() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e(TAG, "getLocalIpAddressByGPRS" + ex.toString());
        }
        return "0.0.0.0";
    }

    /**
     * 获取移动运营商名称
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkOperatorName();
    }

    /**
     * 得到一个程序的权限列表(精简AppSecurityPermissions代码实现)。
     *
     * @param context
     * @author hwp
     * @since v0.0.1
     */
    public static void getAppPermisson(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // 得到自己的包名
            String pkgName = pi.packageName;

            PackageInfo pkgInfo = pm.getPackageInfo(pkgName,
                    PackageManager.GET_PERMISSIONS);// 通过包名，返回包信息
            String[] sharedPkgList = pkgInfo.requestedPermissions;// 得到权限列表

            for (int i = 0; i < sharedPkgList.length; i++) {
                String permName = sharedPkgList[i];

                PermissionInfo tmpPermInfo = pm.getPermissionInfo(permName, 0);// 通过permName得到该权限的详细信息
                PermissionGroupInfo pgi = pm.getPermissionGroupInfo(
                        tmpPermInfo.group, 0);// 权限分为不同的群组，通过权限名，我们得到该权限属于什么类型的权限。
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(i + "-" + permName + "\n");
                stringBuffer.append(i + "-" + pgi.loadLabel(pm).toString()
                        + "\n");
                stringBuffer.append(i + "-"
                        + tmpPermInfo.loadLabel(pm).toString() + "\n");
                stringBuffer.append(i + "-"
                        + tmpPermInfo.loadDescription(pm).toString() + "\n");
                stringBuffer.append(i + "- end" + "\n");
                LogUtils.i("getAppPermisson", stringBuffer.toString());
            }
        } catch (NameNotFoundException e) {
            LogUtils.e("getAppPermisson",
                    "Could'nt retrieve permissions for package");
        }
    }

    /**
     * 设置手机的外放，听筒模式
     *
     * @param context
     * @param boolCallIn true＝外放模式，false=听筒模式
     * @author hwp
     * @since v0.0.1
     */
    public static void setPlayMode(Context context, boolean boolCallIn) {
        try {
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (boolCallIn) {
                LogUtils.i("播放模式", "正常放音模式");
                audioManager.setSpeakerphoneOn(true);// 关闭扬声器
                audioManager.setMode(AudioManager.MODE_NORMAL);// 把模式调成正常放音模式
            } else {
                LogUtils.i("播放模式", "听筒放音模式");
                audioManager.setSpeakerphoneOn(true);// 关闭扬声器
                audioManager.setMode(AudioManager.MODE_IN_CALL);// 把模式调成听筒放音模式
            }
        } catch (Exception e) {
            LogUtils.e("getPlayMode",
                    "Could'nt retrieve permissions for package");
        }

    }

    public static int getStatusBarHeight(Context context) {
        Class c = null;
        Object bj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            bj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(bj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断是否具有ROOT权限
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean checkPermissionRoot() {
        boolean res = false;
        try {
            res = (new File("/system/bin/su").exists())
                    || (new File("/system/xbin/su").exists());
        } catch (Exception e) {

        }
        return res;
    }

    /**
     * 判断权限是否可用
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static boolean checkPermission(Context context,
                                          String permissionString) {
        PackageManager pm = context.getPackageManager();
        // boolean permission = (PackageManager.PERMISSION_GRANTED ==
        // pm.checkPermission(permissionString,
        // DeviceUtils.getAppPackageName(MyApplication.getInstance())));
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm
                .checkPermission(permissionString, "com.cicada.cicada"));
        if (permission) {
            ToastUtils.showToastImage(context, "有这个权限", 0);
        } else {
            ToastUtils.showToastImage(context, "木有这个权限", 0);
        }
        return permission;
    }

    /**
     * 获取权限列表
     *
     * @param context
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public static void getPermissionList(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pack = pm.getPackageInfo("packageName",
                    PackageManager.GET_PERMISSIONS);
            String[] permissionStrings = pack.requestedPermissions;
            ToastUtils.showToastImage(context,
                    "权限清单--->" + permissionStrings.toString(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    public static String getUUID2(Context context) {
        UUID uuid = null;
        if (uuid == null) {
            synchronized (DeviceUtils.class) {
                if (uuid == null) {
                    final String id = AppSharedPreferences.getInstance()
                            .getUUID();

                    if (id != null && id.length() > 0) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Secure
                                .getString(context.getContentResolver(),
                                        Secure.ANDROID_ID);
                        // Use the Android ID unless it's broken, in which case
                        // fallback on deviceId,
                        // unless it's not available, then fallback on a random
                        // number which we store
                        // to a prefs file
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId
                                    .getBytes(StandardCharsets.UTF_8));
                        } else {
                            final String deviceId = ((TelephonyManager) context
                                    .getSystemService(Context.TELEPHONY_SERVICE))
                                    .getDeviceId();
                            uuid = deviceId != null ? UUID
                                    .nameUUIDFromBytes(deviceId
                                            .getBytes(StandardCharsets.UTF_8)) : UUID
                                    .randomUUID();
                        }
                        AppSharedPreferences.getInstance().setUUID2(
                                uuid.toString());
                    }
                }
            }
        }

        return uuid.toString();
    }

    /**
     * 获取设备名称
     *
     * @return
     */
    public static String getDevicesName() {
        return Build.DEVICE;
    }

    /**
     * 获取网络连接方式wifi或者ETHERNET
     *
     * @param context
     * @return
     */
    public static String getNetworkTypeWifiOrBrand(Context context) {
        String strNetworkType = "neterror";
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return strNetworkType;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            strNetworkType = "WIFI";
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            strNetworkType = "MOBILE";
        } else {
            strNetworkType = "ETHERNET";
        }
        return strNetworkType;
    }


    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(RxAppTool.getAppPackageName(context),
                            PackageManager.GET_META_DATA);

            return appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static AppEnvConfig getAppEnv(Context context) {
        String app_env = getMetaData(context, "APP_ENV");
        return AppEnvConfig.typeOf(app_env);
    }
}
