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
@Table(name = DAOConstants.TABLE_BASE_TAKEPHOTO)
public class BaseKidsCardTakePhoto extends EntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 图片记录地id
	 */
	@Unique
	@Column(column = DAOConstants.COLUMN_KIDSCARD_TAKEPHOTO_ID)
	private String recordId;

	/**
	 * 图片记录地id
	 */
	@Unique
	@Column(column = DAOConstants.COLUMN_KIDSCARD_TAKEPHOTO_FACE_ID)
	private String faceRecordId;
	/**
	 * 记录信息
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARD_TAKEPHOTO_INFO)
	private String info;
	/**
	 * 班级ID
	 */
	@Column(column = DAOConstants.COLUMN_KIDSCARD_PHOTO_PATH)
	private String photoPath;

	public String getFaceRecordId() {
		return faceRecordId;
	}

	public void setFaceRecordId(String faceRecordId) {
		this.faceRecordId = faceRecordId;
	}

	public String getRecordId() {
		return recordId;
	}


	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}



	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
