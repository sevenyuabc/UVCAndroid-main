package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.cicada.kidscard.storage.db.DAOConstants;

import java.io.Serializable;

/**
 * 区域通行规则
 */
@Table(name = DAOConstants.TABLE_BASE_AREA_THROUGH_RULE)
public class BaseAreaThroughRule extends EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(column = DAOConstants.COLUMN_AREA_THROUGH_RULE_USERID)
    private String userId;

    @Column(column = DAOConstants.COLUMN_AREA_THROUGH_RULE_CHILDID)
    private String childId;

    @Column(column = DAOConstants.COLUMN_AREA_THROUGH_RULE_START_TIME_STR)
    private String startTimeStr;

    @Column(column = DAOConstants.COLUMN_AREA_THROUGH_RULE_END_TIME_STR)
    private String  endTimeStr;

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

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
