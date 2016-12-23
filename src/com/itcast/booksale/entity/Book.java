package com.itcast.booksale.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 书
 * 
 * @author Administrator
 *
 */

public class Book implements Serializable {
	// 用户
	User user;

	// 发布出售日期与编辑日期
	Date createDate;
	Date editDate;

	// 图书ID
	Integer id;

	// 图书标题
	private String title;
	// 图书作者
	private String author;
	// 图书出版社
	private String publisher;
	// 图书ISBN码
	private String isbn;
	// 图书价格
	private float price;
	// 图书标签
	private String tag;
	// 图书摘要
	private String summary;
	// 卖家备注
	private String text;
	//图书照片
	private String bookavatar;
	// 书本数量
	private int booknumber;
	
	

	public int getBooknumber() {
		return booknumber;
	}

	public void setBooknumber(int booknumber) {
		this.booknumber = booknumber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public String getBookavatar() {
		return bookavatar;
	}
	public void setBookavatar(String bookavatar) {
		this.bookavatar = bookavatar;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
