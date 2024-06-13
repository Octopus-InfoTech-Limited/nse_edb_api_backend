package com.octopus_tech.share.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.octopus_tech.share.util.PropertiesHelper.PropertiesNotFoundException;

public class EnhancedProperties extends Properties
{
	private Logger logger = LogManager.getLogger(PropertiesHelper.class);
	private Set<String> loadedProperties = new HashSet<String>();
	
	public EnhancedProperties() 
	{
		super();
	}

	public EnhancedProperties(Properties defaults) 
	{
		super(defaults);
	}

	public Number getNumber(String key)
	{
		Object obj = this.getProperty(key);
		if(obj != null && obj instanceof Number)
		{
			return (Number)obj;
		}
		if(NumberUtils.isParsable(obj.toString()))
		{
			return NumberUtils.createNumber(obj.toString());
		}
		
		return null;
	}
	
	public Number getNumber(String key, Number defaultValue)
	{
		Number ret = getNumber(key);
		if(ret != null)
		{
			return ret;
		}
		return defaultValue;
	}
	
	public <T> T get(String key, T defaultValue)
	{
		Class<T> clazz = (Class<T>) defaultValue.getClass();
		
		Object ret = getOrDefault(key, defaultValue);
		if(!clazz.isInstance(ret))
		{
			return defaultValue;
		}
		return (T)ret;
	}
	
	public String getComplexProperty(String key, String defaultValue)
	{
		String ret = this.getProperty(key, defaultValue);
		if(ret != null)
		{
			final String regex = "\\$\\{([a-zA-Z0-9\\.\\-]+)\\}";
	    	final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	    	final Matcher matcher = pattern.matcher(ret);
	
	    	while(matcher.find())
	    	{
	    		String propKey = matcher.group(1);
	    		String propVal = this.getComplexProperty(propKey, "");
	    		
	    		ret = ret.replace("${" + propKey + "}", propVal);
	    	}
		}
    	return ret;
	}
	
	public void load(InputStream is, ServletContext servletContext) throws IOException
	{
		this.load(is);
		loadIncludeProperties(servletContext);
		loadRequireProperties(servletContext);
	}
	
	private void loadIncludeProperties(ServletContext servletContext) throws IOException
	{
		Set<String> keys = keySet()
				.stream()
				.filter(t->t instanceof String)
				.map(t->t.toString())
				.filter(t->t.startsWith("enhancedproperty.include."))
				.collect(Collectors.toSet());
		
		for(String key:keys)
		{
			String val = this.getProperty(key);
			if(val != null || val.trim().length() > 0)
			{
				if(loadedProperties.contains(val.trim()))
				{
					continue;
				}
				loadedProperties.add(val.trim());
				
				try(InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/classes/resources/" + val.trim()))
				{
					this.load(inputStream, servletContext);
				}
				catch(Exception e)
				{
					logger.warn("cannot include properties, {} in /WEB-INF/classes/resources/", key);
					logger.warn(e);
				}
			}
		}
	}
	
	private void loadRequireProperties(ServletContext servletContext) throws IOException
	{
		Set<String> keys = keySet()
				.stream()
				.filter(t->t instanceof String)
				.map(t->t.toString())
				.filter(t->t.startsWith("enhancedproperty.require."))
				.collect(Collectors.toSet());
		
		for(String key:keys)
		{
			String val = this.getProperty(key);
			if(val != null || val.trim().length() > 0)
			{
				if(loadedProperties.contains(val.trim()))
				{
					continue;
				}
				loadedProperties.add(val.trim());
				
				try(InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/classes/resources/" + val.trim()))
				{
					this.load(inputStream, servletContext);
				}
				catch(Exception e)
				{
					logger.error("cannot include properties, {} in /WEB-INF/classes/resources/", key);
					logger.error(e);
					
					throw new PropertiesNotFoundException(String.format("cannot include properties, {} in /WEB-INF/classes/resources/", key), e);
				}
			}
		}
	}
}
