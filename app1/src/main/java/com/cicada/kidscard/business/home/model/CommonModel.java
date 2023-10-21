package com.cicada.kidscard.business.home.model;


import com.cicada.kidscard.business.home.domain.FileInfo;
import com.cicada.kidscard.business.home.domain.VersionInfo;
import com.cicada.kidscard.net.domain.HeatRequest;
import com.cicada.kidscard.net.domain.Request;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface CommonModel {

    /**
     * 单个文件上传
     *
     * @param file
     * @return
     */
    @Multipart
    @POST("/zl_api/file/upload/multi")
    Observable<List<FileInfo>> uploadFile(@Part MultipartBody.Part file);

    /**
     * 检测新版本
     */
    @POST("/zl_api/kidscare/face/version/check")
    Observable<VersionInfo> checkVersion(@Body Request request);

    /**
     * 下载 apk 文件
     *
     * @return
     */
    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 检测新版本
     */
    @POST("/zl_api/robrain/dealData/receive")
    Observable<String> heatBeat(@Body HeatRequest request);
}
