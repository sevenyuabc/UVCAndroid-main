package com.cicada.kidscard.business.setting.domain;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.storage.preferences.BaseSharePreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 学校信息对象
 * <p/>
 * 创建时间: 2014年11月17日 下午5:03:32 <br/>
 */
public class SchoolInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String schoolId;
	private String schoolName;
	private String schoolIcon;
	private String cityName;
	private String sociologyCreditCode;//社会信用代码
	private long schoolTypeId;
	private List<Long> schoolTypeIds = new ArrayList<Long>();
	private String deviceName;
	private String areaId;
	private String areaName;
	private String deviceSn;
	private List<AreaInfo>  areaDatas = new ArrayList<>();
	/**
	 * 学校类型 zl xys xyb
	 */
	private String source;


	public SchoolInfo() {
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		if (this.schoolName == null) {
			return "";
		}
		return schoolName;
	}

	public String getSociologyCreditCode() {
		return sociologyCreditCode;
	}

	public void setSociologyCreditCode(String sociologyCreditCode) {
		this.sociologyCreditCode = sociologyCreditCode;
	}

	public String getDeviceName() {
		if(TextUtils.isEmpty(deviceName)){
			deviceName = this.schoolName + BaseSharePreference.getInstance().getSn();
		}
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolIcon() {
		return schoolIcon;
	}

	public void setSchoolIcon(String schoolIcon) {
		this.schoolIcon = schoolIcon;
	}

	public long getSchoolTypeId() {
		return schoolTypeId;
	}

	public void setSchoolTypeId(long schoolTypeId) {
		this.schoolTypeId = schoolTypeId;
	}

	public List<AreaInfo> getAreaDatas() {
		return areaDatas;
	}

	public void setAreaDatas(List<AreaInfo> areaDatas) {
		this.areaDatas = areaDatas;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public List<Long> getSchoolTypeIds() {
		return schoolTypeIds;
	}

	public void setSchoolTypeIds(List<Long> schoolTypeIds) {
		this.schoolTypeIds = schoolTypeIds;
	}


	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public boolean isZLSchool(){
		return  "zl".equalsIgnoreCase(this.source);
	}

	public boolean isXYSSchool(){
		return  "xys".equalsIgnoreCase(this.source);
	}

	public boolean isXYBSchool(){
		return  "xyb".equalsIgnoreCase(this.source);
	}
}
