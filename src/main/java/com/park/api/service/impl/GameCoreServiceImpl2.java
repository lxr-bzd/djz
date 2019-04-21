package com.park.api.service.impl;

import com.park.api.service.GameCoreService;
import com.park.api.service.bean.GameConfig;

/**
 * 
 ** 生数据转换二进制：
 * 1=> 00
 * 2=> 01
 * 3=> 10
 * 4=> 11
 **生数据转换16进制：生数据五位一组，转换为二进制，再转换为16进制，两个5位合成一组
 *
 **配数据和生数据一样
 *
 **兑数据转换二进制
 * √=> 1
 * X=> 0
 **兑数据转换16进制:
 *
 **供数据转换二进制
 *	老=> 1
 * 	少=> 0
 * 	男=> 0
 * 	女=> 1
 *
 *
 */

public class GameCoreServiceImpl2 implements GameCoreService{
	
	static final int COUNT_VLEN = 11;
	/*户数*/
	static final int SHENG_GROUP_NUM = 10;
	static final int SHENG_ITEM_SIZE = 4;//生行一组大小
	

	/**
	 **异或逻辑
	 */
	@Override
	public String reckonDui(GameConfig conf,String sheng, String pei) {

		int group = conf.gameGroupNum;
		int groupLength = SHENG_ITEM_SIZE;
		int v = 0b1111111111;
		
		int pei1 = Long.valueOf(pei.substring(0,2), 36).intValue();
		int pei2 = Long.valueOf(pei.substring(2,4), 36).intValue();;
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			int sheng1 = Long.valueOf(sheng.substring(st,st+2), 36).intValue();
			int sheng2 = Long.valueOf(sheng.substring(st+2,st+4), 36).intValue();
			
			int ret1 = sheng1^pei1^v;
			int ret2 = sheng2^pei2^v;
			
			duis.append(create16Str(ret1,ret2)+",");
			
			
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}

	/**
	 * * 与逻辑
	 *
	 * @param sheng
	 * @param dui 
	 * @return 
	 */
	@Override
	public String reckonGong(GameConfig conf,String sheng, String dui) {
		int group = conf.gameGroupNum;
		int groupLength = SHENG_ITEM_SIZE;
		
		
		StringBuilder gongs = new StringBuilder();
			
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			int sheng1 = Long.valueOf(sheng.substring(st,st+2), 36).intValue();
			int sheng2 = Long.valueOf(sheng.substring(st+2,st+4), 36).intValue();
			
			int dui1 = Long.valueOf(dui.substring(st,st+2), 36).intValue();
			int dui2 = Long.valueOf(dui.substring(st+2,st+4), 36).intValue();
			
			int ret1 = sheng1&dui1;
			int ret2 = sheng2&dui2;
			
			gongs.append(create16Str(ret1,ret2)+",");
		}
			
			
		return gongs.deleteCharAt(gongs.length()-1).toString();
	}

	@Override
	public String reckonGongCol(GameConfig conf,String pei, String gong) {
		int group = conf.gameGroupNum;
		int groupLength = SHENG_ITEM_SIZE;
		int v = 0b1111111111;
		
		int pei1 = Long.valueOf(pei.substring(0,2), 36).intValue();
		int pei2 = Long.valueOf(pei.substring(2,4), 36).intValue();;
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < group; i++) {
			int st = i*(groupLength+1);
			int gong1 = Long.valueOf(gong.substring(st,st+2), 36).intValue();
			int gong2 = Long.valueOf(gong.substring(st+2,st+4), 36).intValue();
			
			int ret1 = gong1^pei1^v;
			int ret2 = gong2^pei2^v;
			
			duis.append(create16Str(ret1,ret2)+",");	
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}
	
	
	
	/**
	 * 
	 * @param ten1 前10位十进制
	 * @param ten2 后10位十进制
	 * @return
	 */
	private static String create16Str(int ten1,int ten2) {
		String str = null;
		if(ten1<36) {
			str="0"+Long.toString(ten1, 36).toUpperCase();
		}else str=Long.toString(ten1, 36).toUpperCase();
			
		if(ten2<36) {
			str+="0"+Long.toString(ten2, 36).toUpperCase();
		}else str+=Long.toString(ten2, 36).toUpperCase();
		
		return str;
	}
	

	
}
