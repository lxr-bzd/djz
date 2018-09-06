package com.park.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;

import com.park.api.entity.Crow;
import com.park.api.entity.Game;


public interface CrowDao {

	 Crow getLastRow(@Param("uid")String uid);
	 Crow getInputRow(@Param("uid")String uid);
	 
	
	 void update(@Param("model")Crow crow);
	 
	 void delete(@Param("uid")String uid);
	 
	 
	 
	 Integer getGameNum(@Param("uid")String uid);

	 
	 void save(@Param("mo")Crow mo);
	 
	 
	 Integer getGroup();
	 
	 
	 void setUserGroup(@Param("uid")String uid,@Param("group")Integer group);
	 
	 
	 //2018-8-28新加入
	 Crow getRow(@Param("hid")String hid,@Param("row")Integer row);
	 
	 Crow getInputRow2(@Param("hid")String hid);
	 
	 Integer getRuningCount(@Param("uid")String uid);
	 
	 List<Map> getAllGong(@Param("hid")String hid);
	 
	 /**
	  * 复制模板到游戏表
	  * @param uid
	  * @param tbNo
	  */
	 void temp2ruing(@Param("uid")String uid,@Param("hid")String hid,@Param("tbNo")Integer tbNo);
	 /**
	  * 创建游戏记录
	  */
	 void createGame(@Param("mo")Game game);
	 
	 Game getRuningGame(@Param("uid")String uid);
	 
	 void updateGame(@Param("mo")Game game);
	 
	 
	 
}
