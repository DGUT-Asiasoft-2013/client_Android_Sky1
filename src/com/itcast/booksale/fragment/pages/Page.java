package com.itcast.booksale.fragment.pages;

import java.util.List;

//此处应该跟网络上的一样，网上下载的网页中
public class Page<Comment> {
        //id
		Integer number;
		List<Comment> content;
		public Integer getNumber() {
			return number;
		}
		public void setNumber(Integer number) {
			this.number = number;
		}
		public List<Comment> getContent() {
			return content;
		}
		public void setContent(List<Comment> content) {
			this.content = content;
		}

}
