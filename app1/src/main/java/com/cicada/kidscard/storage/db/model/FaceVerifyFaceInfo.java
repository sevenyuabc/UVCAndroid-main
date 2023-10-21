package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * Create time: 2019/9/27 13:32
 *
 * @author liuyun.
 */
public class FaceVerifyFaceInfo {
    private String bizid;
    private String invokepid;
    private String school_stdcode;
    private String merchantname;
    private String IRurl;
    private String resultCode;
    private  List<Map<String,Object>> scores;

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

    public String getInvokepid() {
        return invokepid;
    }

    public void setInvokepid(String invokepid) {
        this.invokepid = invokepid;
    }

    public String getSchool_stdcode() {
        return school_stdcode;
    }

    public void setSchool_stdcode(String school_stdcode) {
        this.school_stdcode = school_stdcode;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }


    public String getIRurl() {
        return IRurl;
    }

    public void setIRurl(String IRurl) {
        this.IRurl = IRurl;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<Map<String, Object>> getScores() {
        return scores;
    }

    public void setScores(List<Map<String, Object>> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
