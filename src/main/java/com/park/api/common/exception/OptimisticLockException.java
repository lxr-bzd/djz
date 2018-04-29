package com.park.api.common.exception;

import com.lxr.commons.exception.ApplicationException;
import com.park.api.common.BaseController;

public class OptimisticLockException extends ApplicationException{

	public OptimisticLockException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OptimisticLockException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public OptimisticLockException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public OptimisticLockException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
