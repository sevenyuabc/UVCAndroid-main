package com.cicada.kidscard.storage.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.exception.DbException;
import com.cicada.kidscard.storage.db.model.BaseAreaThroughRule;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsCardOrderTop;
import com.cicada.kidscard.storage.db.model.BaseKidsCardRecord;
import com.cicada.kidscard.storage.db.model.BaseKidsCardTakePhoto;
import com.cicada.kidscard.storage.db.model.BaseKidsFaceInfo;
import com.cicada.kidscard.storage.db.model.BaseKidsVerifyLog;

/**
 * 用户数据库操作类进行封装
 */
public class DAOHelperSchool implements DAOConstants, DbUpgradeListener {
    /**
     * 用户数据库名字
     */
    private static final String DATABASE_NAME = "db_cicada_kidscard";

    private static final int DATABASE_VERSION = 10;

    public DbUtils mDBUtils;
    private static volatile DAOHelperSchool instance = null;

    public DAOHelperSchool(Context context) {
        mDBUtils = DbUtils.create(context, DATABASE_NAME, DATABASE_VERSION, this);
        mDBUtils.configAllowTransaction(true);
        try {
            mDBUtils.createTableIfNotExist(BaseKidsCardChildInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDBUtils.createTableIfNotExist(BaseKidsCardRecord.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        try {
            mDBUtils.createTableIfNotExist(BaseKidsCardTakePhoto.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        try {
            mDBUtils.createTableIfNotExist(BaseKidsCardOrderTop.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mDBUtils.createTableIfNotExist(BaseKidsFaceInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mDBUtils.createTableIfNotExist(BaseKidsVerifyLog.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mDBUtils.createTableIfNotExist(BaseAreaThroughRule.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单一实例
     */
    public static DAOHelperSchool getInstance(Context context) {
        if (instance == null) {
            synchronized (DAOHelperSchool.class) {
                if (instance == null) {
                    instance = new DAOHelperSchool(context);
                }
            }
        }
        return instance;
    }

    /**
     * 是否存在数据库
     *
     * @return
     */
    public static boolean isCreate() {
        return instance != null;
    }


    @Override
    public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
        try {
            if (newVersion > oldVersion) {
                Log.d("hwp", "========update==oldVersion" + oldVersion + " newVersion：" + newVersion);
                upgrade(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkColumnExist(DbUtils db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            //查询一行
            String sql = "SELECT * FROM " + tableName + " LIMIT 0";
            Log.d("======sql=====", sql);
            cursor = db.execQuery(sql);
            result = cursor != null && cursor.getColumnIndex(columnName) != -1;
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        Log.d("====checkColumnExist==", ">>" + result);
        return result;
    }

    public void upgrade(DbUtils db) throws DbException {

        //8-9
        if (!checkColumnExist(db, DAOConstants.TABLE_BASE_KIDSCARDRECORD, DAOConstants.COLUMN_KIDSCARDRECORD_ENTRANCE)) {
            db.execNonQuery("alter table " + DAOConstants.TABLE_BASE_KIDSCARDRECORD + " add  column " + DAOConstants.COLUMN_KIDSCARDRECORD_ENTRANCE + " TEXT;");
        }

        if (!checkColumnExist(db, DAOConstants.TABLE_BASE_KIDSCARDRECORD, DAOConstants.COLUMN_KIDSCARDRECORD_AREAID)) {
            db.execNonQuery("alter table " + DAOConstants.TABLE_BASE_KIDSCARDRECORD + " add  column " + DAOConstants.COLUMN_KIDSCARDRECORD_AREAID + " TEXT;");
        }

        // 9-10
        if (!checkColumnExist(db, DAOConstants.TABLE_BASE_KIDSFACEINFO, DAOConstants.COLUMN_KIDS_FACEINFO_TRAFFICSTATUS)) {
            db.execNonQuery("alter table " + DAOConstants.TABLE_BASE_KIDSFACEINFO + " add column " + DAOConstants.COLUMN_KIDS_FACEINFO_TRAFFICSTATUS + " TEXT;");
        }
        // 10-11
        if (!checkColumnExist(db, DAOConstants.TABLE_BASE_KIDSFACEINFO, DAOConstants.KIDS_FACEINFO_TEACHERID)) {
            db.execNonQuery("alter table " + DAOConstants.TABLE_BASE_KIDSFACEINFO + " add column " + DAOConstants.KIDS_FACEINFO_TEACHERID + " TEXT;");
        }
    }

}
