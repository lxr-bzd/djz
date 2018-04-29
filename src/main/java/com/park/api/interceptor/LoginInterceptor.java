package com.park.api.interceptor;


import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;  
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.alibaba.fastjson.JSONObject;
import com.lxr.framework.long1.JsonResult;
import com.park.api.ServiceManage;
import com.park.api.bean.JsonResoleResponseBodyAdvice;
import com.park.api.common.exception.TransmittalException;
import com.park.api.service.SecurityService;
import com.park.api.service.UserService;
 
  
  
/** 
 * @author lxr 
 * 2014-8-1 
 */  
public class LoginInterceptor extends HandlerInterceptorAdapter{  
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);   
    
    @Autowired
    SecurityService securityService;
     
    @Autowired
    ServiceManage serviceManage;
    
    @Autowired
    JsonResoleResponseBodyAdvice jsonResoleResponseBodyAdvice;
    
    @Autowired
    UserService userService;
    
    /**  
     * 在业务处理器处理请求之前被调用  
     * 如果返回false  
     *     从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 
     * 如果返回true  
     *    执行下一个拦截器,直到所有的拦截器都执行完毕  
     *    再执行被拦截的Controller  
     *    然后进入拦截器链,  
     *    从最后一个拦截器往回执行所有的postHandle()  
     *    接着再从最后一个拦截器往回执行所有的afterCompletion()  
     */    
    @Override
    public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler) throws Exception {    
        
      System.out.println(request.getRequestURI());
     
      try {
    	  if(!securityService.isLogin()) {
    		  
    		  response.setContentType("text/html;charset=UTF-8");
              response.getWriter().print(JSONObject.toJSONString(JsonResult.getResult(JsonResult.STATUS_UN_AUTH,"无权限，请重新登录")));
              return false;
    	  }
    	  
    	  String lockInfo = userService.getLockInfo(securityService.getSessionSubject().getAccount());
    	  if(lockInfo!=null) {
    		  response.setContentType("text/html;charset=UTF-8");
              response.getWriter().print(JSONObject.toJSONString(JsonResult.getResult(JsonResult.STATUS_LOCK,lockInfo)));
              return false;
    	  }
       	   return true;
    	  
    	  
	} catch (TransmittalException e) {
		//处理并发多次请求重复登录
		response.setContentType("text/html;charset=UTF-8");
	   response.getWriter().print(JSONObject.toJSONString(JsonResult.getResult(JsonResult.STATUS_TRANSMITTAL,e.getMessage())));
	   
	}catch (Exception e) {
		e.printStackTrace();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(JSONObject.toJSONString(JsonResult.getResult(JsonResult.STATUS_FAIL,"拦截未知异常")));
		
	}
      
     
      return false;
        
    }    
    
    /** 
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作    
     * 可在modelAndView中加入数据，比如当前时间 
     */  
    @Override    
    public void postHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler,    
            ModelAndView modelAndView) throws Exception {     
            
    }    
    
    /**  
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等   
     *   
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()  
     */    
    @Override    
    public void afterCompletion(HttpServletRequest request,    
            HttpServletResponse response, Object handler, Exception ex)    
            throws Exception {    
           
    }    
  
}    
