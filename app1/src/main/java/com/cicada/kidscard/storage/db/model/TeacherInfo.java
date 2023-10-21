package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class TeacherInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private String userId;
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


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
