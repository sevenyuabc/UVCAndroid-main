package com.cicada.kidscard.net.domain;


import com.cicada.kidscard.net.BaseURL;

public class Result<T> {
    /**
     * 服务器当前时间
     */
    private long ts;
    /**
     * 响应结果code
     */
    private String rtnCode;
    /**
     * 提示语
     */
    private String msg;

    /**
     * 业务相关数据
     */
    private T bizData;


    /***
     * code 和 data 是另一种返回方式
     */
    private int code;

    private T data;


    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getBizData() {
        return bizData;
    }

    public void setBizData(T bizData) {
        this.bizData = bizData;
    }

    public long getTs() {
        return ts;
    }


    /**
     * 服务器当前时间
     *
     * @since v0.0.1
     */
    public void setTs(long ts) {
        this.ts = ts;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return BaseURL.APP_BUSINESS_SUCCESS.equalsIgnoreCase(this.rtnCode) || 200 == code;
    }
}
