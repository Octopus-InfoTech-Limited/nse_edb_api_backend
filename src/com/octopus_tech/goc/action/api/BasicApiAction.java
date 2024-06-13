package com.octopus_tech.goc.action.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;

import com.octopus_tech.goc.model.User;
import com.octopus_tech.share.action.DBHelperBasicAction;
import com.octopus_tech.share.db.DBHelper;

public abstract class BasicApiAction extends DBHelperBasicAction
{
	private Map<String, Object> responseMap;
	public String json = "";
	
	BasicApiAction()
	{
		responseMap = new HashMap<String, Object>();
	}
	
	@Override
	protected String execute(DBHelper dbHelper, Logger logger) throws Exception 
	{
		String ret = execute(dbHelper, logger, responseMap);
		if(SUCCESS.equals(ret) && json.length() == 0 && responseMap.size() > 0)
		{
			json = gson.toJson(responseMap);
		}
		return ret;
	}
	
	protected boolean isAuthenticated()
	{
		User user = (User)session.get("user");
		return user != null;
	}
	
	protected void responseUnauthorised()
	{
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	protected abstract String execute(DBHelper dbHelper, Logger logger, Map<String, Object> responseMap) throws Exception;
}
