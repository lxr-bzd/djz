package sapp;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class DllTest {

	public interface Dll extends Library{
		Dll instance = (Dll)Native.loadLibrary("D:\\Documents\\Visual Studio 2013\\Projects\\Win32Project1\\x64\\Release\\Win32Project1", Dll.class);
		//public String countQueue();
		public String reckonDui(String sheng,String pei);
	}
	
	/**
	 * 
	 * @param sheng 36进制 四位一组,ag:AB0P,09OP...
	 * @param pei
	 * @return 1:对 ，2：错
	 */
	public static String reckonDui(String sheng,String pei) {
		String[] shengs = sheng.split(",");
		
		StringBuilder duis = new StringBuilder();
		
		for (int i = 0; i < shengs.length; i++) {
			String ts = shengs[i];
			for (int j = 0; j < ts.length(); j++) {
				Integer a = Integer.parseInt(ts.substring(j, j+1));
				Integer b = Integer.parseInt(pei.substring(j, j+1));
				boolean jls = a>2?b>2:b<3;
				boolean jnn = (a%2==0)?(b%2==0):b%2==1;
				
				duis.append(jls?"1":"2");
				duis.append(jnn?"1":"2");
				
			}
			duis.append(",");
			
			
		}
		
		return duis.deleteCharAt(duis.length()-1).toString();
	}
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("sun.arch.data.model"));
		
		String sheng = "";
		
		for (int i = 0; i < 1000; i++) {
			if(i+1<1000)
			sheng+="1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,";
			
			else sheng+="1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111,1111111111";
		}
		
		//String sum = Dll.instance.countQueue();
		long start = System.currentTimeMillis();
		
		
		String a = null;
				
		a = Dll.instance.reckonDui(sheng,"1111111111");
		System.out.println(System.currentTimeMillis()-start);
		start = System.currentTimeMillis();
		a = reckonDui(sheng,"1111111111");
		System.out.println(System.currentTimeMillis()-start);
		start = System.currentTimeMillis();
		a = Dll.instance.reckonDui(sheng,"1111111111");
		System.out.println(System.currentTimeMillis()-start);
		
		System.out.println(a.length());
	}

}
