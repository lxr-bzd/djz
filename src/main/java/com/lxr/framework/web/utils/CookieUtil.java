package com.lxr.framework.web.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CookieUtil {


	
	public static void setCookie(HttpServletResponse response,String key,String value,String path) {
		Cookie cookie = new Cookie(key, value);
		if(StringUtils.isEmpty(path))
			cookie.setPath(path);
		else 
		cookie.setPath("/");
		response.addCookie(new Cookie(key, value));
	}
	
	
	public static String getCookie(HttpServletRequest request,String key,String path) {
		
		Cookie[] cookies =  request.getCookies();
		
		if(cookies==null)
			return null;
		if(StringUtils.isEmpty(path))path = "/";
		for (Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals(key)&&equalsPath(path,cookie.getPath())){
				
				return cookie.getValue();
			}
				
		} 
		return null;
	}
	
	
	public static void delCookie(HttpServletResponse response,String key,String path) {
	 Cookie cookie = new Cookie(key,"");
		cookie.setMaxAge(0);
		response.addCookie(cookie);

	}
	
	private static boolean equalsPath(String mainPath,String path) {
		if(StringUtils.isEmpty(mainPath))
			mainPath="/";
		if(StringUtils.isEmpty(path))
			path ="/";
		
		
		return mainPath.equals(path);

	}
	
	
	public static void updateCookie(String key,String val,HttpServletRequest request) {
		for (Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals(key)){
				cookie.setValue(val);
				return ;
			}
				
		} 

	}
	
}
