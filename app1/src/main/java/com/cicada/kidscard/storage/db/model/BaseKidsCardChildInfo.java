package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.storage.db.DAOConstants;
import com.cicada.kidscard.utils.Preconditions;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;

import java.io.Serializable;

/**
 * 缓存同步当前学校的所有孩子绑卡信息
 * <p/>
 * 创建时间: 2015年6月25日 下午3:07:24 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
@Table(name = DAOConstants.TABLE_BASE_KIDSCARDCHILDINFO)
public class BaseKidsCardChildInfo extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 接送卡卡号信息
     */
    @Unique
    @Column(column = DAOConstants.COLUMN_KIDSCARDCHILDINFO_CARDNUMBER)
    private String cardNumber;
    /**
     * 当前卡是否为老师卡
     * 1-老师 0-学生
     */
    @Column(column = DAOConstants.COLUMN_ISTEACHER_CARD)
    private int isTeacherCard;
    /**
     * 接送对应的用户信息，json字符串形式
     */
    @Column(column = DAOConstants.COLUMN_KIDSCARDCHILDINFO_CHILDINFO)
    private String strUserInfo;

    /**
     * 当前用户通讯录验证码
     */
    @Column(column = DAOConstants.COLUMN_MD5_KEY)
    private String md5Key;
    /**
     * 孩子ID
     */
    @Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CHILDID)
    private String childId;

    /**
     * 老师ID
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USERID)
    private String userId;


    private ChildInfo childInfo;
    private TeacherInfo teacherInfo;

    @Transient
    private int recordStatus;

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getIsTeacherCard() {
        return isTeacherCard;
    }

    public void setIsTeacherCard(int isTeacherCard) {
        this.isTeacherCard = isTeacherCard;
    }

    public String getStrUserInfo() {
        return strUserInfo;
    }

    public void setStrUserInfo(String strUserInfo) {
        this.strUserInfo = strUserInfo;
    }

    public ChildInfo getChildInfo() {
        if(isTeacherCard == 0 && Preconditions.isNotEmpty(strUserInfo)){
            childInfo = JSON.parseObject(strUserInfo,ChildInfo.class);
        }
        return childInfo;
    }

    public void setChildInfo(ChildInfo childInfo) {
        if (childInfo != null) {
            strUserInfo = childInfo.toString();
            childId = childInfo.getChildId();
        }
        this.childInfo = childInfo;
    }

    public TeacherInfo getTeacherInfo() {
        if(isTeacherCard == 1 && Preconditions.isNotEmpty(strUserInfo)){
            teacherInfo = JSON.parseObject(strUserInfo,TeacherInfo.class);
        }
        return teacherInfo;
    }

    public void setTeacherInfo(TeacherInfo teacherInfo) {
        if (teacherInfo != null) {
            strUserInfo = teacherInfo.toString();
            userId = teacherInfo.getUserId();
        }
        this.teacherInfo = teacherInfo;
    }

    /**
     * md5用户值
     *
     * @return
     */
    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
