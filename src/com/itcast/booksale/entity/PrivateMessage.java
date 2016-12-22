package com.itcast.booksale.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown=true)

public class PrivateMessage {
	User privateMessageSender;//发送者
	User privateMessageReceiver;//接收者
	Date createDate;//发送时间
	int id;//私信id
	String privateText;//私信的内容
	public User getPrivateMessageSender() {
		return privateMessageSender;
	}
	public void setPrivateMessageSender(User privateMessageSender) {
		this.privateMessageSender = privateMessageSender;
	}
	public User getPrivateMessageReceiver() {
		return privateMessageReceiver;
	}
	public void setPrivateMessageReceiver(User privateMessageReceiver) {
		this.privateMessageReceiver = privateMessageReceiver;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getPrivateText() {
		return privateText;
	}
	public void setPrivateText(String privateText) {
		this.privateText = privateText;
	}


	
}
