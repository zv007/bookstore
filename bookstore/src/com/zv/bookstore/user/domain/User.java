package com.zv.bookstore.user.domain;

/**
 * User的领域对象
 * @author zv
 *
 */
public class User {
	/*
	 * 对应数据库表
	 * uid CHAR(32) PRIMARY KEY,主键
	  username VARCHAR(50) NOT NULL,用户名
	  `password` VARCHAR(50) NOT NULL,密码
	  email VARCHAR(50) NOT NULL,邮箱
	  `code` CHAR(64) NOT NULL,激活码
	  state BOOLEAN,用户状态，有两种是否激活
	 */
	private String uid;
	private String username;
	private String password;
	private String email;
	private String code;
	private boolean state;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String uid, String username, String password, String email, String code, boolean state) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.email = email;
		this.code = code;
		this.state = state;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", email=" + email + ", code="
				+ code + ", state=" + state + "]";
	}
	
	
}
