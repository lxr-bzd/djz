package com.park.api.common;


import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.park.api.ServiceManage;

@Plugin(name = "DbAppender", category = "Core", elementType = "appender", printObject = true)
public class DbAppender extends AbstractAppender {

 

    //需要实现的构造方法，直接使用父类就行
    protected DbAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
            final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

	@Override
	public void append(LogEvent event) {
		
		if(ServiceManage.jdbcTemplate==null)
			return;
		
		System.out.println("====="+event.getLoggerName()+"++"+event.getMessage());
		
		
	}
	
	   /*  接收配置文件中的参数 */
    @PluginFactory
    public static DbAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginElement("Filter") final Filter filter,
                                              @PluginElement("Layout") Layout<? extends Serializable> layout,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("no name defined in conf.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new DbAppender(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void stop() {
       super.stop();
    }

   
}
