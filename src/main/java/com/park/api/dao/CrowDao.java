package com.park.api.dao;

import org.apache.ibatis.annotations.Param;

import com.park.api.entity.Crow;


public interface CrowDao {

	 Crow getLastRow(@Param("uid")String uid);
	 Crow getInputRow(@Param("uid")String uid);
	 
	
	 void update(@Param("model")Crow crow);
	 
	 void delete(@Param("uid")String uid);
	 
	 Crow getNextRow(@Param("uid")String id,@Param("row")Integer row);
	 
	 Crow getRow(@Param("uid")String id,@Param("row")Integer row);
	 
	 Integer getGameNum(@Param("uid")String uid);

	 
	 void save(@Param("mo")Crow mo);
	 
	 
	 Integer getGroup();
	 
	 
	 void setUserGroup(@Param("uid")String uid,@Param("group")Integer group);
	 
	 
	 
	 
}
