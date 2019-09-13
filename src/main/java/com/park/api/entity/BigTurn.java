package com.park.api.entity;

import com.park.api.service.bean.BigTurnConfig;

public class BigTurn {
	
	Integer id;
	Integer frow;
	
	String bg;
	String gj;
	
	String hz_jg;
	String hb_jg;
	String xz_hb;
	
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

	public String getGj() {
		return gj;
	}

	public void setGj(String gj) {
		this.gj = gj;
	}

	public String getHz_jg() {
		return hz_jg;
	}

	public void setHz_jg(String hz_jg) {
		this.hz_jg = hz_jg;
	}

	public String getHb_jg() {
		return hb_jg;
	}

	public void setHb_jg(String hb_jg) {
		this.hb_jg = hb_jg;
	}

	public String getXz_hb() {
		return xz_hb;
	}

	public void setXz_hb(String xz_hb) {
		this.xz_hb = xz_hb;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getConfig_json() {
		return config_json;
	}

	public void setConfig_json(String config_json) {
		this.config_json = config_json;
	}

	public BigTurnConfig getBigTurnConfig() {
		return bigTurnConfig;
	}

	public void setBigTurnConfig(BigTurnConfig bigTurnConfig) {
		this.bigTurnConfig = bigTurnConfig;
	}
	
	
	

}
