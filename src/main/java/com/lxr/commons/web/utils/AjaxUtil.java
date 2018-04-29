package com.lxr.commons.web.utils;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class AjaxUtil {
/**
* ajax response method<br/>
* 如果出现异常，则返回错误代码505
*
* @param response
* @param text
* ajax返回的内容
*
*/
public static void ajaxResponse(HttpServletResponse response, String text) {
		try {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(text);
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		/**
		* <pre>
		* 注意：ajax请求必须携带：jsonnp请求callback
		*
		* eg:
		* $.getJSON(path + "ajax/news/comment.go?newsId=" + newsId + "&content=" +content+ "&callback=?", function(data) {
		* alert(data['val']);
		* });
		*
		* </pre>
		*
		* @param response
		* @param msg
		*/
		public static void jsonnpResponse(HttpServletResponse response,
		String callback, String msg) {
		try {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(callback + "(" + msg + ")");
		} catch (IOException e) {
		}
		}
		
		
		public static void stringResponse(HttpServletResponse response,
				String str) {
				try {
				response.setContentType("text/html;charset=utf-8");
				
				response.getWriter().write(str);
				} catch (IOException e) {
				}
		}
		
		public static void jsonResponse(HttpServletResponse response,
				Object obj) {
				try {
				response.setContentType("text/html;charset=utf-8");
				String v = JSONObject.fromObject(obj).toString();
				response.getWriter().write(v);
				} catch (IOException e) {
				}
		}
		/**
		* 异常操作，需要浏览器地址返回上一步
		*
		* @param response
		* @param alertMsg
		* 弹出提示信息
		*/
		public static void ajaxResponseRollBack(HttpServletResponse response,
		String msg) {
		try {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(
		"<script type='text/javascript'>alert('" + msg
		+ "');window.history.back();</script>");
		} catch (IOException e) {
		// LogConsoleUtil.write(Common.class, "ajaxResponseRollBack()",
		// e.getMessage());
		}
		}
		// ajaxJSON返回
		public static void ajaxJSONResponse(HttpServletResponse response,
		String str, Object object) {
		response.setContentType("text/json;charset=utf-8");
		JSONObject json = new JSONObject();
		json.put(str, object);
		try {
		response.getWriter().write(json.toString());
		} catch (IOException e) {
		e.printStackTrace();
		}
		}
		public static void ajaxResponseAlert(HttpServletResponse response,
		String msg) {
		try {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(
		"<script type='text/javascript'>alert('" + msg
		+ "');</script>");
		} catch (IOException e) {
		// LogConsoleUtil.write(Common.class, "ajaxResponseRollBack()",
		// e.getMessage());
		}
		}
		public static Integer StringToInteger(HttpServletRequest request,
		String name) {
		String obj = request.getParameter(name);
		Integer result = -1;
		if (obj != null) {
		result = Integer.parseInt(obj);
		}
		return result;
		}
		
		
		public static void main(String[] args) {
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println(JSONObject.fromObject(map));
			
		}
		
		
		}


