package com.cicada.kidscard.business.home.model;


import com.cicada.kidscard.business.home.domain.CardRecordIdInfo;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.storage.db.model.BaseTemperatureInfo;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MainModel {


    /**
     * 上传刷卡信息
     *
     * @return
     */
    @POST("/zl_api/kidscare/card/record/sendCardMessage")
    Observable<CardRecordIdInfo> uploadCardRecord(@Body Request request);

    /**
     * 上传体温信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/eduplus-safe-campus/health/student/add")
    Observable<String> uploadTemperatureRecord(@Body BaseTemperatureInfo temperatureInfo);

    /**
     * 上传补偿刷卡信息
     *
     * @return
     */
    @POST("/zl_api/kidscare/card/record/repairCardReord")
    Observable<String> uploadCompensateCardRecord(@Query("schoolId") String schoolId, @Body Request request);


}
