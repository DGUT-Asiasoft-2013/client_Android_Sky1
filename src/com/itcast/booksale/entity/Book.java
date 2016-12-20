package com.itcast.booksale.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 图书类信息
 * @author Administrator
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Book implements Serializable{

	//用户
	User user;

	//发布出售日期与编辑日期
	Date createDate;
	Date editDate;

	//图书ID
	Integer id;

	//图书标题
	private String Title;	
	//图书作者
	private String Author;
	//作者信息
	private String AuthorInfo;
	//图书出版社
	private String Publisher;
	//出版时间
	private String PublishDate;
	//图书ISBN码
	private String ISBN;
	//图书价格
	private String Price;
	//图书页数
	private String Page;
	//图书评分
	private String Rate;
	//图书标签
	private String Tag;
	//图书目录
	private String Content;
	//图书摘要
	private String Summary;



	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public String getAuthorInfo() {
		return AuthorInfo;
	}
	public void setAuthorInfo(String authorInfo) {
		AuthorInfo = authorInfo;
	}
	public String getPublisher() {
		return Publisher;
	}
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	public String getPublishDate() {
		return PublishDate;
	}
	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public String getPage() {
		return Page;
	}
	public void setPage(String page) {
		Page = page;
	}
	public String getRate() {
		return Rate;
	}
	public void setRate(String rate) {
		Rate = rate;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getSummary() {
		return Summary;
	}
	public void setSummary(String summary) {
		Summary = summary;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


}
