package com.park.api.bean;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.lxr.framework.long1.JsonResult;
import com.lxr.framework.web.utils.RequestUtil;

@ControllerAdvice(basePackages = "com.park.api.controller")
public class JsonResoleResponseBodyAdvice implements ResponseBodyAdvice{

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		
			return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		if(!(body instanceof JsonResult))return body;
		
		JsonResult result = (JsonResult)body;
		if(RequestUtil.getRequest().getSession(false)!=null){
		result.setJSESSIONID(RequestUtil.getSession().getId());
		
		result.setToken(RequestUtil.getRequest().getAttribute("token"));
		}
		return result;
	}
	
	
	
}
