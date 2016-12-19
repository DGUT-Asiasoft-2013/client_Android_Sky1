package com.itcast.booksale.entity;

import java.util.Date;

public class Comment {
	
	User user;
	String content;       //评论内容
	Date createDate;      //评论创建时间
	Date editDate;      //评论编辑时间
    Book book;          //书籍信息
}
