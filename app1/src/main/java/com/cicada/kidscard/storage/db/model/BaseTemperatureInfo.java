package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;
import com.cicada.kidscard.storage.db.DAOConstants;

import java.io.Serializable;

/**
 * 体温记录
 * <p>
 * Create time: 2020-02-14 16:31
 *
 * @author liuyun.
 */
@Table(name = DAOConstants.TABLE_BASE_TEMPERATURE)
public class BaseTemperatureInfo extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 记录唯一标识：识别成功的时间
     */
    @Unique
    @Column(column = DAOConstants.COLUMN_LOCALID)
    private String localUniqueId;

    /**
     * 用户id（家长/老师）
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USERID)
    private String userNo;

    /**
     * 学校id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_SCHOOLID)
    private String schoolNo;

    /**
     * 用户类型:用户类型 0学生1老师2家长
     */
    @Column(column = DAOConstants.COLUMN_USERTYPE)
    private int userType;
    /**
     * 班级id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CLASS_ID)
    private String classNo;

    /**
     * 班级名称
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CLASS_NAME)
    private String className;


    /**
     * 孩子id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CHILD_ID)
    private String childNo;

    /**
     * 孩子name
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CHILD_NAME)
    private String childName;


    /**
     * 名称
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USER_NAME)
    private String userName;

    /**
     * 体温
     */
    @Column(column = DAOConstants.COLUMN_TEMPERATURE)
    private String morningTemperature;

    /**
     * 本地测温时间
     */
    @Column(column = DAOConstants.COLUMN_CHECKDATE)
    private long checkDateStr;

    /**
     * 数据来源：0家长填报1老师填报2体温枪扫描
     */
    @Transient
    private int sourceType = 2;

    @Transient
    private String device_sn;


    public BaseTemperatureInfo() {
        this.sourceType = 2;
    }


    public String getLocalUniqueId() {
        return localUniqueId;
    }

    public void setLocalUniqueId(String localUniqueId) {
        this.localUniqueId = localUniqueId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getSchoolNo() {
        return schoolNo;
    }

    public void setSchoolNo(String schoolNo) {
        this.schoolNo = schoolNo;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getClassNo() {
        return classNo;
    }

    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getChildNo() {
        return childNo;
    }

    public void setChildNo(String childNo) {
        this.childNo = childNo;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(String morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public long getCheckDateStr() {
        return checkDateStr;
    }

    public void setCheckDateStr(long checkDateStr) {
        this.checkDateStr = checkDateStr;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getDevice_sn() {
        return device_sn;
    }

    public void setDevice_sn(String device_sn) {
        this.device_sn = device_sn;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
