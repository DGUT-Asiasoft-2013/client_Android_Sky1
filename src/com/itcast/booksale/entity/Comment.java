package com.itcast.booksale.entity;

import java.util.Date;

/**
 * 评论实现类
 * @author Administrator
 *
 */
public class Comment {
	
	private int id;

	private User commentor;                
	private String content;       //评论内容
	private Date createDate;      //评论创建时间
	private Date editDate;      //评论编辑时间
	private Book book;          //书信息

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public User getCommentor() {
		return commentor;
	}
	public void setCommentor(User commentor) {
		this.commentor = commentor;
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
