package com.park.api.entity;

import com.park.api.service.bean.BigTurnConfig;

public class BigTurn {
	
	Integer id;
	Integer frow;
	
	String bg;
	String gj;
	
	String zd;
	String zd_jg;
	Long zd_sum;
	String zd_last_jg;
	Long zd_jg_sum;

	String zd_lock;
	
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

	public String getZd() {
		return zd;
	}

	public void setZd(String zd) {
		this.zd = zd;
	}

	public String getZd_jg() {
		return zd_jg;
	}

	public void setZd_jg(String zd_jg) {
		this.zd_jg = zd_jg;
	}

	public Long getZd_sum() {
		return zd_sum;
	}

	public void setZd_sum(Long zd_sum) {
		this.zd_sum = zd_sum;
	}

	public String getZd_last_jg() {
		return zd_last_jg;
	}

	public void setZd_last_jg(String zd_last_jg) {
		this.zd_last_jg = zd_last_jg;
	}

	public Long getZd_jg_sum() {
		return zd_jg_sum;
	}

	public void setZd_jg_sum(Long zd_jg_sum) {
		this.zd_jg_sum = zd_jg_sum;
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

	public String getZd_lock() {
		return zd_lock;
	}

	public void setZd_lock(String zd_lock) {
		this.zd_lock = zd_lock;
	}
}
