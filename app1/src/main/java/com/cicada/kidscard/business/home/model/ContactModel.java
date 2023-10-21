package com.cicada.kidscard.business.home.model;

import com.cicada.kidscard.business.home.domain.BaseKidsCardInfoResponse;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ContactModel {


    /**
     * 分页获取通讯录
     *
     * @return
     */
    @POST("/zl_api/kidscare/card/pool/queryPageChildInfosBySchoolId")
    Observable<BaseKidsCardInfoResponse> queryPageChildInfosBySchoolId(@Body Request request);

    /**
     * 增量获取通讯录
     *
     * @return
     */
    @POST("/zl_api/kidscare/card/pool/queryInfosBySchoolIdAndMd5Key")
    Observable<List<BaseKidsCardChildInfo>> queryInfosBySchoolIdAndMd5Key(@Body Request request);

    /**
     * 获取单个孩子刷卡信息
     *
     * @return
     */
    @POST("/zl_api/kidscare/card/pool/queryUserInfoByCardNumber")
    Observable<BaseKidsCardChildInfo> getSingleChildInfo(@Query("schoolId") String schoolId, @Body Request request);

}
