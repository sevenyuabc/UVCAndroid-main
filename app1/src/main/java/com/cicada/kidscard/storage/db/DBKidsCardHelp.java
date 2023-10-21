/*
 * Copyright (c) 2013-2014, thinkjoy Inc. All Rights Reserved.
 *
 * Project Name: XutilsDemo
 * $Id: areaDao.java 2014年10月24日 上午10:28:29 $
 */
package com.cicada.kidscard.storage.db;

import android.content.Context;

import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.home.presenter.SendCardMessage;
import com.cicada.kidscard.business.home.presenter.SendTemperature;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsCardRecord;
import com.cicada.kidscard.storage.db.model.BaseKidsCardTakePhoto;
import com.cicada.kidscard.storage.db.model.BaseTemperatureInfo;
import com.cicada.kidscard.utils.Preconditions;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

/**
 * 主要缓存当前学校所有的接送卡对应的孩子信息
 * <p/>
 * 创建时间: 2015年6月7日 下午7:05:08 <br/>
 *
 * @author hwp
 * @version @param <T>
 * @since v0.0.1
 */
public class DBKidsCardHelp implements DAOConstants {
    private static volatile DBKidsCardHelp instance = null;

    private final Context mContext;

    public DBKidsCardHelp(Context context) {
        super();
        mContext = context;
    }

    /**
     * 单一实例
     */
    public static DBKidsCardHelp getInstance(Context context) {
        if (instance == null) {
            synchronized (DBKidsCardHelp.class) {
                if (instance == null) {
                    instance = new DBKidsCardHelp(context);
                }
            }
        }
        return instance;
    }

    /**
     * 缓存待提交的接送卡刷卡信息
     *
     * @param baseKidsCardRecord
     * @return
     */
    public boolean saveKidsCardRecord(BaseKidsCardRecord baseKidsCardRecord) {
        if (baseKidsCardRecord == null) {
            return false;
        }
        boolean success = false;
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.save(baseKidsCardRecord);
            SendCardMessage.getInstance().send();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 查询刷卡记录中第一条数据
     *
     * @return
     */
    public BaseKidsCardRecord findFirstCardRecord() {
        BaseKidsCardRecord listBaseKidsCardRecords = null;
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().beginTransaction();
            listBaseKidsCardRecords = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findFirst(Selector.from(BaseKidsCardRecord.class).where(WhereBuilder.b(COLUMN_KIDSCARDRECORD_STATUS, "=", 0)).orderBy(COLUMN_KIDSCARDRECORD_LOCALID, true));
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().endTransaction();
        }
        return listBaseKidsCardRecords;
    }

