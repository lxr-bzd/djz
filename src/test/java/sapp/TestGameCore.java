package sapp;

import java.util.Random;

import com.park.api.service.GameCoreService2;
import com.park.api.service.bean.GameConfig;
import com.park.api.service.impl.GameCoreServiceImpl;
import com.park.api.service.impl.GameCoreServiceImpl2;

public class TestGameCore {
	
	public static String create16String(GameConfig config) {
		 Random random = new Random();
		 
			 StringBuilder sheng = new StringBuilder();
			 for (int j = 0; j < config.gameGroupNum; j++) {
				 String item = GameCoreService2.createShengStr(random.nextInt(1024), random.nextInt(1024));
				 sheng.append(item+",");
			}
			return sheng.substring(0,sheng.length()-1);
	}
	
	
	public static String createPei16String(int pei) {
		
		String bString = "";
		switch (pei) {
		case 1:
			bString+="00";
			break;
		case 2:
			bString+="01";
			break;
		case 3:
			bString+="10";
			break;
		case 4:
			bString+="11";
			break;
		}
		
		String ret = "";
		for (int i = 0; i < 5; i++) {
			ret+=bString;
		}
		
		
		
		int v = Integer.parseInt(ret,2);
		
				 String item = GameCoreService2.createShengStr(v, v);
			return item;
	}
	
	
	private static String createPei(int p) {
		
		String ret = "";
		for (int i = 0; i < 10; i++) {
			ret+=p;
		}
		return ret;

	}
	
	
	public static void main(String[] args) {
		
		GameConfig config = new GameConfig();
		config.gameGroupNum = 10;
		
		Random random = new Random();
		String sheng = create16String(config);
		int pei = random.nextInt(4);
		
		GameCoreServiceImpl2 newCore = new GameCoreServiceImpl2();
		GameCoreServiceImpl gameCore = new GameCoreServiceImpl();
		
		System.out.println("生："+sheng);
		System.out.println("配："+pei);
		
		System.out.println(""+newCore.reckonDui(config, sheng, createPei16String(pei)));
		System.out.println(""+gameCore.reckonDui(config, sheng, createPei(pei)));
		
		
		
		
	}

}
