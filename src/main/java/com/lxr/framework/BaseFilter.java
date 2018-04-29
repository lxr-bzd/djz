package com.lxr.framework;

import java.util.Date;

public class BaseFilter {
	
	

	int page=1;
	
	int limit = 10;
	
	String kwd;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getKwd() {
		return kwd;
	}

	public void setKwd(String kwd) {
		this.kwd = kwd;
	}

	
	
	
}
