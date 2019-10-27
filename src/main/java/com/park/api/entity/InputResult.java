package com.park.api.entity;

public class InputResult {

	
	String uid;
	Object[] rets;
	
	long[] yz;
	
	
	
	long jg_qh;
	
	int queueNum;
	int queueCount;
	
	//本次的结果总和
	long jg_sum;
	//上一次的结果总和
	long up_jg_sum;
	//当前产生的结果
	int jg;
	
	int tgTrend;
	
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
	
	public int getQueueNum() {
		return queueNum;
	}
	public void setQueueNum(int queueNum) {
		this.queueNum = queueNum;
	}
	public int getQueueCount() {
		return queueCount;
	}
	public void setQueueCount(int queueCount) {
		this.queueCount = queueCount;
	}
	public long getJg_sum() {
		return jg_sum;
	}
	public void setJg_sum(long jg_sum) {
		this.jg_sum = jg_sum;
	}
	public long getJg_qh() {
		return jg_qh;
	}
	public void setJg_qh(long jg_qh) {
		this.jg_qh = jg_qh;
	}
	public long getUp_jg_sum() {
		return up_jg_sum;
	}
	public void setUp_jg_sum(long up_jg_sum) {
		this.up_jg_sum = up_jg_sum;
	}
	public int getJg() {
		return jg;
	}
	public void setJg(int jg) {
		this.jg = jg;
	}
	public int getTgTrend() {
		return tgTrend;
	}
	public void setTgTrend(int tgTrend) {
		this.tgTrend = tgTrend;
	}
	
	
	
	
}
