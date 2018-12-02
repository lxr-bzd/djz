package com.park.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.park.api.entity.Count;

public interface CountDao {

	void save(@Param("hid")String hid,@Param("rule_type")Integer rule_type,@Param("rule")String rule,@Param("tid")String tid);
	
	List<Count> findAll(@Param("hid")String hid);
	
	void update(@Param("mo")Count count);
	
}
