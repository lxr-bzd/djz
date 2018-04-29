package com.park.api.service;

import java.util.Map;

public interface UserService {

	Map<String, Object> getByAccount(String account);
	
	boolean isLock(String account);
	
	String getLockInfo(String account);
	
}
