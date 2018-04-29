package com.park.api.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;  
import java.util.Properties;

import com.park.api.ServiceManage;  
   
public class ConnectionFactory {  
   
   
   
   
   Connection connection;
   
   private ConnectionFactory() throws IOException {  
 
    }  
   
   public static Connection getDatabaseConnection() throws SQLException, IOException {
	   if(ServiceManage.dataSource!=null)
		   return ServiceManage.dataSource.getConnection(); 
	   
	   
Properties properties = new Properties();
	   
	   properties.load(ConnectionFactory.class.getResourceAsStream("/config.properties"));
	   
	   String driver = properties.getProperty("jdbc.driver");
	  
	    String url =  properties.getProperty("jdbc.url");
	    String username =  properties.getProperty("jdbc.user");
	    String password =  properties.getProperty("jdbc.password");
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	   
	    
	    return conn; 
    }  
   
   
   
   
   public static void main(String[] args) throws SQLException, IOException {
	System.out.println(new ConnectionFactory().getDatabaseConnection());
}
   

}  