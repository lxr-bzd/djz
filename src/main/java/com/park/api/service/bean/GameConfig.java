package com.park.api.service.bean;

public class GameConfig {

	public int gameGroupNum = 100;//总组数
	/*户数*/
	public int gameGroupLen = 10;
	
	/*户数*/
	public int gameRowNum = 212;
	
	public int isTemporary = 1;
	
	/**合并报告选择表 **/
	public  int hbBg = 0;
	/**合并求和选择表 **/
	public  int hbQh = 0;
	
	public int getGameGroupNum() {
		return gameGroupNum;
	}
	public void setGameGroupNum(int gameGroupNum) {
		this.gameGroupNum = gameGroupNum;
	}
	public int getGameGroupLen() {
		return gameGroupLen;
	}
	public void setGameGroupLen(int gameGroupLen) {
		this.gameGroupLen = gameGroupLen;
	}
	public int getGameRowNum() {
		return gameRowNum;
	}
	public void setGameRowNum(int gameRowNum) {
		this.gameRowNum = gameRowNum;
	}
	public int getIsTemporary() {
		return isTemporary;
	}
	public void setIsTemporary(int isTemporary) {
		this.isTemporary = isTemporary;
	}
	public int getHbBg() {
		return hbBg;
	}
	public void setHbBg(int hbBg) {
		this.hbBg = hbBg;
	}
	public int getHbQh() {
		return hbQh;
	}
	public void setHbQh(int hbQh) {
		this.hbQh = hbQh;
	}
	
	
	
	
	
}
