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


	String tg_trends;

	String jgzd_lock;

	String jgzd;
	String jgzd_jg;
	Long jgzd_sum;
	Long jgzd_jg_sum;

	String inverse_lock;

	String xb_inv_lock;
	String xb_lock;


	String bkbg;
	String bkbg_jg;
	Long bkbg_sum;
	Long bkbg_jg_sum;

	String bkbg_trend;



	String bkzd;
	String bkzd_jg;
	Long bkzd_sum;
	Long bkzd_jg_sum;


	
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

	public String getJgzd_lock() {
		return jgzd_lock;
	}

	public void setJgzd_lock(String jgzd_lock) {
		this.jgzd_lock = jgzd_lock;
	}

	public String getTg_trends() {
		return tg_trends;
	}

	public void setTg_trends(String tg_trends) {
		this.tg_trends = tg_trends;
	}

	public String getJgzd() {
		return jgzd;
	}

	public void setJgzd(String jgzd) {
		this.jgzd = jgzd;
	}

	public String getJgzd_jg() {
		return jgzd_jg;
	}

	public void setJgzd_jg(String jgzd_jg) {
		this.jgzd_jg = jgzd_jg;
	}

	public Long getJgzd_sum() {
		return jgzd_sum;
	}

	public void setJgzd_sum(Long jgzd_sum) {
		this.jgzd_sum = jgzd_sum;
	}

	public Long getJgzd_jg_sum() {
		return jgzd_jg_sum;
	}

	public void setJgzd_jg_sum(Long jgzd_jg_sum) {
		this.jgzd_jg_sum = jgzd_jg_sum;
	}

	public String getInverse_lock() {
		return inverse_lock;
	}

	public void setInverse_lock(String inverse_lock) {
		this.inverse_lock = inverse_lock;
	}

	public String getXb_inv_lock() {
		return xb_inv_lock;
	}

	public void setXb_inv_lock(String xb_inv_lock) {
		this.xb_inv_lock = xb_inv_lock;
	}

	public String getXb_lock() {
		return xb_lock;
	}

	public void setXb_lock(String xb_lock) {
		this.xb_lock = xb_lock;
	}

	public String getBkbg() {
		return bkbg;
	}

	public void setBkbg(String bkbg) {
		this.bkbg = bkbg;
	}

	public String getBkbg_jg() {
		return bkbg_jg;
	}

	public void setBkbg_jg(String bkbg_jg) {
		this.bkbg_jg = bkbg_jg;
	}

	public Long getBkbg_sum() {
		return bkbg_sum;
	}

	public void setBkbg_sum(Long bkbg_sum) {
		this.bkbg_sum = bkbg_sum;
	}

	public Long getBkbg_jg_sum() {
		return bkbg_jg_sum;
	}

	public void setBkbg_jg_sum(Long bkbg_jg_sum) {
		this.bkbg_jg_sum = bkbg_jg_sum;
	}

	public String getBkzd() {
		return bkzd;
	}

	public void setBkzd(String bkzd) {
		this.bkzd = bkzd;
	}

	public String getBkzd_jg() {
		return bkzd_jg;
	}

	public void setBkzd_jg(String bkzd_jg) {
		this.bkzd_jg = bkzd_jg;
	}

	public Long getBkzd_sum() {
		return bkzd_sum;
	}

	public void setBkzd_sum(Long bkzd_sum) {
		this.bkzd_sum = bkzd_sum;
	}

	public Long getBkzd_jg_sum() {
		return bkzd_jg_sum;
	}

	public void setBkzd_jg_sum(Long bkzd_jg_sum) {
		this.bkzd_jg_sum = bkzd_jg_sum;
	}

	public String getBkbg_trend() {
		return bkbg_trend;
	}

	public void setBkbg_trend(String bkbg_trend) {
		this.bkbg_trend = bkbg_trend;
	}
}
