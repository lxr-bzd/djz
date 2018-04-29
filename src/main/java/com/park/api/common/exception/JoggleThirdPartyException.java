package com.park.api.common.exception;

import com.lxr.commons.exception.ApplicationException;

/**
 * 第三方接口调用异常
 * @author Administrator
 *
 */
public class JoggleThirdPartyException extends ApplicationException{
	
	public Object result;
	

	public JoggleThirdPartyException() {
		super();
	}

	public JoggleThirdPartyException(String message, Throwable cause) {
		super(message, cause);
	}

	public JoggleThirdPartyException(String message) {
		super(message);
	}
	

	public JoggleThirdPartyException(Throwable cause) {
		super(cause);
	}
	
	
	/**
	 * 
	 * @param message
	 * @param result 第三方接口返回原始数据
	 */
	public JoggleThirdPartyException(String message,Object result) {
		
		super(message);
		
		this.result = result;
	}

	
	
}
