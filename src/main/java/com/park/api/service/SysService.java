package com.park.api.service;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.park.api.ServiceManage;
import com.park.api.dao.SysDao;
@Service
public class SysService {

	@Autowired
	SysDao sysDao;
	
	public boolean isLock() {
		Map<String, Object> map = sysDao.getTime();
		if(map==null||map.get("val")==null)return false;
		
		Integer all = Integer.valueOf(map.get("val").toString());
		if(all<0)return false;
		
		if(map.get("used")==null)return false;
		Integer used = Integer.valueOf(map.get("used").toString());
		if(used>=all)return true;
		return false;

	}
	
	
	public <T> T getSysConfig(String ckey,Class<T> reType){
		
		 T t = ServiceManage.jdbcTemplate.queryForObject("select val from djt_sys where ckey=?", reType,ckey);
		 return t;
		
	}
	
	public Map<String, String> getAllConfig() {
		List<Map<String, Object>> list = ServiceManage.jdbcTemplate.queryForList("select ckey,val from djt_sys ");
		Map<String, String> map = new HashMap<String, String>(); 
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).get("ckey").toString(), list.get(i).get("val").toString());
		}
		
		return map;

	}
	
}
