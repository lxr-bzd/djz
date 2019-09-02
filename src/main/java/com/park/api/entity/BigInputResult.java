package com.park.api.entity;

import java.util.List;

public class BigInputResult {
	
	List<InputResult> results;
	
	long[] hzBg;
	long[] hbBg;
	long[] xzBg;
	
	public List<InputResult> getResults() {
		return results;
	}

	public void setResults(List<InputResult> results) {
		this.results = results;
	}

	public long[] getHzBg() {
		return hzBg;
	}

	public void setHzBg(long[] hzBg) {
		this.hzBg = hzBg;
	}

	public long[] getHbBg() {
		return hbBg;
	}

	public void setHbBg(long[] hbBg) {
		this.hbBg = hbBg;
	}

	public long[] getXzBg() {
		return xzBg;
	}

	public void setXzBg(long[] xzBg) {
		this.xzBg = xzBg;
	}

	

}
