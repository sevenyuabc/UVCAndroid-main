package com.cicada.kidscard.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * FileName: FileUtils
 * Author: Target
 * Date: 2020/6/3 5:39 PM
 * 文件常量
 */
public class CommFileUtils {


    //下载文件压缩包路径
    public static final String DM_TARGET_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "Face-Import" + File.separator;

    //文件名称
    public static final String FILE_NAME = "Face.zip";

    //版本下载文件压缩包路径
    public static final String DM_TARGET_VERSION_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

    //版本文件名称
    public static final String FILE_VERSION_NAME = "Install.apk";


    /**
     * 静默安装
     */
    public static void installAppSilent(Context mContext, String update_localpath) {



//        if (!TextUtils.isEmpty(update_localpath)) {
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            File apkFile = new File(update_localpath);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Logcat.log().e("==============build  " + Build.VERSION.SDK_INT + "   " + apkFile);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                Uri uri = FileProvider.getUriForFile(mContext, RxAppTool.getAppPackageName(mContext) + ".fileprovider", apkFile);
//                intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                mContext.startActivity(intent);
//            } else {
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                mContext.startActivity(intent);
//            }
//
//        }

    }
}
