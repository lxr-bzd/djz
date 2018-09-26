package com.park.api.entity;

public class Crow {

	String id;
	
	String uid;//int(11)	 
	Integer row;//int(11)	 
	String sheng;//varchar(713)	 '102组生组合'
	String pei;//varchar(713)	 '102组配组合'
	String dui;//varchar(1225)	 '102组兑组合，1：勾，2叉'
	String gong;//varchar(1225)	 '102组供组合，1：白老，2：白少，3：白男，4：白女，5：红老，6：红少，7：红男，8：红女'
	String gong_col;//102组供组合颜色，1：白，2：红
	String count;//varchar(1225)	 '统计'
	
	String a_tg;
	String b_tg;
	String c_tg;
	
	Integer is_finish;
	
	String tg_val;
	
	
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
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
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
	public void setIs_finish(Integer is_finish) {
		this.is_finish = is_finish;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	public String getA_tg() {
		return a_tg;
	}
	public void setA_tg(String a_tg) {
		this.a_tg = a_tg;
	}
	public String getB_tg() {
		return b_tg;
	}
	public void setB_tg(String b_tg) {
		this.b_tg = b_tg;
	}
	public String getC_tg() {
		return c_tg;
	}
	public void setC_tg(String c_tg) {
		this.c_tg = c_tg;
	}
	public Integer getIs_finish() {
		return is_finish;
	}
	public String getTg_val() {
		return tg_val;
	}
	public void setTg_val(String tg_val) {
		this.tg_val = tg_val;
	}
	
	
	
}
