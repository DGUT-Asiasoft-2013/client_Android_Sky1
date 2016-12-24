package com.itcast.booksale.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//此处应该跟网络上的一样，网上下载的网页中
//泛型,@JsonIgnoreProperties is to Ignore something you dont't want Properties
@JsonIgnoreProperties(ignoreUnknown=true)
public class Page<T> {
	List<T> content;
	Integer number;
	Integer numberOfElements;
	
	public Integer getNumberOfElements() {
		return numberOfElements;
	}
	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
}
	
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}


