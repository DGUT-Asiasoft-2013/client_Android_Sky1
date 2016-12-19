package com.itcast.booksale.entity;

public class User {

	String account;
	String passwordHash;
	String name;
	String avatar;
	String email;
	String phoneNumb;//手机号必填(联系方法)
	String qq;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumb() {
		return phoneNumb;
	}
	public void setPhoneNumb(String phoneNumb) {
		this.phoneNumb = phoneNumb;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
}
