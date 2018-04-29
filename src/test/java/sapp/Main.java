package sapp;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.text.MessageFormat;

import javax.enterprise.inject.New;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.lxr.pay.wxpay.utils.MsgType;


public class Main {

    public static void main(String[] args) throws Exception {
    	
    	
        // 1. 创建一个顶层容器（窗口）
        JFrame jf = new JFrame("查看url");          // 创建窗口
        jf.setSize(600, 250);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        // 2. 创建中间容器（面板容器）
        JPanel panel = new JPanel();                // 创建面板容器，使用默认的布局管理器

        // 3. 创建一个基本组件（按钮），并添加到 面板容器 中
        JTextArea btn = new JTextArea();
        btn.setFont(new Font("宋体",Font.PLAIN,18));
        String ip = getLocalIP();
        btn.setText(MessageFormat.format(getMsg(), ip));
        panel.add(btn);
        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }
    public static String getLocalIP() throws Exception{  
    	  String localIP = "";  
    	  InetAddress addr = (InetAddress) InetAddress.getLocalHost();  
    	  //获取本机IP  
    	  localIP = addr.getHostAddress().toString();  
    	  return localIP;  
    	  }  
    
    public static String getMsg() {
    	String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    	
    	String msg = null;
    
    	try {
    		jarPath = URLDecoder.decode(jarPath,"utf-8");
        	if(new File(jarPath).isFile())jarPath = new File(jarPath).getParent();
        	
        	String path = new File(jarPath,"msg.txt").getPath();
    	msg = readFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if(msg!=null) return msg;
    	
  	 return "请保证在服务器运行这个程序，才能获取正确的路径\n"
  	 		+ "前台路径：http://{0}:8080/djt/login.html"
     		+"\n后台路径：http://{0}:8080/djt-back-manage";
  	 
  	  }  
    
    private static String readFile(String filepath) throws FileNotFoundException, IOException {
    	  StringBuilder sb = new StringBuilder();
    	  String s ="";
    	  BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), "utf-8"));

    	  while( (s = br.readLine()) != null) {
    	  sb.append(s + "\n");
    	  }

    	  br.close();
    	  String str = sb.toString();
    	  
    	  
    	  return str;
    	 }

}
