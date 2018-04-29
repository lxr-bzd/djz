package com.park.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.park.api.entity.Tmpl;

public interface TmplDao {

	
	 Integer getTmplNum(@Param("group")int group);
	
	 
	 List<Tmpl> findTmpl(@Param("group")int group);
	 
}
