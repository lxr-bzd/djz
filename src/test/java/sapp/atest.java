package sapp;

import java.util.Random;

public class atest {
	 public static void main(String[] args) {
		 StringBuilder builder = new StringBuilder("123,");
			System.out.println(Long.toBinaryString(Long.valueOf("SF",36)));
			System.out.println(Long.valueOf("DX", 36));
			System.out.println(Long.toString(682,36));
			
			Random random = new Random();
			for (int i = 0; i < 100; i++) {
				int v = random.nextInt(20);
				System.out.println(v+"_"+((v+1)%2==0?4:0));
			}
	}
	 
	 
	 
	 
	
}
