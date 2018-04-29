package com.lxr.framework;

public class Subject {
	
	public static final int LOGIN_TYPE_SMS = 0;
	
	public static final int LOGIN_TYPE_PWD = 1;
	//自动登录
	public static final int LOGIN_TYPE_AUTO = 2;
	
	Object id;
	
	String account;
	
	String pwd;
	
	String token;
	
	String sessionId;
	
	String vcode;
	
	Integer loginType;

	
	
	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	
	
	

}
