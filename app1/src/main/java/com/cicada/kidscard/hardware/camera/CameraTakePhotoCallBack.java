package com.cicada.kidscard.hardware.camera;

import android.graphics.Bitmap;

/**
 * 拍照回调
 * <p>
 * Create time: 2021/5/31 10:21
 *
 * @author liuyun.
 */
public  interface CameraTakePhotoCallBack {
    void  onTakePictureComplete(Bitmap bitmap,String filePath);
}
