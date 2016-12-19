package com.itcast.booksale.entity;

import java.util.Date;

/**
 * ������Ϣ
 * @author Administrator
 *
 */
public class Comment {
	
	private int id;
	private User user;
	private String content;       //��������
	private Date createDate;      //���۴���ʱ��
	private Date editDate;      //���۱༭ʱ��
	private Book book;          //�鼮��Ϣ
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
}
