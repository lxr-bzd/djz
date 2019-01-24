package com.park.api.entity;

public class Game {

	String id;
	String uid;
	String tid;
	Integer tbNum;
	Integer focus_row;
	String info;
	Long createtime;
	Long endtime;
	Integer state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public Integer getTbNum() {
		return tbNum;
	}
	public void setTbNum(Integer tbNum) {
		this.tbNum = tbNum;
	}
	
	
	
	public Integer getFocus_row() {
		return focus_row;
	}
	public void setFocus_row(Integer focus_row) {
		this.focus_row = focus_row;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public Long getEndtime() {
		return endtime;
	}
	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
