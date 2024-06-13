package com.octopus_tech.share;

import java.util.EnumSet;
import java.util.HashSet; 
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.InstantiationException;

import com.octopus_tech.share.cron.ShareCronManager;
import com.octopus_tech.share.db.HibernateUtil;
import com.octopus_tech.share.filter.CORSOptionFilter;
import com.octopus_tech.share.filter.HTMLUtf8ify;
import com.octopus_tech.share.filter.StructFilter; 
import com.octopus_tech.share.util.EnhancedProperties;
import com.octopus_tech.share.util.PropertiesHelper;

@WebListener
public class ShareServletContextListener implements ServletContextListener
{
	public static final String CONTEXT_ATTR_SHARE_EXECUTOR = "SHARE_EXECUTOR";
	
	private ScheduledExecutorService scheduledExecutorService = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties(sce.getServletContext());
		int threadCount = 1;
		String threadCountStr = ep.getComplexProperty("share.cron.thread", "1");
		threadCount = NumberUtils.toInt(threadCountStr, 1);
		if(threadCount <= 0)
		{
			threadCount = 1;
		}
		
		ServletContext context = sce.getServletContext();
		ThreadFactory daemonFactory = new DaemonThreadFactory(context);
		scheduledExecutorService = Executors.newScheduledThreadPool(threadCount, daemonFactory);
		
		context.setAttribute("SHARE_EXECUTOR", scheduledExecutorService);

		try
		{
			ShareCronManager.install(sce.getServletContext(), scheduledExecutorService);
		}
		catch (Exception e)
		{
			throw new RuntimeException("could not start cron job", e);
		}
		
		initFilters(sce);
		initErrorPages(sce);
		initTaglibs(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
		ServletContext context = sce.getServletContext();
		
		ShareCronManager.uninstall();
		
		HibernateUtil.destoryDBSessionFactory();
		if(scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
			try
			{
				scheduledExecutorService.awaitTermination(10, TimeUnit.SECONDS);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			scheduledExecutorService.shutdownNow();
			scheduledExecutorService = null;
			context.setAttribute("SHARE_EXECUTOR", null);
		}
	}
	
	private void initFilters(ServletContextEvent sce)
	{
		FilterRegistration.Dynamic dynamic;

		dynamic = sce.getServletContext().addFilter(
				CORSOptionFilter.class.getCanonicalName(), 
				CORSOptionFilter.class
		);
		dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

		dynamic = sce.getServletContext().addFilter(
				HTMLUtf8ify.class.getCanonicalName(), 
				HTMLUtf8ify.class
		);
		dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "*.html");
		
//		dynamic = sce.getServletContext().addFilter(
//				SetCharacterEncodingFilter.class.getCanonicalName(),
//				SetCharacterEncodingFilter.class
//		);
		dynamic.setInitParameter("encoding", "UTF-8");
		dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
		
		dynamic = sce.getServletContext().addFilter(
				StructFilter.class.getCanonicalName(), 
				StructFilter.class
		);
		dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
	}
	
	private void initErrorPages(ServletContextEvent sce)
	{
		
	}
	
	private void initTaglibs(ServletContextEvent sce)
	{
	}
	
	public static class DaemonThreadFactory implements ThreadFactory
	{
		ServletContext context;
		
		public DaemonThreadFactory(ServletContext context)
		{
			this.context = context;
		}
		
		@Override
		public Thread newThread(Runnable run) 
		{
			Thread ret = new Thread(run);
			ret.setDaemon(true);
			ret.setName(context.getContextPath().replace("/", "") + " background task");
			ret.setContextClassLoader(context.getClassLoader());
			return ret;
		}
		
	}
}
