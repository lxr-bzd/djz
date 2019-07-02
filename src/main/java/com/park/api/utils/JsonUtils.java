package com.park.api.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;

public class JsonUtils {
	
	
	public static  Long[] toLongArray(String arrStr) {
		if(StringUtils.isEmpty(arrStr))return null;
		List<Long> upHbqhList = JSONArray.parseArray(arrStr,Long.class);
		return upHbqhList.toArray(new Long[upHbqhList.size()]);
		

	}
	

	public static  Integer[] toIntArray(String arrStr) {
		if(StringUtils.isEmpty(arrStr))return null;
		List<Integer> upHbqhList = JSONArray.parseArray(arrStr,Integer.class);
		return upHbqhList.toArray(new Integer[upHbqhList.size()]);
		

	}
}
