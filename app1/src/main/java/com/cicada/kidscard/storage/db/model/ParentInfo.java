package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
/**
 * 家庭成员
 * @author hwp
 *
 */
public class ParentInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户名字
	 */
	private String userName;
	/**
	 * 用户头像
	 */
	private String userIcon;
	/**
	 * 用户介绍
	 */
	private String userIntro;
	/**
	 * 用户性别 男、女
	 */
	private String userSex;
	/**
	 * 用户班级
	 */
	private int classId;
	
	private String phoneNum;
	/**
	 * 与孩子关系
	 */
	private String relation;
	/**
	 * 是否已经推班
	 */
	private boolean userHasExitClass;

	/**
	 * 人脸特征值
	 */
	private String faceFeature;
	

	public void setUserId(long value) {
		this.userId = value;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserName(String value) {
		this.userName = value;
	}

	public String getUserName() {
		if (this.userName == null) {
			return "";
		}
		return this.userName;
	}

	public void setUserIntro(String value) {
		this.userIntro = value;
	}

	public String getUserIntro() {
		if (this.userIntro == null) {
			return "";
		}
		return this.userIntro;
	}

	public void setUserIcon(String value) {
		this.userIcon = value;
	}

	public String getUserIcon() {
		if (this.userIcon == null) {
			return "";
		}
		return this.userIcon;
	}

	public void setUserSex(String value) {
		this.userSex = value;
	}

	public String getUserSex() {
		if (this.userSex == null) {
			return "";
		}
		return this.userSex;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public boolean isUserHasExitClass() {
		return userHasExitClass;
	}

	public void setUserHasExitClass(boolean userHasExitClass) {
		this.userHasExitClass = userHasExitClass;
	}

	public String getFaceFeature() {
		return faceFeature;
	}

	public void setFaceFeature(String faceFeature) {
		this.faceFeature = faceFeature;
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(this);
	}

	

	
}
