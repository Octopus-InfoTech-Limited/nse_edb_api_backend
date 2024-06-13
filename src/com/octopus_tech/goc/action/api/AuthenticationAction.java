package com.octopus_tech.goc.action.api;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.Logger;
import org.apache.struts2.util.ServletContextAware;

import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;
import com.octopus_tech.share.sso.edconnect.EDConnectSSO;
import com.octopus_tech.share.util.EnhancedProperties;
import com.octopus_tech.share.util.PropertiesHelper;

public class AuthenticationAction extends DBHelperBasicAction
{
	private static final long serialVersionUID = 8633826514879362164L;

	public String _mode;
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger) throws Exception 
	{
		EnhancedProperties ep = PropertiesHelper.getApplicationProperties(servletContext);
		EDConnectSSO sso = new EDConnectSSO(ep);
		
		if("sso".equals(_mode))
		{
			response.sendRedirect(sso.getLoginUrl());
			return NONE;
		}
		else if("logout".equals(_mode))
		{
			session.clear();
			response.sendRedirect(sso.getLogoutUrl(ep.getComplexProperty("sso.edconnect.finish_uri", "")));
			return NONE;
		}
		return SUCCESS;
	}
}
