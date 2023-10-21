package com.cicada.kidscard.business.home.model;


import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.view.banner.BannerInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BannerModel {

    /**
     * 天气信息
     *
     * @return
     */
    @GET("v3/weather/weatherInfo?key=06c47454a76f4aa53554bae4ba81d191")
    Observable<String> weatherInfo(@Query("city") String cityNo, @Query("extensions") String extensions);

    /**
     * 轮播信息
     *
     * @return
     */
    @POST("/zl_api/robrain/saas/CardMachine/getHomePage")
    Observable<List<BannerInfo>> getAttendancePictureList(@Query("schoolId") String schoolId, @Query("token") String token, @Body Request request);
}
