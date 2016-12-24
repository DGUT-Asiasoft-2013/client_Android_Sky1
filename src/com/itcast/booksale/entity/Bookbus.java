package com.itcast.booksale.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Bookbus {

	public static class Bus_Key implements Serializable {
		// 用户
		User user;
		Book book; // 书
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Book getBook() {
			return book;
		}
		public void setBook(Book book) {
			this.book = book;
		}
		
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Bus_Key){
				Bus_Key other = (Bus_Key)obj;
				return book.getId() == other.book.getId() && user.getId() == other.user.getId();
			}else{
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return book.getId();
         }
	}

	/*
	 * 把这个属性弄成id,不然客户端识别的时候会发现有没有识别的属性id
	 * to this as id ,because client will find that it don't hava the Properties id
	 */
	Bus_Key id;
	public Bus_Key getId() {
		return id;
	}
	public void setId(Bus_Key id) {
		this.id = id;
	}
	Date createDate;
	
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
