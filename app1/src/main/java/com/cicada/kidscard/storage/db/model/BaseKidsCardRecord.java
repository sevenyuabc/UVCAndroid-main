package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;
import com.cicada.kidscard.storage.db.DAOConstants;

import java.io.Serializable;

/**
 * 孩子考勤纪录提交类
 * <p/>
 * 创建时间: 2015年6月25日 下午3:07:09 <br/>
 * 
 * @author hwp
 * @version
 * @since v0.0.1
 */
@Table(name = DAOConstants.TABLE_BASE_KIDSCARDRECORD)
public class BaseKidsCardRecord extends EntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 本地缓编号
	 */
	@Unique
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_LOCALID)
	private String localId;
	/**
	 * 学校ID
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_SCHOOLID)
	private String schoolId;
	/**
	 * 班级ID
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CLASSID)
	private String classId;
	/**
	 * 孩子ID
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CHILDID)
	private String targetId;
	/**
	 * 用户ID
	 */
	@Column(column = DAOConstants.COLUMN_KIDS_FACEINFO_USERID)
	private String userId;
	/**
	 * 孩子Icon
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CHILDICON)
	private String childIcon;
	/**
	 * 孩子名称
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CHILDNAME)
	private String targetName;
	/**
	 * 刷卡卡号信息
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CARDNUMBER)
	private String cardNumber;
	/**
	 * 家长头像信息
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_USERICON)
	private String userIcon;
	
	/**
	 * 孩子测量体温
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_TEMPERATURE)
	private float temperature;
	
	/**
	 * 本地的刷卡时间信息
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_REQUESTDATE)
	private long requestDate;
	/**
	 * 孩子班级
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_CHILDCLASSID)
	private String childClassId;

	/**
	 * 学校入离园状态
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_SCHOOL_STATE)
	private String schoolState;

	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_IS_TEACHER_CARD)
	private int isTeacherCard;

	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_STATUS)
	private int status = 0;

	/**
	 * 1:代表通过 2：代表不通过
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_ENTRANCE)
	private int entrance = 1;

	/**
	 * 区域id
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARDRECORD_AREAID)
	private String areaId;

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
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

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChildClassId() {
		return childClassId;
	}

	public void setChildClassId(String childClassId) {
		this.childClassId = childClassId;
	}

	/**
	 * localId.
	 * 
	 * @return the {@link #localId}
	 * @see #localId
	 * @since v0.0.1
	 */
	public String getLocalId() {
		return localId;
	}

	/**
	 * localId.
	 * 
	 * @param localId
	 *            the {@link #localId} to set
	 * @see #localId
	 * @since v0.0.1
	 */
	public void setLocalId(String localId) {
		this.localId = localId;
	}



	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	/**
	 * childIcon.
	 * 
	 * @return the {@link #childIcon}
	 * @see #childIcon
	 * @since v0.0.1
	 */
	public String getChildIcon() {
		return childIcon;
	}

	/**
	 * childIcon.
	 * 
	 * @param childIcon
	 *            the {@link #childIcon} to set
	 * @see #childIcon
	 * @since v0.0.1
	 */
	public void setChildIcon(String childIcon) {
		this.childIcon = childIcon;
	}

	/**
	 * cardNumber.
	 * 
	 * @return the {@link #cardNumber}
	 * @see #cardNumber
	 * @since v0.0.1
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * cardNumber.
	 * 
	 * @param cardNumber
	 *            the {@link #cardNumber} to set
	 * @see #cardNumber
	 * @since v0.0.1
	 */
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	/**
	 * userIcon.
	 * 
	 * @return the {@link #userIcon}
	 * @see #userIcon
	 * @since v0.0.1
	 */
	public String getUserIcon() {
		return userIcon;
	}

	/**
	 * userIcon.
	 * 
	 * @param userIcon
	 *            the {@link #userIcon} to set
	 * @see #userIcon
	 * @since v0.0.1
	 */
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	
	/**
	 * temperature.
	 * @return  the {@link #temperature}
	 * @see #temperature
	 * @since   v0.0.1
	 */
	public float getTemperature() {
		return temperature;
	}

	
	/**
	 * temperature.
	 *
	 * @param   temperature  the {@link #temperature} to set
	 * @see #temperature
	 * @since   v0.0.1
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	/**
	 * requestDate.
	 * @return  the {@link #requestDate}
	 * @see #requestDate
	 * @since   v0.0.1
	 */
	public long getRequestDate() {
		return requestDate;
	}

	/**
	 * requestDate.
	 *
	 * @param   requestDate  the {@link #requestDate} to set
	 * @see #requestDate
	 * @since   v0.0.1
	 */
	public void setRequestDate(long requestDate) {
		this.requestDate = requestDate;
	}


	public String getSchoolState() {
		return schoolState;
	}

	public void setSchoolState(String schoolState) {
		this.schoolState = schoolState;
	}

	public int getIsTeacherCard() {
		return isTeacherCard;
	}

	public void setIsTeacherCard(int isTeacherCard) {
		this.isTeacherCard = isTeacherCard;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getEntrance() {
		return entrance;
	}

	public void setEntrance(int entrance) {
		this.entrance = entrance;
	}
}
