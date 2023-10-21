package com.cicada.kidscard.business.setting.domain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 孩子考勤纪录提交类
 * <p/>
 * 创建时间: 2015年5月29日 上午10:16:36 <br/>
 *
 * @author hwp
 * @version 
 * @since v0.0.1
 */
public class KidsCardSchoolSetting implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 学校ID
	 */
	private String schoolId;
	/**
	 * 入校开始时间 固定格式 hh:mm:ss or hh:mm
	 */
	private String intoSchoolStartTime;
	/**
	 * 入校截至时间 固定格式 hh:mm:ss or hh:mm
	 */
	private String intoSchoolEndTime;
	/**
	 * 刷卡卡号信息 固定格式 hh:mm:ss or hh:mm
	 */
	private String leaveSchoolStartTime;
	/**
	 * 家长头像信息 固定格式 hh:mm:ss or hh:mm
	 */
	private String leaveSchoolEndTime;

	private  String areaId;
	private  String deviceSn;
	private  String deviceName;


	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * intoSchoolStartTime.
	 * @return  the {@link #intoSchoolStartTime}
	 * @see #intoSchoolStartTime
	 * @since   v0.0.1
	 */
	public String getIntoSchoolStartTime() {
		return intoSchoolStartTime;
	}

	/**
	 * intoSchoolStartTime.
	 *
	 * @param   intoSchoolStartTime  the {@link #intoSchoolStartTime} to set
	 * @see #intoSchoolStartTime
	 * @since   v0.0.1
	 */
	public void setIntoSchoolStartTime(String intoSchoolStartTime) {
		this.intoSchoolStartTime = intoSchoolStartTime;
	}

	/**
	 * intoSchoolEndTime.
	 * @return  the {@link #intoSchoolEndTime}
	 * @see #intoSchoolEndTime
	 * @since   v0.0.1
	 */
	public String getIntoSchoolEndTime() {
		return intoSchoolEndTime;
	}

	/**
	 * intoSchoolEndTime.
	 *
	 * @param   intoSchoolEndTime  the {@link #intoSchoolEndTime} to set
	 * @see #intoSchoolEndTime
	 * @since   v0.0.1
	 */
	public void setIntoSchoolEndTime(String intoSchoolEndTime) {
		this.intoSchoolEndTime = intoSchoolEndTime;
	}

	/**
	 * leaveSchoolStartTime.
	 * @return  the {@link #leaveSchoolStartTime}
	 * @see #leaveSchoolStartTime
	 * @since   v0.0.1
	 */
	public String getLeaveSchoolStartTime() {
		return leaveSchoolStartTime;
	}

	/**
	 * leaveSchoolStartTime.
	 *
	 * @param   leaveSchoolStartTime  the {@link #leaveSchoolStartTime} to set
	 * @see #leaveSchoolStartTime
	 * @since   v0.0.1
	 */
	public void setLeaveSchoolStartTime(String leaveSchoolStartTime) {
		this.leaveSchoolStartTime = leaveSchoolStartTime;
	}

	/**
	 * leaveSchoolEndTime.
	 * @return  the {@link #leaveSchoolEndTime}
	 * @see #leaveSchoolEndTime
	 * @since   v0.0.1
	 */
	public String getLeaveSchoolEndTime() {
		return leaveSchoolEndTime;
	}

	/**
	 * leaveSchoolEndTime.
	 *
	 * @param   leaveSchoolEndTime  the {@link #leaveSchoolEndTime} to set
	 * @see #leaveSchoolEndTime
	 * @since   v0.0.1
	 */
	public void setLeaveSchoolEndTime(String leaveSchoolEndTime) {
		this.leaveSchoolEndTime = leaveSchoolEndTime;
	}
}
