package com.park.api.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import com.lxr.commons.exception.ApplicationException;
import com.lxr.commons.exception.CallException;
import com.lxr.commons.exception.CheckException;
import com.lxr.framework.long1.JsonResult;
import com.park.api.common.exception.JoggleThirdPartyException;
import com.park.api.service.SecurityService;



public class BaseController{
	
	static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	@Autowired
	public SecurityService securityService;
	
	@ExceptionHandler
	@ResponseBody
	public Object exceptionHandler(Exception e) {
		
		if(e instanceof ApplicationException){
			
			//System.out.println("应用异常："+e.toString());
			String emString = "应用异常："+e.toString();
			
			
			StackTraceElement[] stackTraceElements = e.getStackTrace();
			if(stackTraceElements.length>0)
				emString+="\n"+stackTraceElements[0].toString();
			logger.warn(emString);
		}
			
		
		
		if(e instanceof CallException)
			return JsonResult.getResult(JsonResult.STATUS_CALL_ERROR,e.getMessage());
		
		if(e instanceof CheckException)
			return JsonResult.getResult(JsonResult.STATUS_INVALID_PARAMETER,e.getMessage());
		
		if(e instanceof JoggleThirdPartyException){
			System.out.println("第三方接口错误："+((JoggleThirdPartyException)e).result);
			return JsonResult.getResult(JsonResult.STATUS_FAIL,e.getMessage());
		}
		
		if(e instanceof RestClientException){
			System.out.println("网络访问错误："+e.getMessage());
			return JsonResult.getResult(JsonResult.STATUS_FAIL,"数据获取失败");
		}
	
		
		
		if(e instanceof ApplicationException)
			return JsonResult.getFailResult(e.getMessage());
		
		logger.error("严重异常：",e);
		//e.printStackTrace();
		
		
		return JsonResult.getFailResult("数据出错");

	}


	
	public Map<String, String> getParamMap(HttpServletRequest request) {
		
		Map<String, String[]> map = request.getParameterMap();
		
		Map<String, String> map2 = new HashMap<>();
		
		for (Map.Entry<String, String[]> entry : map.entrySet()) {  
			  
		    if(entry.getValue()!=null&&entry.getValue().length>0)
		    	map2.put(entry.getKey(), entry.getValue()[0]);
		  
		}
		
		
		return map2;

	}
	
	  public String getBodyForStr(HttpServletRequest request,  
	            String charEncoding){
		  if(StringUtils.isEmpty(charEncoding))
		  charEncoding = "utf-8";
		// 调用核心服务类接收处理请求
					InputStream inputStream;
					try {
						inputStream = request.getInputStream();
				
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charEncoding);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String str = null;
					StringBuffer reqXml = new StringBuffer();
					while ((str = bufferedReader.readLine()) != null) {
						reqXml.append(str);
					}
					return reqXml.toString();
					} catch (IOException e) {
						throw new ApplicationException(e);
					}
					
					
	    }  
	  
	 
	  
	

}
