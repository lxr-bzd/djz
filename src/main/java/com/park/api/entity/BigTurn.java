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


	String bga;
	String bga_jg;
	Long bga_sum;
	String bga_last_jg;
	Long bga_jg_sum;

	String bgb;
	String bgb_jg;
	Long bgb_sum;
	String bgb_last_jg;
	Long bgb_jg_sum;

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

	public String getBga() {
		return bga;
	}

	public void setBga(String bga) {
		this.bga = bga;
	}

	public String getBga_jg() {
		return bga_jg;
	}

	public void setBga_jg(String bga_jg) {
		this.bga_jg = bga_jg;
	}

	public Long getBga_sum() {
		return bga_sum;
	}

	public void setBga_sum(Long bga_sum) {
		this.bga_sum = bga_sum;
	}

	public String getBga_last_jg() {
		return bga_last_jg;
	}

	public void setBga_last_jg(String bga_last_jg) {
		this.bga_last_jg = bga_last_jg;
	}

	public Long getBga_jg_sum() {
		return bga_jg_sum;
	}

	public void setBga_jg_sum(Long bga_jg_sum) {
		this.bga_jg_sum = bga_jg_sum;
	}

	public String getBgb() {
		return bgb;
	}

	public void setBgb(String bgb) {
		this.bgb = bgb;
	}

	public String getBgb_jg() {
		return bgb_jg;
	}

	public void setBgb_jg(String bgb_jg) {
		this.bgb_jg = bgb_jg;
	}

	public Long getBgb_sum() {
		return bgb_sum;
	}

	public void setBgb_sum(Long bgb_sum) {
		this.bgb_sum = bgb_sum;
	}

	public String getBgb_last_jg() {
		return bgb_last_jg;
	}

	public void setBgb_last_jg(String bgb_last_jg) {
		this.bgb_last_jg = bgb_last_jg;
	}

	public Long getBgb_jg_sum() {
		return bgb_jg_sum;
	}

	public void setBgb_jg_sum(Long bgb_jg_sum) {
		this.bgb_jg_sum = bgb_jg_sum;
	}

	public String getZd_lock() {
		return zd_lock;
	}

	public void setZd_lock(String zd_lock) {
		this.zd_lock = zd_lock;
	}
}
