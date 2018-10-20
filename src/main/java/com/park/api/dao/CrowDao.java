package com.park.api.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;

import com.park.api.entity.Crow;
import com.park.api.entity.Game;


public interface CrowDao {
	
	/**
	 * 获取用户使用的表
	 * @param uid
	 * @return
	 */
	 Integer getGroup(@Param("uid")String uid);
	 
	 /**
	  * 复制模板到游戏表
	  * @param uid
	  * @param tbNo
	  */
	 void copy2ruing(@Param("uid")String uid,@Param("hid")String hid,@Param("tbNo")Integer tbNo);
	 
	 /**
	  * 
	  * @param mo
	  */
	 void save(@Param("hid")String hid,@Param("mo")Crow mo);
	
	 /**
	  * 获取焦点行
	  * @param hid
	  * @return
	  */
	 Crow getInputRow(@Param("hid")String hid);
	 
	
	 void update(@Param("model")Crow crow);
	 
	 void delete(@Param("uid")String uid);
	 
	 
	 
	 Integer getGameNum(@Param("uid")String uid);
	 
	 void setUserGroup(@Param("uid")String uid,@Param("group")Integer group);
	 
	 
	 //2018-8-28新加入
	 Crow getRow(@Param("hid")String hid,@Param("row")Integer row);
	 
	 
	 Integer getRuningCount(@Param("uid")String uid);
	 
	 List<Map> getAllGong(@Param("hid")String hid);
	 
	 String getUpTgVal(@Param("hid")String hid);
	 
	
	 /**
	  * 创建游戏记录
	  */
	 void createGame(@Param("mo")Game game);
	 
	 Game getRuningGame(@Param("uid")String uid);
	 
	 void updateGame(@Param("mo")Game game);
	 
	 
	 
}