    /**
     * 查询刷卡记录条数
     *
     * @return
     */
    public long findCardRecordCount() {
        long count = 0;
        try {
            count = (int) DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.count(BaseKidsCardRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }


    /**
     * 查询单个待提交的接送卡刷卡信息
     *
     * @param localId
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public BaseKidsCardRecord findKidsCardRecord(String localId) {
        BaseKidsCardRecord baseKidsCardRecord = null;
        try {
            baseKidsCardRecord = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findFirst(Selector.from(BaseKidsCardRecord.class).where(COLUMN_KIDSCARDRECORD_LOCALID, "=", localId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseKidsCardRecord;
    }

    /**
     * 删除单个待提交的接送卡刷卡信息
     *
     * @param localId
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public boolean deleteKidsCardRecord(String localId) {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.delete(BaseKidsCardRecord.class, WhereBuilder.b(COLUMN_KIDSCARDRECORD_LOCALID, "=", localId));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 删除单个接送卡孩子信息
     *
     * @param cardNumber
     * @return
     */
    public boolean deleteKidsCardChildInfo(String cardNumber) {
        try {
            DAOHelperSchool.getInstance(mContext).mDBUtils.delete(BaseKidsCardChildInfo.class, WhereBuilder.b(COLUMN_KIDSCARDCHILDINFO_CARDNUMBER, "=", cardNumber));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 缓存单条接送卡孩子信息
     *
     * @param baseKidsCardChildInfo
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public boolean saveKidsCardChildInfo(BaseKidsCardChildInfo baseKidsCardChildInfo) {
        if (baseKidsCardChildInfo == null) {
            return false;
        }
        boolean success = false;
        try {
            if (deleteKidsCardChildInfo(baseKidsCardChildInfo.getCardNumber())) {
                DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.save(baseKidsCardChildInfo);
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    /**
     * 保存接送卡孩子信息
     *
     * @param listBaseKidsCardChildInfos
     * @return
     */
    public boolean saveKidsCardChildInfo(List<BaseKidsCardChildInfo> listBaseKidsCardChildInfos) {
        if (listBaseKidsCardChildInfos == null) {
            return false;
        }
        boolean success = false;
        int iii = 0;
        try {
            DAOHelperSchool.getInstance(mContext).mDBUtils.saveAll(listBaseKidsCardChildInfos);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 删除指定MD5key对应的数据
     *
     * @return
     */
    public boolean deleteKidsCardChildInfoMd5IsNull() {
        try {
            DAOHelperSchool.getInstance(mContext).mDBUtils.delete(BaseKidsCardChildInfo.class, WhereBuilder.b(DAOConstants.COLUMN_MD5_KEY, "=", null));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 删除指定MD5key对应的数据
     *
     * @param mdkKeyList
     * @return
     */
    public boolean deleteKidsCardChildInfos(List<String> mdkKeyList) {
        if (Preconditions.isEmpty(mdkKeyList)) {
            return false;
        }
        try {
            DAOHelperSchool.getInstance(mContext).mDBUtils.delete(BaseKidsCardChildInfo.class, WhereBuilder.b(DAOConstants.COLUMN_MD5_KEY, "IN", mdkKeyList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 查询所有接送卡孩子信息
     *
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public List<BaseKidsCardChildInfo> findKidsCardChildInfo() {
        List<BaseKidsCardChildInfo> listBaseKidsCardChildInfos = null;
        try {
            listBaseKidsCardChildInfos = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findAll(BaseKidsCardChildInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listBaseKidsCardChildInfos;
    }

    /**
     * 查询单个接送卡孩子信息
     *
     * @param cardNumber
     * @return
     * @author hwp
     * @since v0.0.1
     */
    public BaseKidsCardChildInfo findKidsCardChildInfo(String cardNumber) {
        BaseKidsCardChildInfo baseKidsCardChildInfo = null;
        try {
            baseKidsCardChildInfo = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findFirst(Selector.from(BaseKidsCardChildInfo.class).where(COLUMN_KIDSCARDCHILDINFO_CARDNUMBER, "=", cardNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseKidsCardChildInfo;
    }

    /**
     * 清空本地通讯录
     *
     * @return
     */
    public boolean clearKidsCardChildInfo() {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.deleteAll(BaseKidsCardChildInfo.class);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存发送照片失败时，上传照片再次进行。
     *
     * @param baseKidscardPhoto
     */
    public void addSendPhotoErrorRecord(BaseKidsCardTakePhoto baseKidscardPhoto) {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.save(baseKidscardPhoto);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存发送照片失败时，上传照片再次进行。
     *
     * @param baseKidscardPhoto
     */
    public void updateSendPhotoErrorRecord(BaseKidsCardTakePhoto baseKidscardPhoto) {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.update(baseKidscardPhoto, COLUMN_KIDSCARD_PHOTO_PATH);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询所有为发送图片
     */
    public List<BaseKidsCardTakePhoto> findPhotoRecord() {
        List<BaseKidsCardTakePhoto> photoList = null;
        try {
            photoList = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findAll(BaseKidsCardTakePhoto.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return photoList;
    }


    public void deleteFailePhoto(BaseKidsCardTakePhoto photo) {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.delete(photo);
        } catch (Exception e) {
        }
    }


    /**
     * 保存体温记录
     *
     * @param temperatureInfo
     * @return
     */
    public boolean saveTemperatureRecord(final Context context, BaseTemperatureInfo temperatureInfo) {
        if (null == temperatureInfo) {
            return false;
        }
        boolean success = false;
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.save(temperatureInfo);
            SendTemperature.getInstance().send();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }


    /**
     * 查询体温记录中第一条数据
     *
     * @return
     */
    public BaseTemperatureInfo findFirstTemperatureInfo() {
        BaseTemperatureInfo temperatureInfo = null;
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().beginTransaction();
            temperatureInfo = DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.findFirst(Selector.from(BaseTemperatureInfo.class).orderBy(COLUMN_LOCALID, true));
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.getDatabase().endTransaction();
        }

        return temperatureInfo;
    }

    /**
     * 删除体温记录
     *
     * @param localId
     * @return
     */
    public boolean deleteTemperatureInfo(String localId) {
        try {
            DAOHelperSchool.getInstance(MyApplication.getInstance()).mDBUtils.delete(BaseTemperatureInfo.class, WhereBuilder.b(COLUMN_LOCALID, "=", localId));
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }
}
