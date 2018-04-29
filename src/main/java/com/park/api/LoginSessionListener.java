package com.park.api;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import com.lxr.framework.Subject;
import com.park.api.service.SecurityService;


/**
 * session监听
 * @author Administrator
 *
 */
public class LoginSessionListener implements HttpSessionListener {  
	
    public void sessionCreated(HttpSessionEvent event) {  
    	
    	System.out.println("session生成："+event.getSession().getId());
    }  
    
    
   public void sessionDestroyed(HttpSessionEvent event) {   
        HttpSession session = event.getSession();  
        
        
        Object o = session.getAttribute(SecurityService.SUBJECT_KEY);
        
        Subject s  = null;
        
        if(o instanceof Subject)
        s = (Subject)o;
       
        
      //  Subject s = ServiceManage.securityService.outoLogout(session);;
       
        System.out.println(session.getId()+":销毁");
        if(s!=null){
        	
        	   System.out.println(s.getAccount()+ "超时退出。");  
        }
      
    }   

   
}    