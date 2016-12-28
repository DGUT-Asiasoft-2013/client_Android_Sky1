package com.itcast.booksale.entity;

import java.io.Serializable;
import java.util.Date;



public class Subscribe{
	boolean b;
	int count;
	public boolean isB() {
		return b;
	}

	public void setB(boolean b) {
		this.b = b;
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public static class Key implements Serializable{
		User me;
		User saler;
		public User getMe() {
			return me;
		}
		public void setMe(User me) {
			this.me = me;
		}
		public User getSaler() {
			return saler;
		}
		public void setSaler(User saler) {
			this.saler = saler;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Key){
				Key other = (Key)obj;
				return saler.getId() == other.saler.getId() && me.getId() == other.me.getId();
			}else{
				return false;
			}
		}
		@Override
		public int hashCode() {
			return saler.getId();
		}
		
	
	}
	
	Key id;
	Date createDate;
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	void onPrePersist(){
		createDate = new Date();
	}
}