package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;
import com.cicada.kidscard.storage.db.DAOConstants;

import java.io.Serializable;

/**
 * 刷脸记录
 */
@Table(name = DAOConstants.TABLE_BASE_KIDS_VERIFYLOG)
public class BaseKidsVerifyLog extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 记录唯一标识
     */
    @Unique
    @Column(column = DAOConstants.COLUMN_KIDS_VERIFYLOG_LOCALID)
    private String localUniqueId;


    @Column(column = DAOConstants.COLUMN_VERIFYLOG_AUTH_IMG)
    private String auth_img;

    @Column(column = DAOConstants.COLUMN_VERIFYLOG_ZFACE_INFO)
    private String zface_info;

    @Column(column = DAOConstants.COLUMN_VERIFYLOG_DEVICE_NUM)
    private String device_num;

    @Column(column = DAOConstants.COLUMN_VERIFYLOG_SCENE_CODE)
    private String scene_code;

    @Column(column = DAOConstants.COLUMN_VERIFYLOG_FACE_ID)
    private String face_id;

    /**
     * 日志状态：1-成功 0-失败
     */
    @Column(column = DAOConstants.COLUMN_VERIFYLOG_STATUS)
    private  int status;

    @Column(column = DAOConstants.COLUMN_CREATE_TIME)
    private  long createTime;

    public BaseKidsVerifyLog() {
    }

    public String getLocalUniqueId() {
        return localUniqueId;
    }

    public void setLocalUniqueId(String localUniqueId) {
        this.localUniqueId = localUniqueId;
    }

    public String getAuth_img() {
        return auth_img;
    }

    public void setAuth_img(String auth_img) {
        this.auth_img = auth_img;
    }

    public String getZface_info() {
        return zface_info;
    }

    public void setZface_info(String zface_info) {
        this.zface_info = zface_info;
    }

    public String getDevice_num() {
        return device_num;
    }

    public void setDevice_num(String device_num) {
        this.device_num = device_num;
    }

    public String getScene_code() {
        return scene_code;
    }

    public void setScene_code(String scene_code) {
        this.scene_code = scene_code;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
