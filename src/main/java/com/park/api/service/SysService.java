package com.park.api.service;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	
	
}
