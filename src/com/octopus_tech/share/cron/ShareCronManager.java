package com.octopus_tech.share.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections8.Reflections;
import org.reflections8.scanners.ResourcesScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.scanners.TypeAnnotationsScanner;
import org.reflections8.util.ClasspathHelper;
import org.reflections8.util.ConfigurationBuilder;
import org.reflections8.util.FilterBuilder;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;
import com.octopus_tech.share.db.CRUDHelper;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.db.HibernateUtil;
import com.octopus_tech.share.util.chrono.Epoch;
import com.octopus_tech.share.util.chrono.TimeVector;

public class ShareCronManager 
{
	private static ShareCronManager instance = null;

	private final Logger logger = LogManager.getLogger(ShareCronManager.class);
	private ServletContext servletContext;
	private ScheduledExecutorService scheduledExecutorService;
	private List<InstalledCron> crons = Collections.synchronizedList(new ArrayList<>());
	private List<String> logs = Collections.synchronizedList(new LinkedList<>());
	
	public static void install(ServletContext servletContext, ScheduledExecutorService scheduledExecutorService) throws InstantiationException, IllegalAccessException
	{
		if(instance == null)
		{
			instance = new ShareCronManager(servletContext, scheduledExecutorService);
		}
	}
	
	public static void uninstall()
	{
		instance = null;
	}
	
	public static ShareCronManager getManager()
	{
		return instance;
	}
	
	private ShareCronManager(ServletContext servletContext, ScheduledExecutorService scheduledExecutorService) throws InstantiationException, IllegalAccessException
	{
		this.scheduledExecutorService = scheduledExecutorService;
		this.servletContext = servletContext;
		scanTaskClasses();
		scheduledExecutorService.scheduleAtFixedRate(mainLoop, 10, 1, TimeUnit.SECONDS);
	}
	
	private void scanTaskClasses() throws InstantiationException, IllegalAccessException
	{
		List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
		classLoadersList.add(ClasspathHelper.contextClassLoader());
		classLoadersList.add(ClasspathHelper.staticClassLoader());

		Reflections reflections = new Reflections(new ConfigurationBuilder()
		    .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
		    .addScanners(new TypeAnnotationsScanner())
		    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0]))));
		    //.filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("org.your.package"))));
		Set<Class<?>> classes1 = reflections.getTypesAnnotatedWith(CronScheduleOn.class);
		Set<Class<?>> classes2 = reflections.getTypesAnnotatedWith(CronScheduleOnGroup.class);
		
		for(Class<?> clazz: classes1)
		{
			CronScheduleOn[] ccs = clazz.getAnnotationsByType(CronScheduleOn.class);
			ICronTask task = (ICronTask)clazz.newInstance();
			
			InstalledCron ic = new InstalledCron();
			ic.ccs = Arrays.asList(ccs);
			ic.task = task;
			crons.add(ic);
			
			logger.info("Installing cron {} with {} schedule(s)", clazz.getCanonicalName(), ccs.length);
		}
		
		for(Class<?> clazz: classes2)
		{
			CronScheduleOnGroup ccs = clazz.getAnnotation(CronScheduleOnGroup.class);
			ICronTask task = (ICronTask)clazz.newInstance();
			
			InstalledCron ic = new InstalledCron();
			ic.ccs = Arrays.asList(ccs.value());
			ic.task = task;
			crons.add(ic);
			
			logger.info("Installing cron {} with {} schedule(s)", clazz.getCanonicalName(), ccs.value().length);
		}
	}
	
	private Runnable mainLoop = new Runnable() 
	{
		@Override
		public void run()
		{
			try
			{
				HibernateUtil.initHibernateSessionFactory(servletContext);
				for(InstalledCron c:crons)
				{
					runCron(c);
				}
			}
			catch(Exception e)
			{
				throw new RuntimeException("error", e);
			}
		}
		
		void runCron(InstalledCron c)
		{
			CronScheduleOn cso = shouldRunCron(c);
			if(cso != null)
			{
				Logger logger = LogManager.getLogger(c.task.getClass());

				PrintWriter pw = new PrintWriter(new WeirdoWriter(logger), true);
				try(DBHelper dbHelper = new DBHelper())
				{
					synchronized (c.lock)
					{
						c.task.run(dbHelper, pw, cso);
					}
					dbHelper.commit();
					updateLastRun(c);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					
					pw.println(e.toString());
					logger.catching(e);
				}
			}
		}
		
		void updateLastRun(InstalledCron c)
		{
			c.lastrun = Epoch.now();
		}
		
		CronScheduleOn shouldRunCron(InstalledCron c)
		{
			Epoch now = Epoch.now();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(now.getEpoch() * 1000L);
			
			for(CronScheduleOn cc:c.ccs)
			{
				if(cc.day() < 0 && cc.hour() < 0 && cc.minute() < 0 && cc.second() < 0)
				{
					continue;
				}
				if(cc.day() >= 0 && calendar.get(Calendar.DAY_OF_MONTH) != cc.day())
				{
					continue;
				}
				if(cc.hour() >= 0 && calendar.get(Calendar.HOUR_OF_DAY) != cc.hour())
				{
					continue;
				}
				if(cc.minute() >= 0 && calendar.get(Calendar.MINUTE) != cc.minute())
				{
					continue;
				}
				if(cc.second() >= 0 && calendar.get(Calendar.SECOND) != cc.second())
				{
					continue;
				}
				return cc;
			}
			return null;
		}
	};
	
	private static class InstalledCron
	{
		Epoch lastrun = Epoch.ZERO;
		
		final Object lock = new Object();
		List<CronScheduleOn> ccs;
		ICronTask task;
	}
	
	private class WeirdoWriter extends Writer
	{
		Logger logger;
		StringBuffer sb = new StringBuffer();
		
		public WeirdoWriter(Logger logger)
		{
			super();
			this.logger = logger;
		}

		@Override
		public void close() throws IOException 
		{
			flush();
			
			synchronized (logs) 
			{
				while(logs.size() >= 25000)
				{
					logs.remove(0);
				}
			}
		}

		@Override
		public void flush() throws IOException
		{
			if(sb.length() > 0)
			{
				Epoch epoch = Epoch.now();
				
				String string = sb.toString();
				logger.info(string);
				logs.add(String.format("[%s] %s", epoch.toDateString(), string));
				sb = new StringBuffer();
			}
		}

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException
		{
			sb.append(cbuf, off, len);
		}
		
	}
}
