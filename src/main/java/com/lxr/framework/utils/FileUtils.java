package com.lxr.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.lxr.framework.WebContext;

public class FileUtils {

	

	  
	
	public static String upload(File temFile,String fileName) {
		
		String uri = "image/"+getMD5(temFile)+"."+getSuffix(fileName);
		
		File newFile = new File(WebContext.WEB_ROOT,uri);
		
		if(newFile.isFile())return uri;
		
		fileChannelCopy(temFile, newFile);
		
		return uri;
		

	}
	
	
	public static String getSuffix(String fileName) {
		
		return fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		

	}
	
	
	
	public static String getMD5(File file) {
		   String value = null;
	        FileInputStream in = null;
	    try {
	        in = new FileInputStream(file);
	        ByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        md5.update(byteBuffer);
	        BigInteger bi = new BigInteger(1, md5.digest());
	        value = bi.toString(16);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	            if(null != in) {
	                try {
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return value;
	}
	

	   /**

	    * 使用文件通道的方式复制文件

	    * 

	    * @param s

	    *            源文件

	    * @param t

	    *            复制到的新文件

	    */

	    public static void fileChannelCopy(File s, File t) {

	        FileInputStream fi = null;

	        FileOutputStream fo = null;

	        FileChannel in = null;

	        FileChannel out = null;

	        try {

	            fi = new FileInputStream(s);

	            fo = new FileOutputStream(t);

	            in = fi.getChannel();//得到对应的文件通道

	            out = fo.getChannel();//得到对应的文件通道

	            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

	        } catch (IOException e) {

	            e.printStackTrace();

	        } finally {

	            try {

	                fi.close();

	                in.close();

	                fo.close();

	                out.close();

	            } catch (IOException e) {

	                e.printStackTrace();

	            }

	        }

	    }
	    
	    
	    public static void main(String[] args) {
	    	System.out.println("cbaa195fd424f1acae32bfe97a2946aa".equals("cbaa195fd424f1acae32bfe97a2946aa"));
			System.err.println(getMD5(new File("D:\\QuickSetup.exe")));
		}
	    
}
