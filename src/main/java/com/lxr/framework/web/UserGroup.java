package com.lxr.framework.web;

import javax.ws.rs.GET;

import net.sf.jsqlparser.statement.update.Update;

/*
 * 装载已登录的用户
 */
public interface UserGroup<T> {
	
	
	
	void add(T obj);
	
	
	
	void remove(String account);
	
	
	T get(String account);
	
	
	T get(String sessionid,String token);
	
	
	void update(String account);
	
	
	

}
