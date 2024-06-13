package com.octopus_tech.share.db;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.reflections8.Reflections;

import com.octopus_tech.share.model.DBModel;
import com.octopus_tech.share.util.EnhancedProperties;
import com.octopus_tech.share.util.PropertiesHelper;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

public final class HibernateUtil 
{
	private static SessionFactory sessionFactory = null;
	private static SessionFactory proxiedSessionFactory = null;
    private static StandardServiceRegistry serviceRegistry = null;
    private static long lastCheck = 0;

	private HibernateUtil() 
	{
	}

    public synchronized static void initHibernateSessionFactory(ServletContext servletContext)
    {
    	if(sessionFactory == null)
    	{
	    	EnhancedProperties ep = PropertiesHelper.getApplicationProperties(servletContext);
	    	
	    	List<String> hibernateKeys = ep.keySet()
	    			.stream()
	    			.filter(t->t instanceof String)
	    			.map(t->t.toString())
	    			.filter(t->t.startsWith("hibernate."))
	    			.collect(Collectors.toList());

	    	String _package = ep.get("hibernate.model.package", "com.octopus_tech.share.model");
	    	
	        try 
	        {
	        	Configuration configuration = new Configuration();
	        	
	        	for(String key: hibernateKeys)
	        	{
				    configuration.setProperty(key, ep.getComplexProperty(key, ""));
	        	}
	        	
			    configuration.addPackage(_package);
	            HibernateUtil.listAllModels(_package)
	            	.forEach(configuration::addAnnotatedClass);
	
	            serviceRegistry = new StandardServiceRegistryBuilder()
	                               .applySettings(configuration.getProperties()).build();
	            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	        } 
	        catch (Throwable ex)
	        {
	            throw new ExceptionInInitializerError(ex);
	        }
    	}
    }
    
    public synchronized static void destoryDBSessionFactory()
    {
    	if(sessionFactory != null)
    	{
	    	try
	    	{
	    		sessionFactory.getCurrentSession().close();
	    	}
	    	catch(Exception e) {}
	    	
	    	try
	    	{
	    		sessionFactory.close();
	    	}
	    	catch(Exception e) {}
	    	
	    	sessionFactory = null;
    	}


    	if(serviceRegistry != null)
    	{
	    	try
	    	{
	    		serviceRegistry.close();
	    	}
	    	catch(Exception e) {}
    	}
    }
    
    public static Set<Class<? extends DBModel>> listAllModels(String _package)
    {
    	Reflections ref = new Reflections();
    	return ref.getSubTypesOf(DBModel.class);
    }
    
    private void initProxiedSessionFactory() throws Exception
    {
    	ProxyFactory pf = new ProxyFactory();
    	pf.setInterfaces(new Class<?>[] {SessionFactory.class});
    	proxiedSessionFactory = (SessionFactory)pf.create(new Class<?>[0], new Object[0], new MethodHandlerImpl());
    }
    
    static synchronized SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    private static class MethodHandlerImpl implements MethodHandler
    {
		@Override
		public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable 
		{
			return thisMethod.invoke(sessionFactory, args);
		}
    	
    }
}
