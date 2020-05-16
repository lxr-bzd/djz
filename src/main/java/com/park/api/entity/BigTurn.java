package com.park.api.entity;

import com.park.api.service.bean.BigTurnConfig;

public class BigTurn {
	
	Integer id;
	Integer frow;
	
	String bg;
	String gj;
	
	String zdbg;
	String zdbg_jg;
	Long zdbg_sum;
	Long zdbg_jg_sum;
	String zdbg_lock;


/*	String bga;
	String bga_jg;
	Long bga_sum;
	String bga_last_jg;
	Long bga_jg_sum;

	String bgb;
	String bgb_jg;
	Long bgb_sum;
	String bgb_last_jg;
	Long bgb_jg_sum;*/
	
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


	String bkhz;
	String bkhz_jg;
	Long bkhz_sum;
	Long bkhz_jg_sum;
	String bkhz_lock;

	String bkqh;
	String bkqh_jg;
	Long bkqh_sum;
	Long bkqh_jg_sum;

	String bkzd_lock;

	String turn_bkhz_lock;

	String bkbg_inv_lock;

	String bkhz_inv_lock;
	
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

	public String getBkzd_lock() {
		return bkzd_lock;
	}

	public void setBkzd_lock(String bkzd_lock) {
		this.bkzd_lock = bkzd_lock;
	}

	public String getBkbg_inv_lock() {
		return bkbg_inv_lock;
	}

	public void setBkbg_inv_lock(String bkbg_inv_lock) {
		this.bkbg_inv_lock = bkbg_inv_lock;
	}

	public String getBkhz() {
		return bkhz;
	}

	public void setBkhz(String bkhz) {
		this.bkhz = bkhz;
	}

	public String getBkhz_jg() {
		return bkhz_jg;
	}

	public void setBkhz_jg(String bkhz_jg) {
		this.bkhz_jg = bkhz_jg;
	}

	public Long getBkhz_sum() {
		return bkhz_sum;
	}

	public void setBkhz_sum(Long bkhz_sum) {
		this.bkhz_sum = bkhz_sum;
	}

	public Long getBkhz_jg_sum() {
		return bkhz_jg_sum;
	}

	public void setBkhz_jg_sum(Long bkhz_jg_sum) {
		this.bkhz_jg_sum = bkhz_jg_sum;
	}

	public String getBkqh() {
		return bkqh;
	}

	public void setBkqh(String bkqh) {
		this.bkqh = bkqh;
	}

	public String getBkqh_jg() {
		return bkqh_jg;
	}

	public void setBkqh_jg(String bkqh_jg) {
		this.bkqh_jg = bkqh_jg;
	}

	public Long getBkqh_sum() {
		return bkqh_sum;
	}

	public void setBkqh_sum(Long bkqh_sum) {
		this.bkqh_sum = bkqh_sum;
	}

	public Long getBkqh_jg_sum() {
		return bkqh_jg_sum;
	}

	public void setBkqh_jg_sum(Long bkqh_jg_sum) {
		this.bkqh_jg_sum = bkqh_jg_sum;
	}

	public String getBkhz_lock() {
		return bkhz_lock;
	}

	public void setBkhz_lock(String bkhz_lock) {
		this.bkhz_lock = bkhz_lock;
	}

	public String getTurn_bkhz_lock() {
		return turn_bkhz_lock;
	}

	public void setTurn_bkhz_lock(String turn_bkhz_lock) {
		this.turn_bkhz_lock = turn_bkhz_lock;
	}

	public String getBkhz_inv_lock() {
		return bkhz_inv_lock;
	}

	public void setBkhz_inv_lock(String bkhz_inv_lock) {
		this.bkhz_inv_lock = bkhz_inv_lock;
	}

	public String getZdbg() {
		return zdbg;
	}

	public void setZdbg(String zdbg) {
		this.zdbg = zdbg;
	}

	public String getZdbg_jg() {
		return zdbg_jg;
	}

	public void setZdbg_jg(String zdbg_jg) {
		this.zdbg_jg = zdbg_jg;
	}

	public Long getZdbg_sum() {
		return zdbg_sum;
	}

	public void setZdbg_sum(Long zdbg_sum) {
		this.zdbg_sum = zdbg_sum;
	}

	public Long getZdbg_jg_sum() {
		return zdbg_jg_sum;
	}

	public void setZdbg_jg_sum(Long zdbg_jg_sum) {
		this.zdbg_jg_sum = zdbg_jg_sum;
	}

	public String getZdbg_lock() {
		return zdbg_lock;
	}

	public void setZdbg_lock(String zdbg_lock) {
		this.zdbg_lock = zdbg_lock;
	}
}
