package com.park.api;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.park.api.service.SecurityService;
import com.park.api.service.UserService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ServiceManage {
	
	
	
	public static SecurityService securityService;
	
	public static JdbcTemplate jdbcTemplate;

	public static NamedParameterJdbcTemplate namedJdbcTemplate;
	
	
	public static DataSource dataSource;
	

	
	 @Autowired
	 public void setSecurityService(SecurityService s){
		 
		 securityService = s;
	 }
	 
	 @Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		 ServiceManage.jdbcTemplate = jdbcTemplate;
		 ServiceManage.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}
	 
	 @Autowired
	 public void setDataSource(DataSource dataSource) {
		 ServiceManage.dataSource = dataSource;
	}
	 
	 
	
}
