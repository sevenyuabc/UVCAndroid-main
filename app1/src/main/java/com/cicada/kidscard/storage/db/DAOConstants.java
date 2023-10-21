/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: XutilsDemo
 * $Id: DBConstants.java 2014年10月24日 下午2:13:10 $
 */
package com.cicada.kidscard.storage.db;

/**
 * 数据库常量信息定义，包括数据库名、表名和字段名等
 * <p/>
 * 创建时间: 2014年11月14日 下午1:37:12 <br/>
 *
 * @author hwp
 * @since v0.0.1
 */
public interface DAOConstants {

    /**
     * 学校接送卡绑定的孩子信息
     */
    String TABLE_BASE_KIDSCARDCHILDINFO = "base_kidscardchildinfo";
    String COLUMN_KIDSCARDCHILDINFO_CARDNUMBER = "kidscardchildinfo_cardnumber";
    String COLUMN_KIDSCARDCHILDINFO_CHILDINFO = "kidscardchildinfo_childinfo";
    String COLUME_KIDSCARD_USERINFO = "kidscard_userinfo";//刷卡机用户信息
    String COLUMN_ISTEACHER_CARD = "isTeacherCard";
    String COLUMN_REQUET_TIME = "requestTime";//通讯录请求时间
    String COLUMN_RECORD_STATUS = "recordStatus";//当前通讯录记录项状态
    String COLUMN_MD5_KEY = "md5Key";//当前用户的验证码
    /**
     * 学校接送卡绑定的孩子信息
     */
    String TABLE_BASE_KIDSCARDRECORD = "base_kidscardrecord";
    String COLUMN_KIDSCARDRECORD_LOCALID = "kidscardrecord_localId";
    String COLUMN_KIDSCARDRECORD_SCHOOLID = "kidscardrecord_schoolId";
    String COLUMN_KIDSCARDRECORD_CLASSID = "kidscardrecord_classId";
    String COLUMN_KIDSCARDRECORD_CHILDID = "kidscardrecord_childId";
    String COLUMN_KIDSCARDRECORD_CHILDICON = "kidscardrecord_childIcon";
    String COLUMN_KIDSCARDRECORD_CHILDNAME = "kidscardrecord_childName";
    String COLUMN_KIDSCARDRECORD_CARDNUMBER = "kidscardrecord_cardNumber";
    String COLUMN_KIDSCARDRECORD_USERICON = "kidscardrecord_userIcon";
    String COLUMN_KIDSCARDRECORD_TEMPERATURE = "kidscardrecord_temperature";
    String COLUMN_KIDSCARDRECORD_REQUESTDATE = "kidscardrecord_requestDate";
    String COLUMN_KIDSCARDRECORD_CHILDCLASSID = "kidscardrecord_childClassId";
    String COLUMN_KIDSCARDRECORD_SCHOOL_STATE = "kidscardrecord_schoolState";
    String COLUMN_KIDSCARDRECORD_IS_TEACHER_CARD = "kidscardrecord_isTeacherCard";
    String COLUMN_KIDSCARDRECORD_STATUS = "kidscardrecord_status";
    String COLUMN_KIDSCARDRECORD_ENTRANCE = "kidscardrecord_entrance";
    String COLUMN_KIDSCARDRECORD_AREAID = "kidscardrecord_areaId";
    /**
     * 学校接送卡孩子拍照照片地址
     */
    String TABLE_BASE_TAKEPHOTO = "base_kidscard_takephoto";
    String COLUMN_KIDSCARD_TAKEPHOTO_ID = "kidscar_takephoto_recordID";
    String COLUMN_KIDSCARD_TAKEPHOTO_FACE_ID = "kidscar_takephoto_recordFaceID";
    String COLUMN_KIDSCARD_TAKEPHOTO_INFO = "kidscar_takephoto_INFO";
    String COLUMN_KIDSCARD_PHOTO_PATH = "kidscar_takephoto_PATH";

    /**
     * 进校排名
     */
    String TABLE_BASE_ORDER_TOP = "base_kidscard_order_top";
    String COLUMN_KIDSCARD_ORDER_TOP = "order_top";
    String COLUMN_KIDSCARD_ORDER_TOP_CARDNUMBER = "order_top_cardnumber";

