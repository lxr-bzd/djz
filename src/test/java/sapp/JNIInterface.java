package sapp;

public class JNIInterface {
	  static{
		  System.loadLibrary("JNITest");
		  //
		  }
	    public native String reckonDui(String sheng,String pei);
	
}
