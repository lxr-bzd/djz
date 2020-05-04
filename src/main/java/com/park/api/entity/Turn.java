package com.park.api.entity;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.park.api.service.bean.GameConfig;

public class Turn {
	
	String id;
	Integer mod;
	Integer mod2;
	String rule;
	Integer rule_type;
	
	String config_json;
	
	GameConfig config;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMod() {
		return mod;
	}
	public void setMod(Integer mod) {
		this.mod = mod;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public Integer getRule_type() {
		return rule_type;
	}
	public void setRule_type(Integer rule_type) {
		this.rule_type = rule_type;
	}
	public Integer getMod2() {
		return mod2;
	}
	public void setMod2(Integer mod2) {
		this.mod2 = mod2;
	}
	public GameConfig getConfig() {
		return config;
	}
	public void setConfig(GameConfig config) {
		this.config = config;
	}
	public String getConfig_json() {
		return config_json;
	}
	public void setConfig_json(String config_json) {
		this.config_json = config_json;
		if(StringUtils.isNotBlank(config_json))
			setConfig(JSONObject.parseObject(config_json, GameConfig.class));
		else setConfig(null);
	
	}
	
	

}