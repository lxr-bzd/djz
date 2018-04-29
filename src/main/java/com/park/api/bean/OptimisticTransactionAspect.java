package com.park.api.bean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.park.api.common.exception.OptimisticLockException;

@Component
@Aspect
public class OptimisticTransactionAspect {
	
	
	@Pointcut("@annotation(com.lxr.framework.web.IsTryAgain)")
	private void cut() {
		
	}    
	

	@Around("cut()")
	public void advice(ProceedingJoinPoint joinPoint) throws Throwable{
	    
		for (int i = 0; i < 5; i++) {
			
			try {
		        joinPoint.proceed();
		    } catch (OptimisticLockException e) {
		      if(i>=4)throw e;
		    }
		}
		
	    
	    
	}
}
