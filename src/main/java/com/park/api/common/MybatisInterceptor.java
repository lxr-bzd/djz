package com.park.api.common;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;  
import org.apache.ibatis.executor.statement.StatementHandler;  
import org.apache.ibatis.mapping.BoundSql;  
import org.apache.ibatis.mapping.MappedStatement;  
import org.apache.ibatis.plugin.*;  
import org.apache.ibatis.reflection.MetaObject;  
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import java.sql.*;  

@Intercepts({
     @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),  
     @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})  
public class MybatisInterceptor implements Interceptor {  
  


    //插件运行的代码，它将代替原有的方法
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	
    	if(!(invocation.getArgs()[0] instanceof MappedStatement))
    	    return invocation.proceed();  
    	
    	MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];  
        String sqlId = mappedStatement.getId();  
        String namespace = sqlId.substring(0, sqlId.indexOf('.'));  
        Executor exe = (Executor) invocation.getTarget();  
        String methodName = invocation.getMethod().getName();  
  
        if (methodName.equals("query")) {  
                  int i = 1;
                   
        }  
        else if(methodName.equals("update")){  
        	int i = 1;
              
        }  
        return invocation.proceed();  
    }
    
    /**
     * 拦截类型StatementHandler 
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {  
            return Plugin.wrap(target, this);  
        } else {  
            return target;  
        }  
    }

	@Override
	public void setProperties(Properties properties) {
	properties.get("wrfe");
	}
    
 
}