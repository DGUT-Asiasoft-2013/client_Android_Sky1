package com.itcast.booksale.entity;

import java.io.Serializable;
import java.util.Date;


public class Money implements Serializable{

	private Integer id;
	private Date createDate;
	private Date editDate;
	String current_user;
	float recharge;
	float sumMoney;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public String getCurrent_user() {
		return current_user;
	}
	public void setCurrent_user(String current_user) {
		this.current_user = current_user;
	}
	public float getRecharge() {
		return recharge;
	}
	public void setRecharge(float recharge) {
		this.recharge = recharge;
	}
	public float getSumMoney() {
		return sumMoney;
	}
	public void setSumMoney(float sumMoney) {
		this.sumMoney = sumMoney;
	}
	
	
}
