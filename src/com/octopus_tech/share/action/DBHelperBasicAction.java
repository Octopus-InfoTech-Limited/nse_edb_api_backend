package com.octopus_tech.share.action;

import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopus_tech.share.annotation.NoJson;
import com.octopus_tech.share.db.CRUDHelper;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.db.HibernateUtil;
import com.octopus_tech.share.util.chrono.Epoch;
import com.octopus_tech.share.util.chrono.EpochTypeAdapter;
import com.octopus_tech.share.util.chrono.TimeVector;
import com.octopus_tech.share.util.chrono.TimeVectorTypeAdapter;
import com.opensymphony.xwork2.ActionSupport;

public abstract class DBHelperBasicAction extends ActionSupport implements
	ServletRequestAware,
	ServletResponseAware,
	ServletContextAware,
	SessionAware
{
	private static final long serialVersionUID = -5166330125410469463L;
	
	public static final String NOTFOUND = "notfound";
	public static final String EXCEPTION = "exception";
	
	private Logger logger;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Map<String, Object> session;
	protected ServletContext servletContext;
	protected String applicationPath;
	
	public String _exceptionTrackId;
	
	public static Gson gson = new GsonBuilder()
			.serializeNulls()
			.registerTypeAdapter(Epoch.class, new EpochTypeAdapter())
			.registerTypeAdapter(TimeVector.class, new TimeVectorTypeAdapter())
			.addSerializationExclusionStrategy(new ExclusionStrategy()
			{
				
				@Override
				public boolean shouldSkipField(FieldAttributes f) 
				{
					return f.getAnnotation(NoJson.class) != null;
				}
				
				@Override
				public boolean shouldSkipClass(Class<?> clazz) 
				{
					return false;
				}
			})
			.create();
	
	public DBHelperBasicAction()
	{
		logger = LogManager.getLogger(this.getClass());
	}
	
	public String execute() throws Exception
	{
		HibernateUtil.initHibernateSessionFactory(servletContext);
		
		applicationPath = "/" + ((HttpServletRequest) request).getContextPath()
			.replace("/", "");

		String result = ActionSupport.SUCCESS;
		try(DBHelper dbHelper = new DBHelper())
		{
			CRUDHelper.autowireCRUD(dbHelper, this);
			result = this.execute(dbHelper, logger);
			dbHelper.commit();
		}
		catch(Exception e)
		{
			_exceptionTrackId = UUID.randomUUID().toString();
			
			logger.error("Exception track id: " + _exceptionTrackId);
			logger.catching(e);
			return EXCEPTION;
		}
		if(NOTFOUND.equals(result))
		{
			ServletActionContext.getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return result;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) 
	{
		this.response = response;
	}
	
	@Override
	public void setSession(Map<String, Object> session) 
	{
		this.session = session;
	}
	
	@Override
	public void setServletContext(ServletContext context)
	{
		this.servletContext = context;
	}
	
	protected abstract String execute(DBHelper dbHelper, Logger logger) throws Exception;
}
