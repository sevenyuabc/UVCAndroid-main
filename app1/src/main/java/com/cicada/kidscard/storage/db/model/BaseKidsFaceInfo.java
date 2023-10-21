package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;
import com.cicada.kidscard.storage.db.DAOConstants;

import java.io.Serializable;
import java.util.List;

/**
 * 人脸数据
 */
@Table(name = DAOConstants.TABLE_BASE_KIDSFACEINFO)
public class BaseKidsFaceInfo extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 记录唯一标识：userId_child (userId和childId可能重复)
     */
    @Unique
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_LOCALID)
    private String localUniqueId;

    /**
     * 用户id（家长/老师）
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USERID)
    private String userId;

    /**
     * 图像
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_ICON)
    private String userPicture;

    /**
     * 孩子name
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CHILD_NAME)
    private String childName;

    /**
     * 孩子id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CHILD_ID)
    private String childId;

    /**
     * 学校id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_SCHOOLID)
    private String schoolId;

    /**
     * 班级id
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CLASS_ID)
    private String classId;

    /**
     * 班级名称
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CLASS_NAME)
    private String className;
    /**
     * 名称
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USER_NAME)
    private String userName;

    /**
     * 关系（家长有效）
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_RELATION)
    private String relation;

    /**
     * 特征值
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_FACEFEATURE)
    private String faceFeature;

    /**
     * 特征值版本
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_FACEFEATURE_VERSION)
    private String faceFeatureVersion;

    /**
     * md5
     */
    @Column(column = DAOConstants.COLUMN_MD5_KEY)
    private String md5Keys;
    /**
     * 支付宝userID
     */
    @Column(column = DAOConstants.COLUMN_CONSTANT_KIDS_UID)
    private String aliUserId;

    /**
     * trafficStatus 1:判断门禁 2:禁止通行
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_TRAFFICSTATUS)
    private int trafficStatus;

    /**
     * 当前记录状态（增量更新使用）
     * 1增加  -1减少
     */
    @Transient
    private int recordStatus;

    /**
     * 孩子家长信息
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_PARENT_LIST)
    private String parentListStr;

    /**
     * 卡号
     */
    @Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_CARDNO)
    private String cardNo;
    /**
     * 老师id
     */
    @Column(column = DAOConstants.KIDS_FACEINFO_TEACHERID)
    private String teacherId;
    /**
     * 老师Name
     */
    @Column(column = DAOConstants.KIDS_FACEINFO_TEACHERNAME)
    private String teacherName;
    /**
     * userType
     */
    @Column(column = DAOConstants.KIDS_FACEINFO_USERTYPE)
    private String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    /**
     * 识别的时间戳
     */
    @Transient
    private long faceTime;

    public long getFaceTime() {
        return faceTime;
    }

    public void setFaceTime(long faceTime) {
        this.faceTime = faceTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Transient
    private List<ParentInfo> parentList;


    public BaseKidsFaceInfo() {
        this.faceFeatureVersion = DAOConstants.COLUMN_CONSTANT_KIDS_FACEINFO_FACEFEATURE_VERSION;
    }

    public String getParentListStr() {
        return parentListStr;
    }

    public void setParentListStr(String parentListStr) {
        this.parentListStr = parentListStr;
    }

    public List<ParentInfo> getParentList() {
        return parentList;
    }

    public void setParentList(List<ParentInfo> parentList) {
        if (null != parentList && !parentList.isEmpty()) {
            this.parentListStr = JSON.toJSONString(parentList);
        }
        this.parentList = parentList;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public int getTrafficStatus() {
        return trafficStatus;
    }

    public void setTrafficStatus(int trafficStatus) {
        this.trafficStatus = trafficStatus;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(String faceFeature) {
        this.faceFeature = faceFeature;
    }

    public String getFaceFeatureVersion() {
        return faceFeatureVersion;
    }

    public void setFaceFeatureVersion(String faceFeatureVersion) {
        this.faceFeatureVersion = faceFeatureVersion;
    }

    public String getLocalUniqueId() {
        return localUniqueId;
    }

    public void setLocalUniqueId(String localUniqueId) {
        this.localUniqueId = localUniqueId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMd5Keys() {
        return md5Keys;
    }

    public void setMd5Keys(String md5Keys) {
        this.md5Keys = md5Keys;
    }

    public String getAliUserId() {
        return aliUserId;
    }

    public void setAliUserId(String aliUserId) {
        this.aliUserId = aliUserId;
    }

    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
