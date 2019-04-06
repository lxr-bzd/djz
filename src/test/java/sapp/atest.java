package sapp;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.json.Json;

import com.alibaba.fastjson.JSONArray;
import com.park.api.service.GameCoreService2;

public class atest {
	 public static void main(String[] args) {
		 
		List<Long> aaString =  JSONArray.parseArray(JSONArray.parseArray("[[0,2000000,2000000,0],[2000000,-2000000]]").getString(1),Long.class);
		 StringBuilder builder = new StringBuilder("123,");
			System.out.println(Long.toBinaryString(Long.valueOf("SF",36)));
			System.out.println(Long.valueOf("DX", 36));
			System.out.println(Long.toString(682,36));
			
			p2();
			/*Random random = new Random();
			for (int i = 0; i < 1024; i++) {
				
				System.out.println(Arrays.toString(GameCoreService2.shengMap[i]));
			}*/
	}
	 
	 
	 private static void p() {
		 String[] str = "GWBV,HCFU,P09E,FZQY,H70A,DAHB,JWEC,7EIC,AGP7,DSLP,F4NE,GXC4,M9CL,BNC9,HPDB,9NFQ,AG43,8TC2".split(",");
		
		 for (int i = 0; i < str.length; i++) {
			
			 int j =  Long.valueOf(str[i].substring(0, 2), 36).intValue();
			 System.out.print(Arrays.toString(GameCoreService2.shengMap[j]));
			 j =  Long.valueOf(str[i].substring(2, 4), 36).intValue();
			 System.out.println(Arrays.toString(GameCoreService2.shengMap[j]));
			 
		}
		 
	}
	 
	 
	 private static void p2() {
		 String[] str = "17BG,AALD,CJEU,3REG,AWNH,4RBT,GGOD,97F8,ES4U,86OA,8BCV,64LD,IPDV,ESOE".split(",");
		
		 Random random = new Random();
		 for (int i = 0; i < str.length; i++) {
			String v = "";
			for (int j = 0; j < 10; j++) {
				v+=random.nextInt(4)+1+",";
			} 
			System.out.println(v);
			 
		}
		 
	}
	
}
