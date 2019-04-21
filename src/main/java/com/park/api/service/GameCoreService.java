package com.park.api.service;

import com.park.api.service.bean.GameConfig;

public interface GameCoreService {
	
	
	/**
	 * 
	 * @param sheng
	 * @param pei
	 * @return
	 */
	public String reckonDui(GameConfig conf,String sheng,String pei);

	/**
	 * 
	 * @param sheng
	 * @param dui
	 * @return
	 */
	public String reckonGong(GameConfig conf,String sheng,String dui);
	
	/**
	 * 
	 * @param pei
	 * @param gong
	 * @return
	 */
	public String reckonGongCol(GameConfig conf,String pei,String gong);
	
}
