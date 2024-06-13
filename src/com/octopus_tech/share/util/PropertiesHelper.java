package com.octopus_tech.share.util;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class PropertiesHelper
{
	private PropertiesHelper() {}
	
	public static EnhancedProperties getApplicationProperties() throws PropertiesNotFoundException
	{
		return getProperties("application.properties");
	}

	public static EnhancedProperties getApplicationProperties(ServletContext servletContext) throws PropertiesNotFoundException
	{
		return getProperties(servletContext, "application.properties");
	}
	
	public static EnhancedProperties getProperties(String filename) throws PropertiesNotFoundException
	{
		return getProperties(ServletActionContext.getServletContext(), filename);
	}
	
	public static EnhancedProperties getProperties(ServletContext servletContext, String filename) throws PropertiesNotFoundException
	{
		Logger logger = LogManager.getLogger(PropertiesHelper.class);
		
		EnhancedProperties prop = new EnhancedProperties();
		String path = servletContext.getRealPath("/WEB-INF/classes/resources/");
		logger.debug("path is " + path);
		try(InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/classes/resources/" + filename))
		{
			prop.load(inputStream, servletContext);
		}
		catch(Exception e)
		{
			logger.error("cannot found properties, application.properties in /WEB-INF/classes/resources/");
			logger.error(e);
			
			throw new PropertiesNotFoundException("cannot found properties, application.properties in /WEB-INF/classes/resources/", e);
		}

		return prop;
	}
	
	public static class PropertiesNotFoundException extends IllegalArgumentException
	{
		public PropertiesNotFoundException(String reason, Exception cause)
		{
			super(reason, cause);
		}
	}
}
