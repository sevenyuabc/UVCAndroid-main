package com.cicada.kidscard.business.home.presenter;

import com.cicada.kidscard.business.home.domain.FileInfo;
import com.cicada.kidscard.business.home.model.CommonModel;
import com.cicada.kidscard.business.home.view.IUploadView;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.utils.FileUtil;
import com.cicada.kidscard.utils.Preconditions;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 文件上传
 * <p>
 * Create time: 2021/5/13 10:42
 *
 * @author liuyun.
 */
public class UploadPresenter {

    private final IUploadView uploadView;

    public UploadPresenter(IUploadView uploadView) {
        this.uploadView = uploadView;
    }

    public void uploadPostFile(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        RetrofitUtils.createService(CommonModel.class)
                .uploadFile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<FileInfo>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onSuccess(List<FileInfo> result) {

                        if (null != uploadView) {
                            if (Preconditions.isNotEmpty(result)) {
                                FileUtil.deleteFile(filePath);
                                uploadView.uploadSuccess(result);
                            } else {
                                uploadView.uploadFailed();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        if (null != uploadView) {
                            uploadView.uploadFailed();
                        }
                    }
                });
    }
}
