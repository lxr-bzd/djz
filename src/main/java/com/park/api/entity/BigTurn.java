package com.park.api.entity;

import com.park.api.service.bean.BigTurnConfig;

public class BigTurn {
	
	Integer id;
	Integer frow;
	
	String bg;
	String bg_hb;
	String jg;
	String jg_hb;
	String gj;
	Integer state;
	String config_json;
	
	BigTurnConfig bigTurnConfig;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFrow() {
		return frow;
	}
	public void setFrow(Integer frow) {
		this.frow = frow;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public String getBg_hb() {
		return bg_hb;
	}
	public void setBg_hb(String bg_hb) {
		this.bg_hb = bg_hb;
	}
	public String getJg() {
		return jg;
	}
	public void setJg(String jg) {
		this.jg = jg;
	}
	public String getJg_hb() {
		return jg_hb;
	}
	public void setJg_hb(String jg_hb) {
		this.jg_hb = jg_hb;
	}
	public String getGj() {
		return gj;
	}
	public void setGj(String gj) {
		this.gj = gj;
	}
	
	
	public String getConfig_json() {
		return config_json;
	}
	public void setConfig_json(String config_json) {
		this.config_json = config_json;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public BigTurnConfig getBigTurnConfig() {
		return bigTurnConfig;
	}
	public void setBigTurnConfig(BigTurnConfig bigTurnConfig) {
		this.bigTurnConfig = bigTurnConfig;
	}
	
	
	
	
	

}
