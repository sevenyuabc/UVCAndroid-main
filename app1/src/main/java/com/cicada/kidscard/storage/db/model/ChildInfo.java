package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 家庭成员
 *
 * @author hwp
 */
public class ChildInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String childId;
    /**
     * 用户名字
     */
    private String childName;
    private String childNamePinyin;
    /**
     * 用户头像
     */
    private String childIcon;
    /**
     * 用户classid
     */
    private String childClassId;
    /**
     * 特权用户
     */
    private int isVipChild;
    /**
     * 用户性别 男、女
     */
    private String childSex;
    /**
     * 用户班级
     */
    private String childClassName;
    /**
     * 用户班级别称
     */
    private String childCustomName;

    /**
     * 用户学校名称
     */
    private String childSchoolName;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getChildCustomName() {
        return childCustomName;
    }

    public void setChildCustomName(String childCustomName) {
        this.childCustomName = childCustomName;
    }

    public String getChildNamePinyin() {
        return childNamePinyin;
    }

    public void setChildNamePinyin(String childNamePinyin) {
        this.childNamePinyin = childNamePinyin;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildIcon() {
        return childIcon;
    }

    public void setChildIcon(String childIcon) {
        this.childIcon = childIcon;
    }

    public String getChildClassId() {
        return childClassId;
    }

    public void setChildClassId(String childClassId) {
        this.childClassId = childClassId;
    }

    public String getChildSex() {
        return childSex;
    }

    public void setChildSex(String childSex) {
        this.childSex = childSex;
    }

    public String getChildClassName() {
        return childClassName;
    }

    public void setChildClassName(String childClassName) {
        this.childClassName = childClassName;
    }

    public String getChildSchoolName() {
        return childSchoolName;
    }

    public void setChildSchoolName(String childSchoolName) {
        this.childSchoolName = childSchoolName;
    }


    public int getIsVipChild() {
        return isVipChild;
    }

    public void setIsVipChild(int isVipChild) {
        this.isVipChild = isVipChild;
    }

}
