package com.lxr.framework;

import com.lxr.framework.long1.Pagination;

public class PaginationFactory {

	public static Pagination pagination(BaseFilter filter) {
		Pagination p = new Pagination();
		p.setLimit(filter.getLimit());
		p.setPage(filter.getPage());

		return p;
	}
	
}
