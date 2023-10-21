package com.cicada.kidscard.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.home.presenter.HomeUtils;
import com.cicada.kidscard.business.home.model.CommonModel;
import com.cicada.kidscard.net.BaseURL;
import com.cicada.kidscard.net.SSLSocketFactoryUtils;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownLoadAppBackService extends IntentService {

    public static final String BUNDLE_KEY_DOWNLOAD_URL = "download_url";
    public static final String BUNDLE_KEY_VERSION_NAME = "version_name";

    private static final String TAG = "DownLoadAppBackService";
    private String versionName;
    private String apkPath;
    private static Context context;

    public DownLoadAppBackService() {
        super("DownLoadAppBackService");
    }

    public static void startDownLoadAppService(Context cxt, String versionName, String url) {
        ActivityManager manager = (ActivityManager) cxt.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (DownLoadAppBackService.class.getName().equals(service.service.getClassName())) {
                LogUtils.d(TAG, "-->不再启动服务，已经在下载了");
                return;//退出不再继续执行
            }
        }
        context = cxt;
        Intent startServiceIntent = new Intent(cxt, DownLoadAppBackService.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString(BUNDLE_KEY_VERSION_NAME, versionName);
        bundle2.putString(BUNDLE_KEY_DOWNLOAD_URL, url);
        startServiceIntent.putExtras(bundle2);
        LogUtils.d(TAG, "-->启动了下载服务");
        cxt.startService(startServiceIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        this.versionName = intent.getExtras().getString(BUNDLE_KEY_VERSION_NAME);
        this.apkPath = MyApplication.getInstance().getInstallApkPath(versionName);
        String url = intent.getExtras().getString(BUNDLE_KEY_DOWNLOAD_URL);
        LogUtils.d(TAG, "-->同步下载开始");
        // 启动后台服务下载apk
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.getBaseURL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        Call<ResponseBody> call = retrofit.create(CommonModel.class).downloadFile(url);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                LogUtils.d(TAG, "-->同步下载结束");
                if (writtenToDisk) {
                    LogUtils.d(TAG, "installing");
                    HomeUtils.installApk(apkPath);
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToastImage(context, "更新失败", 0);
                        }
                    });
                }
            } else {
                call.cancel();
                stopSelf();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToastImage(context, "更新失败", 0);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.d(TAG, e.getMessage());
            call.cancel();
            stopSelf();
        }
    }

    /**
     * 写入
     *
     * @param body
     * @return
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(apkPath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                LogUtils.d(TAG, e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            LogUtils.d(TAG, e.getMessage());
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
