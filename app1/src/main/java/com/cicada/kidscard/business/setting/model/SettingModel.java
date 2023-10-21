package com.cicada.kidscard.business.setting.model;

import com.cicada.kidscard.business.setting.domain.DeviceInfo;
import com.cicada.kidscard.business.setting.domain.SchoolInfo;
import com.cicada.kidscard.net.domain.Request;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @ClassName: SettingModel
 * @Description: TODO
 * @Author: liuyun
 * @CreateDate: 2021/9/14 14:19
 * @UpdateUser: liuyun
 * @UpdateDate: 2021/9/14 14:19
 */
public interface SettingModel {
    /**
     * 查询学校信息
     */
    @POST("/zl_api/kidscare/entrance/guard/device/findschoolbyid")
    Observable<SchoolInfo> findschoolbyid(@Body Request request);


    /**
     * 绑定学校
     *
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("/eduplus-safe-campus/entrance/guard/device/v2/saveOrUpdateDevice")
    Observable<String> saveOrUpdateDevice(@Body DeviceInfo deviceInfo);
}
