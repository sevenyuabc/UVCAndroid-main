package com.cicada.kidscard.storage.db.model;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.storage.db.DAOConstants;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

import java.io.Serializable;

/**
 * 进校排名
 * <p/>
 * 创建时间: 2015年6月25日 下午3:07:09 <br/>
 * 
 * @author hwp
 * @version
 * @since v0.0.1
 */
@Table(name = DAOConstants.TABLE_BASE_ORDER_TOP)
public class BaseKidsCardOrderTop extends EntityBase implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@Unique
	@Column(column = DAOConstants.COLUMN_KIDSCARD_ORDER_TOP_CARDNUMBER)
	private String cardNumber;
	
	
	@Column(column = DAOConstants.COLUMN_KIDSCARD_ORDER_TOP)
	private String orderTop;
	

	public String getOrderTop() {
		return orderTop;
	}

	public void setOrderTop(String orderTop) {
		this.orderTop = orderTop;
	}
	

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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
