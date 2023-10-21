package com.cicada.kidscard.hardware.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.utils.FileUtil;
import com.cicada.kidscard.utils.LogUtils;
import com.tamsiree.rxtool.RxImageTool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class CameraTakeManager {
    Activity activity;
    SurfaceView surfaceView;
    CameraTakePhotoCallBack listener;
    private static volatile CameraTakeManager instance;

    CameraPreviewCallBack cameraPreviewCallBack;
    ExecutorService executorService;


    Camera mCamera;
    boolean takePhoto;//为true时则开始捕捉照片


    public CameraTakeManager() {
        executorService = Executors.newFixedThreadPool(1 + 2 * Runtime.getRuntime().availableProcessors());
    }

    public static CameraTakeManager getInstance() {
        synchronized (CameraTakeManager.class) {
            if (instance == null) {
                instance = new CameraTakeManager();
            }
        }
        return instance;
    }

    public void startPreview(Activity activity, SurfaceView surfaceView, CameraTakePhotoCallBack listener, CameraPreviewCallBack cameraPreviewCallBack) {
        LogUtils.d("startCameraPreview", "ing");
        this.activity = activity;
        surfaceView.setVisibility(View.VISIBLE);
        surfaceView.setZOrderOnTop(true);
        surfaceView.setZOrderMediaOverlay(true);
        this.surfaceView = surfaceView;
        this.listener = listener;
        this.cameraPreviewCallBack = cameraPreviewCallBack;
        initSurfaceView();
    }

    private void initSurfaceView() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void onResume() {
        LogUtils.d("=======", "camera on resume ");
        if (null != this.surfaceView && null != mCamera) {
            this.surfaceView.setVisibility(View.VISIBLE);
            initSurfaceView();
        }
    }

    public void onPause() {
        if (null != surfaceView) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(callback);
        }
    }


    private final Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            if (takePhoto) {
                takeCameraPhoto(bytes, camera);
                takePhoto = false;
                mCamera.setPreviewCallback(null);
            }
        }
    };

    private final SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            LogUtils.d("=======", "surfaceCreated");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtils.d("=======", "surfaceChanged");
            mCamera = openCamera();
            if (mCamera == null) {
                return;
            }
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
                if (null != cameraPreviewCallBack) {
                    cameraPreviewCallBack.previewInitFinished();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LogUtils.d("=========", "surfaceDestroyed");
            holder.removeCallback(callback);
            releaseCamera();
        }
    };


    /**
     * 打开摄像头
     */
    private Camera openCamera() {
        Camera cam = null;
        int cameraId = 0;
        try {
            cam = Camera.open(cameraId);
        } catch (RuntimeException e) {
            LogUtils.e("Camera failed to open: ", e.getLocalizedMessage());
        }
        if (null != cam) {
            Camera.Parameters params = cam.getParameters();
//            List<Camera.Size> camSize = params.getSupportedPictureSizes();
//            for (Camera.Size size : camSize) {
//                LogUtils.e("==========", "width:" + size.width + " height:" + size.height);
//            }

            //旋转角度
            int rotation = 0;
            if (AppContext.isIs32Device()) {
                params.setPreviewSize(640, 480);
                rotation = 0;
            } else if (AppContext.isIsYMDevice()) {
                params.setPreviewSize(640, 480);
                rotation = 90;
            }
            cam.setParameters(params);
            cam.setDisplayOrientation(rotation);
        }
        return cam;
    }

    /**
     * 获取照片
     */
    public void takeCameraPhoto(byte[] data, Camera camera) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                Camera.Size size = camera.getParameters().getPreviewSize();
                int imgWidth = size.width;
                int imgHeight = size.height;
                YuvImage image = new YuvImage(data, ImageFormat.NV21, imgWidth, imgHeight, null);
                if (image != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, imgWidth, imgHeight), 80, stream);
                    Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    int newWidth = 448;//imgWidth * 0.7;
                    int newHeight = 336;//imgHeight * 0.7;
                    if (AppContext.isIsYMDevice()) {
                        /** 因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上*/
                        bmp = RxImageTool.rotate(bmp, 90, 0, 0);
                        newWidth = 336;//imgWidth * 0.7;
                        newHeight = 448;//imgHeight * 0.7;
                    }

                    bmp = RxImageTool.compressByScale(bmp, newWidth, newHeight);

                    final Bitmap bitmap = bmp;
                    String filePath = MyApplication.getInstance().saveCameraImagePath();
                    //保存图片到本地
                    RxImageTool.save(bitmap, filePath, Bitmap.CompressFormat.JPEG);
                    //压缩图片
                    String targetDir = MyApplication.getInstance().getCompressImageDir();
                    Luban.with(AppContext.getContext())
                            .load(filePath)
                            .ignoreBy(30)
                            .setTargetDir(targetDir)
                            .filter(new CompressionPredicate() {
                                @Override
                                public boolean apply(String path) {
                                    return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                                }
                            })
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                    // 压缩开始前调用，可以在方法内启动 loading UI
                                }

                                @Override
                                public void onSuccess(File file) {
                                    //  压缩成功后调用，返回压缩后的图片文件
                                    String filePathAfterCompress = file.getAbsolutePath();
                                    if (!filePath.equalsIgnoreCase(filePathAfterCompress)) {
                                        FileUtil.deleteFile(filePath);
                                    }
                                    if (null != listener) {
                                        listener.onTakePictureComplete(bitmap, filePathAfterCompress);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    //  当压缩过程出现问题时调用
                                    if (null != listener) {
                                        listener.onTakePictureComplete(bitmap, filePath);
                                    }
                                }
                            }).launch();
                } else {
                    if (null != listener) {
                        listener.onTakePictureComplete(null, "");
                    }
                }
            }
        });
    }

    /**
     * 获取相机当前的照片
     */
    public void takePhoto() {
        this.takePhoto = true;
        if (null != mCamera) {
            mCamera.setPreviewCallback(previewCallback);
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
                LogUtils.d("============", "releaseCamera");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放
     */
    public void destroy() {
        onPause();
        releaseCamera();
        this.listener = null;
        this.cameraPreviewCallBack = null;
    }
}
