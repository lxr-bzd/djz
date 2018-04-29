package com.lxr.framework;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;



public class TomcatListener implements ServletContextListener{


    /**
     * 监听web容器关闭
     */
   
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("web容器关闭");

    }

    /**
     * 监听web容器启动
     */
    
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("web容器初始化。。。。");
        WebContext.WEB_ROOT =  sce.getServletContext().getRealPath("/");
        WebContext.servletContext = sce.getServletContext();
        
      /*  try {
        	System.out.println("RM服务启动...");
        	
			ParkcloudServiceFactroy.putService(new HctcServiceImpl(), HctcService.class.getSimpleName());
			
		} catch (RemoteException | MalformedURLException e) {
			e.printStackTrace();
		}*/
        
        
        
    }

}