    /**
     * 人脸特征值信息
     */
    String TABLE_BASE_KIDSFACEINFO = "table_base_kidsfaceinfo";
    String COLUMN_KIDS_FACEINFO_LOCALID = "kids_faceinfo_localid";
    String COLUMN_KIDS_FACEINFO_USERID = "kids_faceinfo_userid";
    String COLUMN_KIDS_FACEINFO_ICON = "kids_faceinfo_icon";
    String COLUMN_KIDS_FACEINFO_USER_NAME = "kids_faceinfo_user_name";
    String COLUMN_KIDS_FACEINFO_SCHOOLID = "kids_faceinfo_schoolid";
    String COLUMN_KIDS_FACEINFO_CLASS_NAME = "kids_faceinfo_class_name";
    String COLUMN_KIDS_FACEINFO_CLASS_ID = "kids_faceinfo_class_id";
    String COLUMN_KIDS_FACEINFO_CHILD_NAME = "kids_faceinfo_child_name";
    String COLUMN_KIDS_FACEINFO_CHILD_ID = "kids_faceinfo_child_id";
    String COLUMN_KIDS_FACEINFO_RELATION = "kids_faceinfo_relation";
    String COLUMN_KIDS_FACEINFO_FACEFEATURE = "kids_faceinfo_faceFeature";
    String COLUMN_KIDS_FACEINFO_FACEFEATURE_VERSION = "kids_faceinfo_faceFeature_version";
    String COLUMN_CONSTANT_KIDS_FACEINFO_FACEFEATURE_VERSION = "jasmine_v1.2-9903ebccf5-9903ebccf5";
    String COLUMN_CONSTANT_KIDS_UID = "kids_faceinfo_uid";
    String COLUMN_KIDS_FACEINFO_TRAFFICSTATUS = "kids_faceinfo_trafficstatus";
    String COLUMN_KIDS_FACEINFO_PARENT_LIST = "kids_faceinfo_parentlist";
    String COLUMN_KIDS_FACEINFO_CARDNO = "kids_faceinfo_cardNo";
    String KIDS_FACEINFO_TEACHERID = "kids_faceinfo_teacherid";
    String KIDS_FACEINFO_TEACHERNAME = "kids_faceinfo_teacherName";
    String KIDS_FACEINFO_USERTYPE = "kids_faceinfo_userType";


    /**
     * 识别日志
     */
    String TABLE_BASE_KIDS_VERIFYLOG = "table_base_kidsverifylog";
    String COLUMN_KIDS_VERIFYLOG_LOCALID = "kids_verifylog_localid";
    String COLUMN_CREATE_TIME = "createTime";
    String COLUMN_VERIFYLOG_AUTH_IMG = "auth_img";
    String COLUMN_VERIFYLOG_ZFACE_INFO = "zface_info";
    String COLUMN_VERIFYLOG_DEVICE_NUM = "device_num";
    String COLUMN_VERIFYLOG_SCENE_CODE = "scene_code";
    String COLUMN_VERIFYLOG_FACE_ID = "face_id";
    String COLUMN_VERIFYLOG_STATUS = "status";


    /**
     * 区域通行规则（时间）
     */
    String TABLE_BASE_AREA_THROUGH_RULE = "table_base_area_through_rule";
    String COLUMN_AREA_THROUGH_RULE_USERID = "userId";
    String COLUMN_AREA_THROUGH_RULE_CHILDID = "childId";
    String COLUMN_AREA_THROUGH_RULE_START_TIME_STR = "startTimeStr";
    String COLUMN_AREA_THROUGH_RULE_END_TIME_STR = "endTimeStr";

    /**
     * 体温
     */
    String TABLE_BASE_TEMPERATURE = "table_base_temperature";
    String COLUMN_LOCALID = "kids_localid";
    String COLUMN_USERTYPE = "userType";
    String COLUMN_TEMPERATURE = "temperature";
    String COLUMN_CHECKDATE = "checkDate";

}
