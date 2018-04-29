package com.park.api.entity;

public class Crow {

	String id;
	
	String uid;//int(11)	 
	Integer crow;//int(11)	 
	String sheng;//varchar(713)	 '102组生组合'
	String pei;//varchar(713)	 '102组配组合'
	String dui;//varchar(1225)	 '102组兑组合，1：勾，2叉'
	String gong;//varchar(1225)	 '102组供组合，1：白老，2：白少，3：白男，4：白女，5：红老，6：红少，7：红男，8：红女'
	String gong_col;//102组供组合颜色，1：白，2：红
	String count;//varchar(1225)	 '统计'
	
	
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
	public Integer getCrow() {
		return crow;
	}
	public void setCrow(Integer crow) {
		this.crow = crow;
	}
	public String getSheng() {
		return sheng;
	}
	public void setSheng(String sheng) {
		this.sheng = sheng;
	}
	public String getPei() {
		return pei;
	}
	public void setPei(String pei) {
		this.pei = pei;
	}
	public String getDui() {
		return dui;
	}
	public void setDui(String dui) {
		this.dui = dui;
	}
	public String getGong() {
		return gong;
	}
	public void setGong(String gong) {
		this.gong = gong;
	}
	public String getGong_col() {
		return gong_col;
	}
	public void setGong_col(String gong_col) {
		this.gong_col = gong_col;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	
	
}
