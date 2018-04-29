package com.lxr.framework.utils;

import com.lxr.framework.long1.Pagination;

public class PageUtils {

	
	public static void setTotalPage(Pagination page) {
		int n = 0;
		if((page.getTotalCount()%page.getLimit())>0)n =1;
		
        page.setTotalPage(page.getTotalCount()/page.getLimit()+n);

	}
	
}
