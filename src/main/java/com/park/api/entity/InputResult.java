package com.park.api.entity;

public class InputResult {

	
	String uid;
	Object[] rets;
	
	long[] yz;
	
	long tg_sum;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Object[] getRets() {
		return rets;
	}
	public void setRets(Object[] rets) {
		this.rets = rets;
	}
	public long[] getYz() {
		return yz;
	}
	public void setYz(long[] yz) {
		this.yz = yz;
	}
	public long getTg_sum() {
		return tg_sum;
	}
	public void setTg_sum(long tg_sum) {
		this.tg_sum = tg_sum;
	}
	
	
	
	
}
