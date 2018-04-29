package com.park.api.common;

import java.io.File;
import java.io.IOException;

import javax.enterprise.inject.New;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.lxr.commons.exception.ApplicationException;


public class SpringFileupload {
	
	
	public static String upload(MultipartFile  file,String rpath,PathResolve pathResolve) throws IllegalStateException, IOException {
		 if (file.isEmpty()) return null;
	           
	            String fileName=file.getOriginalFilename();// 文件原名称
	            
	            // 判断文件类型
	            fileName = java.net.URLEncoder.encode(fileName,"utf-8").replace("%", "");
	             
                String webroot = new File(rpath).getParent();
	            
                
                
	           String path = null;
	           if(pathResolve==null)
	        		   defPath(webroot,fileName, rpath);
	           else path = pathResolve.resolve(webroot,fileName, rpath);
	                   
	           
	           File tofile = new File(webroot,path);
	           if(!tofile.getParentFile().exists())
	           tofile.getParentFile().mkdirs();
	                    // 转存文件到指定的路径
	           if(pathResolve==null||!pathResolve.onUpload(file))
	             file.transferTo(tofile);
	           
	               
	            
	                    System.out.println("上传的文件 :"+fileName); 
	                    
	        return "/"+path;

	}
	
	private static String defPath(String webRoot,String fileName,String rpath) {
		   File file2  = new File(rpath);
		   
           
           String imgroot = file2.getName()+"-image";
           
                   String realPath= new File(webRoot,imgroot).getPath();
                   
                   String trueFileName=String.valueOf(System.currentTimeMillis())+fileName;
                  
                   if(!new File(realPath).exists())
                   	new File(realPath).mkdirs();
                  
                   String path=realPath+"/"+trueFileName;
                   
                   return imgroot+"/"+trueFileName;
                   
	}
	
	
	public static String upload(MultipartHttpServletRequest  request,String name,PathResolve pathResolve) throws IllegalStateException, IOException {
		MultipartHttpServletRequest multipartRequest =  request;         
		// 获得文件：
		MultipartFile knowledge_icon_url = (MultipartFile) multipartRequest.getFile(name); 
	       
		String rpath = request.getSession().getServletContext().getRealPath("/");
		return upload(knowledge_icon_url, rpath,pathResolve);

	}
	
	public static String upload(HttpServletRequest  request,String name,PathResolve pathResolve) {
		 CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
	       
		  String path = null;
	        // 判断是否有文件上传  
	        if (!commonsMultipartResolver.isMultipart(request))throw new MultipartException("Could not parse multipart servlet request");
	
		
		
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;         
		
	try {
			return upload(multipartRequest, name,pathResolve);
		}  catch (Exception e1) {
			throw new ApplicationException(e1);
		}
			

	}
	
	
	public abstract interface PathResolve{
		
		/**
		 * 
		 * @param webRoot web根路径
		 * @param fileName 上传文件名
		 * @param rpath 项目路径
		 * @return 访问的相对路径
		 */
		String resolve(String webRoot,String fileName,String rpath);
		
		/**
		 * 上传拦截
		 * @param multipartFile
		 * @return
		 */
		boolean onUpload(MultipartFile multipartFile);
		
		
	}
	
	
	

}
