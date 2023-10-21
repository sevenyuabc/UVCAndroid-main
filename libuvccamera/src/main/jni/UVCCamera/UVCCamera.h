/*
 * UVCCamera
 * library and sample to access to UVC web camera on non-rooted Android device
 *
 * Copyright (c) 2014-2017 saki t_saki@serenegiant.com
 *
 * File name: UVCCamera.h
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
 * Files in the jni/libjpeg, jni/libusb, jin/libuvc, jni/rapidjson folder may have a different license, see the respective files.
*/

#pragma interface

#ifndef UVCCAMERA_H_
#define UVCCAMERA_H_

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <android/native_window.h>
#include "UVCStatusCallback.h"
#include "UVCButtonCallback.h"
#include "UVCPreview.h"
#include "UVCControl.h"

class UVCCamera {
private:
    uvc_context_t *mContext;
    int mFd;
    uvc_device_handle_t *mDeviceHandle;
    UVCStatusCallback *mStatusCallback;
    UVCButtonCallback *mButtonCallback;
    UVCPreview *mPreview;
    UVCControl *mControl;
public:
    UVCCamera();

    ~UVCCamera();

    UVCControl *getControl();

    int connect(int fd, int quirks);

    int release();

    int setStatusCallback(JNIEnv *env, jobject status_callback_obj);

    int setButtonCallback(JNIEnv *env, jobject button_callback_obj);

    char *getSupportedFormats();

    int setPreviewSize(int width, int height, int frameType, int fps);

    int setPreviewDisplay(ANativeWindow *preview_window);

    int setFrameCallback(JNIEnv *env, jobject frame_callback_obj, int pixel_format);

    int startPreview();

    int stopPreview();

    int setCaptureDisplay(ANativeWindow *capture_window);
};

#endif /* UVCCAMERA_H_ */
